package ar.edu.utn.frba.dds.domain.comunidad;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "comunidad")
@Getter
@Setter
public class Comunidad extends Persistence {

  @Column
  private String nombre;
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private List<Miembro> unosMiembros;

  /*
  @Transient
  private List<Usuario> usuariosAdmin ;
   */

  //esto es por que tiene que crear "servicios" nuevos que lo hace en esta
  @OneToMany(mappedBy = "comunidadAsociada", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private List<Servicio> serviciosCreados ;

  @ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  @JoinTable(name = "incidente_comunidad", joinColumns = @JoinColumn(name = "comunidad_id"), inverseJoinColumns = @JoinColumn(name = "incidente_id"))
  private List<Incidente> incidentesReportados;


  @ManyToOne
  private Usuario creadaPor; //debería ser Miembro pero con este sacamos los nombres

  public Comunidad() {
    this.unosMiembros = new ArrayList<>();
   // this.usuariosAdmin = new ArrayList<>();
    this.serviciosCreados = new ArrayList<>();
    this.incidentesReportados = new ArrayList<>();
  }
/*
  public void agregarUsuariosAdmin(Usuario unUser){
    usuariosAdmin.add(unUser);
  }

 */
  public void agregarServicioCreados (Servicio unServicio){
    serviciosCreados.add(unServicio);
  }
  public void agregarMiembroAcomunidad(Miembro unMiembro){
    unosMiembros.add(unMiembro);

  }



  public List<Incidente> incidentesReportados(){
    return this.getIncidentesReportados();
  }
  public void agregarIncidenteReportados(Incidente unIncidente){
    incidentesReportados.add(unIncidente);
  }

  //desde la comunidad notificara a cada miembro sobre el incidente
  public void notificarIncidente(){
    incidentesReportados.forEach(unIncidente -> unIncidente.notificarIncidenteAComunidad(this));
  }

  //pasar a incidente e incidente lo que hace es aplicar la notificacion de ESTE (this) incidente
  // y distribuirlo a cada miembro de esa comuniad
  public void notificarAcadaMiembroElIncidente(){
    unosMiembros.forEach(unMiembro -> unMiembro.verificarTipoNoNotificacionYRecibir2(this));
    //this.marcarIncidenteComoNotificadoAMiembro(unIncidente);
  }
  public void marcarIncidenteComoNotificadoAMiembro(Incidente unIncidente){
    unIncidente.marcarComoNotificado();
  }
  public void agregarIncidenteACadaMiembro(Incidente unIncidente){
    unosMiembros.forEach(unMiembro -> unMiembro.agregarIncidenteDeComunidad(unIncidente));
  }

  public Boolean miembroEstaEnEstaComunidad(Miembro unMiembro){
    return unosMiembros.contains(unMiembro);
  }

  public List<Incidente> filtrarAquellosIncidentesQueNoPasoLas24HsCreadas(){
    return incidentesReportados.stream().filter(unIncidente -> !unIncidente.pasoLas24hs()).collect(Collectors.toList());
  }

  public List<Incidente> filtrarIncidentesNoNotificados() {
    return incidentesReportados.stream().filter(unIncidente ->  unIncidente.getIncidenteNoFueNotificado()).collect(Collectors.toList());
  }


  public int cantidadIncidentesActivos() {
    return this.incidentesReportados.stream().filter(inc -> inc.getFechaCierre() == null) .toList().size();
  }
}
