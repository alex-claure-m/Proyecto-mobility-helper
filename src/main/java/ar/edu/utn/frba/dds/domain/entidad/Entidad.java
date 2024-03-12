package ar.edu.utn.frba.dds.domain.entidad;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "entidad")
@Getter
@Setter
public class Entidad extends Persistence {

  @Column(name = "nombre")
  private String nombre;

  @ManyToMany(/*mappedBy = "entidadAsociada", */cascade = CascadeType.ALL)
  private List<Establecimiento> establecimientos = new ArrayList<>();

  @OneToOne
  private Localizacion Localizacion;

  @Transient
  private TipoServicio tipo; // tiene sentido Tipo? o mejor interfaz?

  @Enumerated(EnumType.ORDINAL)
  private TipoDeEntidad tipoDeEntidad;

  @OneToMany
  @JoinColumn(name = "servicio_id", referencedColumnName = "id")
  //ok, entidad tiene que conocer a los servicios, por eso peude "acceder" con fk a servicios
  //desde SERVICIOS, no puede
  private List<Servicio> serviciosAdisposicion;

  public Entidad(String nombre) {
    this.nombre = nombre;
    this.establecimientos = new ArrayList<>();
  }

  public Entidad() {

  }

  public void agregarServicios(Servicio unServicio){
    serviciosAdisposicion.add(unServicio);
  }


  public void agregarEstablecimiento(Establecimiento unEstablecimiento) {
    establecimientos.add(unEstablecimiento);
  }

  //necesario para el 1er ranking
  public Integer tiempoPromedioCierreDeIncidentes() {
    int suma = this.establecimientos.stream().mapToInt(Establecimiento::duracionTotalDeIncidentes).sum();
    int cantidad = this.establecimientos.stream().mapToInt(Establecimiento::cantidadDeIncidentesCerrados).sum();
    if(cantidad == 0) return 0;
    return suma / cantidad;
  }

  //necesario para el 2do ranking
  public Integer cantidadDeIncidentesReportados() {
    return this.establecimientos.stream().mapToInt(Establecimiento::cantidadDeIncidentesReportados).sum();
  }

  //necesarios para el 3er ranking
  public Integer tiempoTotalDeResolucionDeIncidentes() {
    return this.establecimientos.stream().mapToInt(Establecimiento::duracionTotalDeIncidentes).sum();
  }

  public Integer cantidadDeIncidentesNoResueltos() {
    return this.establecimientos.stream().mapToInt(Establecimiento::cantidadDeIncidentesNoResueltos).sum();
  }

  //TODO: ¿este va acá o es del incidente?
  public Integer cantidadDeMiembrosAfectados() {
    return 1;
  }

  //métodos para mostrar información de esta entidad en vistas
  public String lugarDeActividad() {
    String detalleDelLugar = this.Localizacion.getProvincia().getNombre();
    if(this.Localizacion.getMunicipio() != null)
      detalleDelLugar = detalleDelLugar.concat(String.format(", %s", this.Localizacion.getMunicipio().getNombre()));

    return detalleDelLugar;
  }
}