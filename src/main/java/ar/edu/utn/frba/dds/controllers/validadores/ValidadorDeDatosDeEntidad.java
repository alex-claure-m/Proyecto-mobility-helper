package ar.edu.utn.frba.dds.controllers.validadores;

import ar.edu.utn.frba.dds.controllers.values.DatosDeEntidad;

public class ValidadorDeDatosDeEntidad {
	public static Validacion validar(DatosDeEntidad datosDeEntidad) {
		if(datosDeEntidad.nombre.isBlank())
			return new Validacion(1, "Debe ingresar un nombre");
		else if(datosDeEntidad.tipoDeEntidad.equals("0"))
			return new Validacion(2, "Debe seleccionar un tipo");
		else if(!datosDeEntidad.nombreMunicipio.isBlank() && datosDeEntidad.nombreProvincia.isBlank())
			return new Validacion(3, "Debe ingresar provincia y municipio");

		return new Validacion(0, "OK");
	}
}
