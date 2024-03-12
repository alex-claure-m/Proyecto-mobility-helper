package ar.edu.utn.frba.dds.domain.establecimientos;

import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("sucursal")
public class Sucursal extends Establecimiento{

  //@Column(name = "nombre")
  //private String nombre;

  public Sucursal(String nombre) {
    this.setNombre(nombre);
  }

  public Sucursal() {

  }

  @Override
  public Boolean estaDisponibleElServicioPrestado(PrestacionServicio prestacion) {
    return null;
  }

  @Override
  public String nombreMostrado() {
    return "Sucursal " + this.getNombre();
  }
}
