package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.miembro.FormaNotificacion;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.miembro.NotificarVia;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.util.HashMap;
import java.util.Map;

public class InicioController {

	public static ModelAndView getInicio(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		Map<String, Object> model = new HashMap<>();
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);
		if(miembro != null) {
			model.put("miembro", miembro);
			if(usuario.tieneAlgunRol(TipoRol.MIEMBRO, TipoRol.ADMINISTRADOR_COMUNIDAD,
					TipoRol.ADMINISTRADOR_PLATAFORMA, TipoRol.ORGANISMO_DE_CONTROL)) {
				model.put("puedeVerNotificaciones", true);
				if(miembro.cantNotificacionesSinLeer() > 0)
					model.put("cantNotifSinLeer", miembro.cantNotificacionesSinLeer());

				if(!usuario.tieneRol(TipoRol.ORGANISMO_DE_CONTROL))
				model.put("puedeVerComunidad", true);
			}
		}

		if(usuario.tieneAlgunRol(TipoRol.ENTIDAD_PRESTADORA,
				TipoRol.ORGANISMO_DE_CONTROL, TipoRol.ADMINISTRADOR_PLATAFORMA))
			model.put("puedeVerInformes", true);

		if(usuario.tieneAlgunRol(TipoRol.ENTIDAD_PRESTADORA, TipoRol.ORGANISMO_DE_CONTROL)) {
			model.put("puedeVerSuUsuario", true);
			/*if(usuario.tieneRol(TipoRol.ENTIDAD_PRESTADORA)) {
				model.put("tipoUsuario", "prestadora");
				model.put("usuarioId", EntidadPrestadoraController.buscarPorUsuario(usuario).getId());
			}
			else if(usuario.tieneRol(TipoRol.ORGANISMO_DE_CONTROL)) {
				model.put("tipoUsuario", "reguladora");
				model.put("usuarioId", OrganismoDeControlController.buscarPorUsuario(usuario).getId());
			}*/
		}

		if(usuario.tieneRol(TipoRol.ADMINISTRADOR_PLATAFORMA))
			model.put("puedeVerUsuarios", true);

		model.put("usuario", usuario);
		return new ModelAndView(model, "inicio.hbs");
	}


	public static ModelAndView getPreferencias(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		Map<String, Object> model = new HashMap<>();
		agregarAtributosAlModel(model, miembro);
		return new ModelAndView(model, "preferencias.hbs");
	}

	public static ModelAndView postPreferencias(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

		String nombre = request.queryParams("nombre");
		String apellido = request.queryParams("apellido");
		String email = request.queryParams("email");
		String medioNotiId = request.queryParams("medio_noti_id");
		String nroTel = request.queryParams("nro-tel");
		String formaNotiId = request.queryParams("forma_noti_id");
		String horaNoti = request.queryParams("hora");
		String minsNoti = request.queryParams("mins");

		Map<String, Object> model = new HashMap<>();
		if(nombre.isBlank() || apellido.isBlank() || email.isBlank()) {
			model.put("error_descripcion", "Datos de perfil inválidos.");
			agregarAtributosAlModel(model, miembro);
			return new ModelAndView(model, "preferencias.hbs");
		}

		if(medioNotiId.equals("1") /*"Por Whatsapp"*/) {
			if(nroTel.isBlank()) { //falta verificar que sea sólo numeros
				model.put("error_descripcion", "Ingrese un número de teléfono.");
				agregarAtributosAlModel(model, miembro);
				return new ModelAndView(model, "preferencias.hbs");
			}
		}

		if(formaNotiId.equals("1") /*"Sin Apuros"*/) {
			if(horaNoti.isBlank() || minsNoti.isBlank()) { //falta verificar que sea sólo numeros
				model.put("error_descripcion", "El horario ingresado no es válido.");
				agregarAtributosAlModel(model, miembro);
				return new ModelAndView(model, "preferencias.hbs");
			}
		}

		miembro.setNombre(nombre);
		miembro.setApellido(apellido);
		miembro.setCorreoElectronico(email);

		//habrá alguna forma de hacer esto más directo y sin switch??
		switch (medioNotiId) {
			case "0": miembro.setNotificarVia(NotificarVia.ENVIAR_POR_EMAIL); break;
			case "1":
				miembro.setNotificarVia(NotificarVia.ENVIAR_POR_TELEFONO);
				miembro.setNroWhatsapp(nroTel);
				break;
		}
		switch (formaNotiId) {
			case "0": miembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN); break;
			case "1":
				miembro.setFormaNotifi(FormaNotificacion.SIN_APUROS);
				miembro.setHorarioNotificacion(LocalTime.of(Integer.parseInt(horaNoti), Integer.parseInt(minsNoti)));
			break;
		}

		System.out.println("Cambios guardados");
		MiembrosController.getRepositorio().modificar(miembro);

		agregarAtributosAlModel(model, miembro);
		model.put("cambiosGuardados", true); //mostrar que se guardaron los cambios
		return new ModelAndView(model, "preferencias.hbs");
	}

	public static void agregarAtributosAlModel(Map<String, Object> model, Miembro miembro) {
		if(miembro.getUsuarioAsociado().tieneAlgunRol(TipoRol.MIEMBRO,
				TipoRol.ADMINISTRADOR_COMUNIDAD, TipoRol.ADMINISTRADOR_PLATAFORMA))
			model.put("puedeVerNotificaciones", true);

		model.put("miembro", miembro);

		if(miembro.getNotificarVia() == NotificarVia.ENVIAR_POR_EMAIL)
			model.put("actual_email", true);
		else if(miembro.getNotificarVia() == NotificarVia.ENVIAR_POR_TELEFONO)
			model.put("actual_telefono", true);

		/*if(miembro.getNroWhatsapp() != null) {
			model.put("actual_nroTelefono", miembro.getNroWhatsapp());
		}*/

		if(miembro.getFormaNotifi() == FormaNotificacion.CUANDO_SUCEDEN)
			model.put("actual_cuandoSuceden", true);
		if(miembro.getFormaNotifi() == FormaNotificacion.SIN_APUROS)
			model.put("actual_sinApuros", true);

		/*LocalTime horarioNotificacion = miembro.getHorarioNotificacion();
		if(horarioNotificacion != null) {
			model.put("actual_hora", horarioNotificacion.getHour());
			model.put("actual_mins", horarioNotificacion.getMinute());
		}*/
	}
}
