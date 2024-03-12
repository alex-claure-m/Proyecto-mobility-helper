package ar.edu.utn.frba.dds.domain.services.georef.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// conjunto de municipios conforma un departamento
@Getter
@Setter
public class ListadoMunicipios {
  public int cantidad; // cantidad de municipio
  public List<Municipio> municipios;

  public List<Centroide> centroides;


}
