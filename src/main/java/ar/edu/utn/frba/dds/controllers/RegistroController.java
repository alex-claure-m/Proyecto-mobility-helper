package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.validadores.ValidadorDeDatosDeRegistro;
import ar.edu.utn.frba.dds.controllers.values.DatosDeRegistro;
import ar.edu.utn.frba.dds.domain.usuarios.Rol;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static ar.edu.utn.frba.dds.controllers.validadores.ValidadorDeDatosDeRegistro.*;

public class RegistroController {
	private static final Logger logger = Logger.getLogger(RegistroController.class.getName());

	public static ModelAndView getRegistro(Request request, Response response) {
		//redireccionar si ya estaba logueado
		if(LoginController.existeSesion(request))
			response.redirect("/inicio");

		return new ModelAndView(null, "registro.hbs");
	}

	//postRegistro - recibe los datos ingresados en el cuadro para registrarse, hace
	//los chequeos y si los datos están OK, agregamos un nuevo usuario al repositorio.
	public static ModelAndView postRegistro(Request request, Response response) {
		DatosDeRegistro datosDeRegistro = new DatosDeRegistro();
		datosDeRegistro.nombre = request.queryParams("nombre");
		datosDeRegistro.apellido = request.queryParams("apellido");
		datosDeRegistro.usuario = request.queryParams("usuario");
		datosDeRegistro.password = request.queryParams("password");
		datosDeRegistro.passwordRepeat = request.queryParams("passwordRepeat");
		datosDeRegistro.email = request.queryParams("email");

		logger.info("Valor de nombre: " + datosDeRegistro.nombre);
		logger.info("Valor de apellido: " + datosDeRegistro.apellido);
		logger.info("Valor de usuario: " + datosDeRegistro.usuario);
		logger.info("Valor de password: " + datosDeRegistro.password);
		logger.info("Valor de passwordRepeat: " + datosDeRegistro.passwordRepeat);
		logger.info("Valor de email: " + datosDeRegistro.email);


		Map<String, Object> model = new HashMap<>();
		Validacion validacion = ValidadorDeDatosDeRegistro.validar(datosDeRegistro);
		if(validacion.getCodigo() == 0) {
			UsuariosController.crearUsuarioMiembro(datosDeRegistro);

			LoginController.crearSesion(request);
			response.redirect("/inicio");
		}

		model.put("registro_error", validacion.getDescripcion());
		return new ModelAndView(model, "registro.hbs");
	}
}
