package ar.edu.utn.frba.dds.domain.services.georef.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Centroide {

  private double lat;
  private double lon;

}
