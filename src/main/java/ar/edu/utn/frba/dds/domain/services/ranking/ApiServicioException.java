package ar.edu.utn.frba.dds.domain.services.ranking;

import java.io.IOException;

public class ApiServicioException extends RuntimeException {
	public ApiServicioException(String motivo) {
		super("la Conexion con el Servicio fallo debido a: " + motivo);
	}
}
