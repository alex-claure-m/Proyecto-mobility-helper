package ar.edu.utn.frba.dds.domain.services.georef.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;

@Embeddable
public class Municipio {
  @Column
  public int idMunicipio;
  @Column
  public String nombreMunicipio;
  @Transient
  public Provincia provinciaMunicipio;

@Transient
  public Centroide centroideMunicipio;

  public int getId() {
    return idMunicipio;
  }

  public String getNombre() {
    return nombreMunicipio;
  }

  public Provincia getProvincia() {
    return provinciaMunicipio;
  }

  public Centroide getCentroide() {
    return centroideMunicipio;
  }

  public void setNombre(String nombre) {
    nombreMunicipio = nombre;
  }

  public void setProvincia(Provincia provincia) {
    provinciaMunicipio = provincia;
  }

  public void setCentroide(Centroide centroide) {
    centroideMunicipio = centroide;
  }
}
