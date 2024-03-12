package ar.edu.utn.frba.dds.domain.servicios;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "servicio")
@Getter
@Setter
public class Servicio extends Persistence {

  @Column(name = "nombre")
  private String nombre;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  @JoinColumn(name = "comunidad_id", referencedColumnName = "id")
  private Comunidad comunidadAsociada;


  public Servicio() {

  }

  public Servicio(String nombre) {
    this.nombre = nombre;
  }
}
