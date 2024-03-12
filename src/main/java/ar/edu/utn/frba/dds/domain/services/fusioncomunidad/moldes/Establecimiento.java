package ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes;

import com.google.gson.annotations.SerializedName;

public class Establecimiento {
	public String id;
	@SerializedName(value = "name")
	public String nombre;

	public Establecimiento(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
}
