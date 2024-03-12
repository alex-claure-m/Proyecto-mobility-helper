package ar.edu.utn.frba.dds.domain.usuarios.validador;

import ar.edu.utn.frba.dds.domain.usuarios.validador.adapters.ValidadorDePasswordAdapter;

public class ValidadorDePassword {
	private ValidadorDePasswordAdapter adapter;

	public ValidadorDePassword(ValidadorDePasswordAdapter adapter) {
		this.adapter = adapter;
	}

	public Boolean validar(String nombreDeUsuario,String password) {
		return this.adapter.validar(nombreDeUsuario, password);
	}
}
