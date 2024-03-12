package ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Comunidad {
	public String id;
	@SerializedName(value = "name")
	public String nombre;
	@SerializedName(value = "lastTimeMerged")
	public String ultimaFusion;
	@SerializedName(value = "degreeOfConfidence")
	public Integer gradoConfianza;
	@SerializedName(value = "members")
	public List<Miembro> miembroList;
	@SerializedName(value = "interestingServices")
	public List<Servicio> servicioList;
	@SerializedName(value = "interestingEstablishments")
	public List<Establecimiento> establecimientoList;

	public Comunidad() {
		this.miembroList = new ArrayList<>();
		this.servicioList = new ArrayList<>();
		this.establecimientoList = new ArrayList<>();
	}

	public Comunidad(String id, String nombre, String ultimaFusion, Integer gradoConfianza) {
		this.id = id;
		this.nombre = nombre;
		this.ultimaFusion = ultimaFusion;
		this.gradoConfianza = gradoConfianza;
		this.miembroList = new ArrayList<>();
		this.servicioList = new ArrayList<>();
		this.establecimientoList = new ArrayList<>();
	}
}
