package ar.edu.utn.frba.dds.controllers.validadores;

import lombok.Getter;

@Getter
public class Validacion {
	private final Integer codigo;
	private final String descripcion;

	public Validacion(Integer codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
}
