package ar.edu.utn.frba.dds.domain.services.georef.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListadoDepartamentos {
  public int canitdad;
  public List<Departamento> departamentos;

}
