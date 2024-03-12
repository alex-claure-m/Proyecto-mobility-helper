package ar.edu.utn.frba.dds.controllers.validadores.establecimientos;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.values.establecimientos.DatosDeSucursal;

public class ValidadorDeDatosDeSucursal {
	public static Validacion validar(DatosDeSucursal datosDeSucursal) {
		return new Validacion(0, "OK");
	}
}
