package ar.edu.utn.frba.dds.controllers.validadores;

import ar.edu.utn.frba.dds.controllers.values.DatosDeRegistro;
import ar.edu.utn.frba.dds.domain.usuarios.validador.ValidadorDePassword;
import ar.edu.utn.frba.dds.domain.usuarios.validador.adapters.ValidarSegunOWASP;

public class ValidadorDeDatosDeRegistro {
	public static Validacion validar(DatosDeRegistro datosDeRegistro) {
		if(!validarUsuario(datosDeRegistro.usuario))
			return new Validacion(1, "El nombre de usuario es requerido");

		if(!validarPassword(datosDeRegistro.password, datosDeRegistro.passwordRepeat))
			return new Validacion(2, "Las contraseñas no coinciden");

		//hacer la validación de contraseña según requerimientos de la primera entrega
		ValidadorDePassword validadorDePassword
						= new ValidadorDePassword(new ValidarSegunOWASP("passwordsTop10k.txt"));
		if(!validadorDePassword.validar(datosDeRegistro.usuario, datosDeRegistro.password))
			return new Validacion(3, "La contraseña es demasiado débil");

		if(!validarNombreCompleto(datosDeRegistro.nombre, datosDeRegistro.apellido))
			return new Validacion(3, "El nombre y el apellido son requeridos");

		if(!validarEmail(datosDeRegistro.email))
			return new Validacion(4, "El correo electrónico no es válido");

		return new Validacion(0, "OK");
	}

	public static Boolean validarPorNulo(String valor) {
		if(valor == null || valor.equals(""))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}

	public static Boolean validarNombreCompleto(String nombre, String apellido) {
		if(!validarPorNulo(nombre) || !validarPorNulo(apellido))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}

	public static Boolean validarUsuario(String usuario) {
		if(!validarPorNulo(usuario))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}

	public static Boolean validarPassword(String password, String passwordRepeat) {
		if(!validarPorNulo(password) || !password.equals(passwordRepeat))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}

	public static Boolean validarEmail(String email) {
		if(!validarPorNulo(email))
			return Boolean.FALSE;

		if(!email.contains("@") || !email.contains("."))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}
}
