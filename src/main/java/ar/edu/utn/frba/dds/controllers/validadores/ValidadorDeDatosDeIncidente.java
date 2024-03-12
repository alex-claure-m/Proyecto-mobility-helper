package ar.edu.utn.frba.dds.controllers.validadores;

import ar.edu.utn.frba.dds.controllers.values.DatosDeIncidente;

public class ValidadorDeDatosDeIncidente {
	public static Validacion validar(DatosDeIncidente datosDeIncidente) {
		if(datosDeIncidente.descripcion.isBlank())
			return new Validacion(1, "Debe ingresar una descripción");
		else if(Integer.parseInt(datosDeIncidente.comunidadId) < 1)
			return new Validacion(2, "Comunidad no válida");
		else if(datosDeIncidente.prestacionId.isBlank())
			return new Validacion(3, "Servicio no válido");
		else if((datosDeIncidente.entidadId != null && datosDeIncidente.entidadId.isBlank())
				|| Integer.parseInt(datosDeIncidente.entidadId) < 0)
			return new Validacion(4, "Entidad no válida");

		return new Validacion(0, "OK");
	}
}
