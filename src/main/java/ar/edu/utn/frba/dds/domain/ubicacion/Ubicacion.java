package ar.edu.utn.frba.dds.domain.ubicacion;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Ubicacion {
	@Column(name="lat")
	private Double lat;
	@Column(name="lon")
	private Double lon;

	public Ubicacion() {

	}

	public Ubicacion(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}
}
