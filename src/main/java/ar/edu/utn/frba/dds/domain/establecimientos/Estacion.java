package ar.edu.utn.frba.dds.domain.establecimientos;

import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
// ya no hace falta cerar un @table, poor que va a ser una sola tabla de la talba padre
@DiscriminatorValue("estacion")
public class Estacion extends Establecimiento{

  @Getter @Setter
  @Enumerated(EnumType.ORDINAL)
  @Column(name="tipo_de_estacion")
  private TipoDeEstacion tipoDeEstacion;

  public Boolean estaDisponibleLaPrestacion(PrestacionServicio unServicio){
    return unServicio.getDisponible();
  }

  @Override
  public Boolean estaDisponibleElServicioPrestado(PrestacionServicio prestacion) {
    return null;
  }

  @Override
  public String nombreMostrado() {
    return "Estación " + this.getNombre() + " (" + this.getTipoDeEstacion() + ")";
  }
}
