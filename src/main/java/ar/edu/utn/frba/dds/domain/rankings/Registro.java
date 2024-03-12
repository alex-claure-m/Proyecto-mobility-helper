package ar.edu.utn.frba.dds.domain.rankings;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Registro<TypeObj, TypeVal> {
	private TypeObj objeto;
	private TypeVal valor;

	public Registro(TypeObj objeto, TypeVal valor) {
		this.objeto = objeto;
		this.valor = valor;
	}
}
