package ar.edu.utn.frba.dds.domain.services.georef.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Departamento {
  public int id;
  public String nombre;
  public Provincia provincia;
}
