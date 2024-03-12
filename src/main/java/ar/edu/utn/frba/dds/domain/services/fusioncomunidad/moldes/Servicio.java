package ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes;

import com.google.gson.annotations.SerializedName;

public class Servicio {
	public String id;
	@SerializedName(value = "name")
	public String nombre;

	public Servicio(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
}
