package ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes;

import com.google.gson.annotations.SerializedName;

public class Recomendacion {
	@SerializedName(value = "community1")
	public Comunidad comunidad1;
	@SerializedName(value = "community2")
	public Comunidad comunidad2;

	public Recomendacion(Comunidad comunidad1, Comunidad comunidad2) {
		this.comunidad1 = comunidad1;
		this.comunidad2 = comunidad2;
	}
}
