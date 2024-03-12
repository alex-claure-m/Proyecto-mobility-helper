package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.validadores.ValidadorDeDatosDeIncidente;
import ar.edu.utn.frba.dds.controllers.values.DatosDeIncidente;
import ar.edu.utn.frba.dds.controllers.values.IncidenteDto;
import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.miembro.Notificacion;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
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
import java.util.*;
import java.util.stream.Collectors;

public class IncidentesController {
	@Getter
	private static Repositorio<Incidente> repositorio = FactoryRepositorio.get(Incidente.class);

	//getAbrirIncidente - devuelve la pantalla para abrir un incidente
	public static ModelAndView getCrearIncidente(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		PrestacionServicio prestacionServicio = FactoryRepositorio.get(PrestacionServicio.class)
				.buscar(Integer.parseInt(request.queryParams("prestacion_id")));

		List<Comunidad> comunidades = miembro.getComunidadesALasQuePertenece();

		Map<String, Object> model = new HashMap<>();
		model.put("comunidades", comunidades);
		model.put("nombreDeServicio", prestacionServicio.getUnServicio().getNombre());

		List<Entidad> entidadesPosibles = EntidadesController.getRepositorio().buscarTodos().stream()
				.filter(entidad -> entidad.getEstablecimientos().contains(prestacionServicio.getUnEstablecimientoEnParticular()))
				.collect(Collectors.toList());

		model.put("entidadesPosibles", entidadesPosibles);

		return new ModelAndView(model, "incidentes/crear.hbs");
	}

	//getVerIncidentes - devuelve la pantalla con todos los incidentes
	public static ModelAndView getVerIncidentes(Request request, Response response) {
		//si no está logueado redirigir a login (lo demás no tendrá efecto)
		//LoginController.forzarLogueo(request, response);
		if(request.queryParams("id") != null)
			return getVerIncidente(request, response);

		List<Incidente> incidentes = repositorio.buscarTodos();

		Map<String, Object> model = new HashMap<>();
		model.put("incidentes", incidentes); //Nota: no funciona sin la dependencia 'handlebars 4.3.1'!!!
		model.put("usuario", UsuariosController.obtenerDeRequest(request));
		return new ModelAndView(model, "incidentes/incidentes.hbs");
	}

	public static ModelAndView postCrearIncidente(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		DatosDeIncidente datosDeIncidente = new DatosDeIncidente();
		datosDeIncidente.descripcion = request.queryParams("resumen");
		datosDeIncidente.textoDescripcion = request.queryParams("texto");
		datosDeIncidente.comunidadId = request.queryParams("comunidad_id");
		datosDeIncidente.prestacionId = request.queryParams("prestacion_id");
		datosDeIncidente.entidadId = request.queryParams("entidad_id");

		Validacion validacion = ValidadorDeDatosDeIncidente.validar(datosDeIncidente);
		if(validacion.getCodigo() == 0) {
			//recuperar la comunidad afectada
			Comunidad comunidad = ComunidadController.getRepositorio()
					.buscar(Integer.parseInt(datosDeIncidente.comunidadId));

			PrestacionServicio prestacionServicio = PrestacionServicioController.getRepositorio()
					.buscar(Integer.parseInt(datosDeIncidente.prestacionId));

			Entidad entidad = EntidadesController.getRepositorio()
					.buscar(Integer.parseInt(datosDeIncidente.entidadId));

			Incidente incidente = new Incidente();
			incidente.setFechaApertura(LocalDateTime.now());
			incidente.setDescripcion(datosDeIncidente.descripcion);
			incidente.setTextoDescripcion(datosDeIncidente.textoDescripcion);
			incidente.setUnaPrestacionDeServicio(prestacionServicio);
			incidente.setMiembro(miembro);
			incidente.setComunidad(comunidad);
			incidente.setEntidad(entidad);
			prestacionServicio.agregarIncidente(incidente);
			comunidad.agregarIncidenteReportados(incidente);

			repositorio.agregar(incidente);

			/*if(comunidad != null) {
				//notificar a los miembros de la comunidad
				for (Miembro unMiembro : comunidad.getUnosMiembros()) {
					Notificacion notif = new Notificacion();
					notif.setMiembroReceptor(unMiembro);
					notif.setIncidenteReportado(incidente);
					String textoNoti = "";
					textoNoti = "Reporte de incidente en "
							+ prestacionServicio.getUnEstablecimientoEnParticular().nombreMostrado()
							+ " de " + incidente.getEntidad().getNombre() + ": " + prestacionServicio.getUnServicio().getNombre();
					notif.setTexto(textoNoti);

					notif.notificarAlMiembro();
					NotificacionesController.getRepositorio().agregar(notif); //persistir cada notificación
				}
			}*/

			//para cada miembro, ver si hay que notificarlo
			MiembrosController.getRepositorio().buscarTodos().forEach(
					miemb -> {
						if((miemb.getEntidadesDeInteres().contains(entidad)
							|| miemb.getComunidadesALasQuePertenece().contains(comunidad))
							|| (miemb.getUsuarioAsociado().tieneRol(TipoRol.ORGANISMO_DE_CONTROL) //si es organismo de control y monitorea esta entidad..
								&& OrganismoDeControlController.buscarPorUsuario(miemb.getUsuarioAsociado()).getEntidadesMonitoreadas().contains(entidad))) {
							Notificacion notif = new Notificacion();
							notif.setMiembroReceptor(miemb);
							notif.setIncidenteReportado(incidente);
							String textoNoti = "";
							textoNoti = "Reporte de incidente en "
									+ prestacionServicio.getUnEstablecimientoEnParticular().nombreMostrado()
									+ " de " + incidente.getEntidad().getNombre() + ": " + prestacionServicio.getUnServicio().getNombre();
							notif.setTexto(textoNoti);

							notif.notificarAlMiembro();
							NotificacionesController.getRepositorio().agregar(notif); //persistir cada notificación
						}
					}
			);

			response.redirect("/prestaciones/ver?id=" + datosDeIncidente.prestacionId);
		}

		//TO-DO: hay cosas que no muestra
		Map<String, Object> model = new HashMap<>();
		model.put("error_descripcion", validacion.getDescripcion());
		return new ModelAndView(model, "incidentes/crear.hbs");
	}

	public static ModelAndView getVerIncidente(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		//buscar el incidente a consultar
		Incidente incidente = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		Map<String, Object> model = new HashMap<>();
		model.put("incidente", incidente);
		if(puedeCerrarElIncidente(miembro, incidente))
			model.put("puedeCerrarse", true);

		return new ModelAndView(model, "incidentes/mostrar_incidente.hbs");
	}

	public static Response postCerrarIncidente(Request request, Response response) {
		Incidente incidente = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		incidente.setFechaCierre(LocalDateTime.now());
		repositorio.modificar(incidente);

		Comunidad comunidad = incidente.getComunidad();
		/*if(comunidad != null) {
			//notificar a los miembros de la comunidad
			for (Miembro unMiembro : comunidad.getUnosMiembros()) {
				Notificacion notif = new Notificacion();
				notif.setMiembroReceptor(unMiembro);
				notif.setIncidenteReportado(incidente);
				String textoNoti = "";
				textoNoti = "CIERRE de incidente - "
						+ incidente.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular().nombreMostrado()
						+ " de " + incidente.getEntidad().getNombre() + ": "
						+ incidente.getUnaPrestacionDeServicio().getUnServicio().getNombre();
				notif.setTexto(textoNoti);

				notif.notificarAlMiembro();
				NotificacionesController.getRepositorio().agregar(notif); //persistir cada notificación
			}
		}*/
		//para cada miembro, ver si hay que notificarlo
		MiembrosController.getRepositorio().buscarTodos().forEach(
				miemb -> {
					if((miemb.getEntidadesDeInteres().contains(incidente.getEntidad())
							|| miemb.getComunidadesALasQuePertenece().contains(comunidad))
							|| (miemb.getUsuarioAsociado().tieneRol(TipoRol.ORGANISMO_DE_CONTROL) //si es organismo de control y monitorea esta entidad..
								&& OrganismoDeControlController.buscarPorUsuario(miemb.getUsuarioAsociado()).getEntidadesMonitoreadas().contains(incidente.getEntidad()))) {
						Notificacion notif = new Notificacion();
						notif.setMiembroReceptor(miemb);
						notif.setIncidenteReportado(incidente);
						String textoNoti = "";
						textoNoti = "CIERRE de incidente - "
								+ incidente.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular().nombreMostrado()
								+ " de " + incidente.getEntidad().getNombre() + ": "
								+ incidente.getUnaPrestacionDeServicio().getUnServicio().getNombre();
						notif.setTexto(textoNoti);

						notif.notificarAlMiembro();
						NotificacionesController.getRepositorio().agregar(notif); //persistir cada notificación
					}
				}
		);

		response.redirect("/incidentes/ver");
		return response;
	}

	public static Boolean puedeCerrarElIncidente(Miembro miembro, Incidente incidente) {
		if(incidente.fueCerrado())
			return Boolean.FALSE;

		if(incidente.getComunidad() != null) {
			if(!miembro.perteneceAcomunidad(incidente.getComunidad()))
				return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	public static void controlarAcceso(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(!request.pathInfo().contains("/ver")) { //cualquiera puede ver
			//si no tiene ninguno de estos, bloquear el acceso
			if(!usuario.tieneAlgunRol(TipoRol.MIEMBRO,
					TipoRol.ADMINISTRADOR_COMUNIDAD,TipoRol.ADMINISTRADOR_PLATAFORMA))
				Spark.halt(403);
		}
	}

	public static void controlarCierreDeIncidente(Request request, Response response) {
		Incidente incidente = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		if(!puedeCerrarElIncidente(MiembrosController.buscarPorUsuario(usuario), incidente))
			Spark.halt(403, "<label style=\"color: crimson;font-size: larger\";>ERROR: No se puede cerrar el incidente " +
					"debido a que ya cerró o no tiene los permisos necesarios para hacerlo.</label>");
	}


	//estos dos métodos devuelven datos de un incidente como json:

	public static String getIncidente(Request request, Response response) {
		Incidente incidente = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		if(incidente != null) {
			IncidenteDto incidenteDto = new IncidenteDto();
			incidenteDto.id = incidente.getId();
			incidenteDto.descripcion = incidente.getDescripcion();
			incidenteDto.descripcion2 = incidente.getDescripcion2();
			incidenteDto.quienAbrio = incidente.nombreDeQuienAbrio();
			incidenteDto.fechaApertura = incidente.horaDelIncidente();
			incidenteDto.fechaCierre = incidente.horaDeCierreDelIncidente();
			incidenteDto.nombreServicio = incidente.nombreDelServicio2();

			return new com.google.gson.Gson().toJson(incidenteDto);
		}

		response.status(404);
		return null;
	}

	public static String getIncidentes(Request request, Response response) {
		response.type("application/json");

		if(request.queryParams("id") != null)
			return getIncidente(request, response);

		List<IncidenteDto> incidenteDtos = new ArrayList<>();
		for(Incidente incidente : repositorio.buscarTodos()) {
			IncidenteDto incidenteDto = new IncidenteDto();
			incidenteDto.id = incidente.getId();
			incidenteDto.descripcion = incidente.getDescripcion();
			incidenteDto.descripcion2 = incidente.getDescripcion2();
			incidenteDto.quienAbrio = incidente.nombreDeQuienAbrio();
			incidenteDto.fechaApertura = incidente.horaDelIncidente();
			incidenteDto.fechaCierre = incidente.horaDeCierreDelIncidente();
			incidenteDto.nombreServicio = incidente.nombreDelServicio2();

			incidenteDtos.add(incidenteDto);
		}
		
		return new com.google.gson.Gson().toJson(incidenteDtos);
	}
}
