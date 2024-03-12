package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.miembro.Notificacion;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MiembrosController {
	@Getter
	private static final Repositorio<Miembro> repositorio;

	static {
		repositorio = FactoryRepositorio.get(Miembro.class);
	}

	public static Miembro buscarPorUsuario(Usuario usuario) {
		List<Miembro> miembros = repositorio.buscarTodos();
		for(Miembro miembro : miembros) {
			Usuario usuarioAsociado = miembro.getUsuarioAsociado();
			if(usuarioAsociado != null && miembro.getUsuarioAsociado().getId().equals(usuario.getId()))
				return miembro;
		}

		return null;
	}

	public static ModelAndView getVerMiembro(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		Map<String, Object> model = new HashMap<>();
		Miembro miembro = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		model.put("miembro", miembro);

		//el administrador puede modificar usuarios y dar/sacar privilegios
		if(usuario.tieneRol(TipoRol.ADMINISTRADOR_PLATAFORMA)) {
			//para no darse privilegio a uno mismo..
			if(!usuario.getId().equals(miembro.getUsuarioAsociado().getId()))
				model.put("puedeDarPrivilegio", true);

			if(miembro.getUsuarioAsociado().tieneRol(TipoRol.ADMINISTRADOR_COMUNIDAD))
				model.put("miembroEsAdministradorComunidad", true);
		}
		return new ModelAndView(model, "usuarios/miembro.hbs");
	}

	public static Response postDarPrivilegio(Request request, Response response) {
		Usuario usuario = UsuariosController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("id")));

		if(usuario.tieneAlgunRol(TipoRol.MIEMBRO, TipoRol.ADMINISTRADOR_COMUNIDAD)) {
			if (usuario.tieneRol(TipoRol.ADMINISTRADOR_COMUNIDAD))
				usuario.setTipoRol(TipoRol.MIEMBRO);
			else
				usuario.setTipoRol(TipoRol.ADMINISTRADOR_COMUNIDAD);
		}

		UsuariosController.getRepositorio().modificar(usuario);
		response.redirect("/miembros/ver?id=" + MiembrosController.buscarPorUsuario(usuario).getId());

		return response;
	}

	public static Response setearUbicacion(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		if(miembro != null) {
			double lat = Double.parseDouble(request.queryParams("lat"));
			double lon = Double.parseDouble(request.queryParams("lon"));

			//TO-DO: por más que esté este chequeo, si el tiempo entre 2 request es muy corto se ejecuta 2 veces!!
			//sólo setear si pasó más de una hora desde la ult. vez
			if(miembro.getUbicacionUltimaModif() == null ||
					LocalDateTime.now().isAfter(miembro.getUbicacionUltimaModif().plusSeconds(10))) {

				miembro.setUbicacionActual2(new Ubicacion(lat, lon));
				miembro.setUbicacionUltimaModif(LocalDateTime.now());

			/*System.out.printf("user: %s - ubicacionActual: %f %f",
					miembro.getUsuarioAsociado().getNameUser(), lat, lon);*/

				List<Incidente> incidentesCercanos = IncidentesController.getRepositorio().buscarTodos()
						.stream().filter(incidente ->
								incidente.getComunidad() != null &&
								incidente.getFechaCierre() == null &&
								miembro.perteneceAcomunidad(incidente.getComunidad()) &&
								incidente.estaProximoA(lat, lon, 3))
						.collect(Collectors.toList());

				incidentesCercanos.forEach(incidente -> {
					Notificacion notif = new Notificacion();
					notif.setMiembroReceptor(miembro);
					notif.setIncidenteReportado(incidente);
					String textoNoti = "";
					textoNoti = "Nuestro sistema ha detectado que se encuentra cerca de "
							+ incidente.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular().nombreMostrado()
							+ " de " + incidente.getEntidad().getNombre() + ", el cual tiene un incidente no cerrado en el servicio "
							+ incidente.getUnaPrestacionDeServicio().nombreDelServicio()
							+ ". Le pedimos, a fin de tener información precisa, si puede tomarse un tiempo " +
							"para revisar el lugar e indicar si el servicio sigue interrumpido.";
					notif.setTexto(textoNoti);

					notif.notificarAlMiembro();
					NotificacionesController.getRepositorio().agregar(notif); //persistir cada notificación
				});

				repositorio.modificar(miembro);
			}
		}

		return response;
	}

	public static Response postMarcarInteres(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		Entidad entidadElegida = EntidadesController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("entidad_id")));

		if(miembro.getEntidadesDeInteres().contains(entidadElegida)) {
			miembro.getEntidadesDeInteres().remove(entidadElegida);
		}
		else {
			miembro.getEntidadesDeInteres().add(entidadElegida);
		}
		repositorio.modificar(miembro);
		response.redirect("/entidades/ver?id=" + entidadElegida.getId());
		return response;
	}

	public static Response removerMiembro(Request request, Response response) {
		Miembro miembro = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		if(miembro != null) {
			ComunidadController.getRepositorio().buscarTodos()
					.forEach(comu -> comu.getUnosMiembros().remove(miembro));
			UsuariosController.getRepositorio().eliminar(miembro.getUsuarioAsociado());
			repositorio.eliminar(miembro);
			response.redirect("/usuarios/ver");
		} else {
			response.status(404);
		}

		return response;
	}

	public static void controlarAcceso(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(request.pathInfo().contains("marcarInteres") || request.pathInfo().contains("setearUbicacion"))
			return;

		if(!usuario.tieneRol(TipoRol.ADMINISTRADOR_PLATAFORMA))
			Spark.halt(403);
	}
}
