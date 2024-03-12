package ar.edu.utn.frba.dds.domain;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
public class Persistence {
  @Id
  @GeneratedValue
  private Integer id;

  @Column
  private Boolean invalidado;

  public void setInvalidado(Boolean invalidado) {
    this.invalidado = invalidado;
  }
}
