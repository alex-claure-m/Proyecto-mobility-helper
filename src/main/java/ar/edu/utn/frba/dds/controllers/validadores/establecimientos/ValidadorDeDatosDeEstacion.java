package ar.edu.utn.frba.dds.controllers.validadores.establecimientos;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.values.establecimientos.DatosDeEstacion;

public class ValidadorDeDatosDeEstacion {
	public static Validacion validar(DatosDeEstacion datosDeEstacion) {
		return new Validacion(0, "OK");
	}
}
