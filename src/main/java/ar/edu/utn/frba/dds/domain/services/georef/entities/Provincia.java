package ar.edu.utn.frba.dds.domain.services.georef.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Provincia {
  @Column
  private int idProvincia;
  @Column
  private String nombreProvincia;

  @Transient
  private Centroide centroideProvincia;

  public int getId() {
    return idProvincia;
  }

  public String getNombre() {
    return nombreProvincia;
  }

  public Centroide getCentroide() {
    return centroideProvincia;
  }

  public void setNombre(String nombre) {
    nombreProvincia = nombre;
  }

  public void setCentroide(Centroide centroide) {
    centroideProvincia = centroide;
  }
}
