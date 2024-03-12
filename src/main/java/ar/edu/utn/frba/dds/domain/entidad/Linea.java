package ar.edu.utn.frba.dds.domain.entidad;

import ar.edu.utn.frba.dds.domain.establecimientos.Estacion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Linea {
  private String nombre;
  private List<Estacion> estaciones = new ArrayList<>();
  private Estacion estacionOrigen;
  private Estacion estacionDestino;


  public Linea(String nombre, Estacion estacionOrigen, Estacion estacionDestino) {
    this.nombre = nombre;
    this.estacionOrigen = estacionOrigen;
    this.estacionDestino = estacionDestino;
  }

  public void agregarEstacion(Estacion unaEstacion){
    estaciones.add(unaEstacion);
  }


}
