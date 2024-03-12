package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.miembro.Notificacion;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class NotificacionesController {
	@Getter
	private static Repositorio<Notificacion> repositorio = FactoryRepositorio.get(Notificacion.class);

	//getVerNotificacion - ver una notificación en particular
	public static ModelAndView getVerNotificacion(Request request, Response response) {
		Notificacion notif = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		notif.setFueLeido(Boolean.TRUE); //el usuario acaba de leer esta notificación
		repositorio.modificar(notif);

		//response.redirect("/incidentes/ver?id=" + notif.getIncidenteReportado().getId());

		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		Map<String, Object> model = new HashMap<>();
		model.put("notiId", notif.getId());
		if(notif.getIncidenteReportado() != null) {
			model.put("tituloDeNoti", String.format("Nuevo incidente en: %s", notif.getIncidenteReportado()
					.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular().nombreMostrado()));
			model.put("incidenteId", notif.getIncidenteReportado().getId());
		}
		else
			model.put("tituloDeNoti", "Sugerencia de revisión de incidente");
		model.put("fechaHoraDeNoti", notif.fechaHoraMostrada());
		model.put("textoDeNoti", notif.getTexto());
		return new ModelAndView(model, "notificaciones/notificacion.hbs");
	}

	//getVerNotificaciones - mostrar el listado de notificaciones de este miembro
	public static ModelAndView getVerNotificaciones(Request request, Response response) {
		if(request.queryParams("id") != null) {
			return getVerNotificacion(request, response);
		}

		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		Map<String, Object> model = new HashMap<>();
		model.put("usuario", usuario);
		model.put("notificaciones", miembro.getNotificacionesRecibidas());
		return new ModelAndView(model, "notificaciones/notificaciones.hbs");
	}

	public static Response removerNotificacion(Request request, Response response) {
		Notificacion noti = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		if(noti != null) {
			MiembrosController.getRepositorio().buscarTodos()
							.forEach(miemb -> miemb.getNotificacionesRecibidas().remove(noti));
			repositorio.eliminar(noti);
			response.redirect("/notificaciones/ver");
		} else {
			response.status(404);
		}

		return response;
	}

	public static void controlarAcceso(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(!usuario.tieneAlgunRol(TipoRol.MIEMBRO, TipoRol.ADMINISTRADOR_COMUNIDAD,
				TipoRol.ADMINISTRADOR_PLATAFORMA, TipoRol.ORGANISMO_DE_CONTROL))
			Spark.halt(403);
	}
}
