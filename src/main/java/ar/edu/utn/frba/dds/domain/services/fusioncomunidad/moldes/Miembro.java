package ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes;

import com.google.gson.annotations.SerializedName;

public class Miembro {
	public String id;
	@SerializedName(value = "name")
	public String nombre;

	public Miembro(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
}
