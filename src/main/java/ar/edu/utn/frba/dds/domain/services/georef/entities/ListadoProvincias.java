package ar.edu.utn.frba.dds.domain.services.georef.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListadoProvincias {
  public int cantidad; //cantidad total de provincias
  public List<Provincia> provincias;

  //public List<Centroide> centroides;
}
