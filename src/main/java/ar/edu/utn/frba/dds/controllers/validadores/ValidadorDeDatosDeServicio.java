package ar.edu.utn.frba.dds.controllers.validadores;

import ar.edu.utn.frba.dds.controllers.values.DatosDeServicio;

public class ValidadorDeDatosDeServicio {
	public static Validacion validar(DatosDeServicio datosDeServicio) {
		if(datosDeServicio.nombre.isBlank())
			return new Validacion(1, "Debe ingresar un nombre");
		else if(Integer.parseInt(datosDeServicio.establecimientoId) < 0)
			return new Validacion(2, "Establecimiento no válido");

		return new Validacion(0, "OK");
	}
}
