package ar.edu.utn.frba.dds.controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

	//getLogin - mostramos la pantalla de login/registro 'por primera vez'.
	public static ModelAndView getLogin(Request request, Response response) {
		if(existeSesion(request))
			response.redirect("/inicio"); //redireccionar si ya existía sesión

		return new ModelAndView(null, "login.hbs");
	}

	//postLogin - recibimos como string el usuario y contraseña, si son correctos abrimos
	//la sesión; si no lo son, le devolvemos la pantalla de inicio con el error de logueo.
	public static ModelAndView postLogin(Request request, Response response) {
		String usuario = request.queryParams("usuario");
		String password = request.queryParams("password");
		//int rolId = Integer.parseInt(request.queryParams("rol")); // necesito el id del rol para verificar

		if(UsuariosController.autenticar(usuario, password)) {
			crearSesion(request);
			response.redirect("/inicio");
		}

		Map<String, Object> model = new HashMap<>();
		model.put("login_error", true);

		return new ModelAndView(model, "login.hbs");
	}

	//postLogoff - desloguearse (cerrar la sesión), borrar la cookie de sesión y redirigir a login.
	public static Response postLogoff(Request request, Response response) {
		terminarSesion(request);

		response.redirect("/login");
		return response;
	}

	public static void crearSesion(Request request) {
		Integer idUsuario = UsuariosController.idDeNombre(request.queryParams("usuario"));

		request.session().attribute("id", String.valueOf(idUsuario));
		//request.session().attribute("usuario", usuario); // Almacena el nombre del usuario
	}

	public static void terminarSesion(Request request) {
		request.session().removeAttribute("id");
	}

	public static Boolean existeSesion(Request request) {
		return sesionId(request) != null;
	}

	public static void forzarLogueo(Request request, Response response) {
		/*if(existeSesion(request) == Boolean.FALSE) {
			response.redirect("/login");
			Spark.halt();
		}*/
	}

	public static Integer sesionId(Request request) {
		if(request.session().attribute("id") != null)
			return Integer.parseInt(request.session().attribute("id"));

		return null;
	}
}
