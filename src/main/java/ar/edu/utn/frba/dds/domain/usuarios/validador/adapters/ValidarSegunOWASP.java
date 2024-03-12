package ar.edu.utn.frba.dds.domain.usuarios.validador.adapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ValidarSegunOWASP implements ValidadorDePasswordAdapter {
	private final String path;

	public ValidarSegunOWASP(String path) {
		this.path = path;
	}

	@Override
	public Boolean validar(String nombreDeUsuario,String password) {
		if(!this.validarCredencialesPorDefecto(nombreDeUsuario, password))
			return Boolean.FALSE;

		if(!this.chequearDebilidadDePassword(password))
			return Boolean.FALSE;

		if(password.length() < 8)
			return Boolean.FALSE;

		return Boolean.TRUE;
	}

	/*public Boolean pasoElTiempoParaReintentar() {
		long segundos = 0;
		if(this.intentosFallidos > 3)
			segundos = this.intentosFallidos * 10;

		LocalDateTime horaActual = LocalDateTime.now();
		return horaActual.isAfter(horaActual.minusSeconds(segundos));
	}*/

	private Boolean validarCredencialesPorDefecto(String nombreDeUsuario,String password) {
		if(nombreDeUsuario.equals("admin") && password.equals("admin"))
			return Boolean.FALSE;

		return Boolean.TRUE;
	}

	private Boolean chequearDebilidadDePassword(String password) {
		try {
			Scanner scanner = new Scanner(new File(this.path));

			while(scanner.hasNextLine()) {
				if(scanner.nextLine().equals(password))
					return Boolean.FALSE;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return Boolean.TRUE;
	}
}
