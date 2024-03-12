package ar.edu.utn.frba.dds.domain.miembro;


import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "miembro")
@Setter
@Getter
public class Miembro extends Persistence {

  @ManyToMany(mappedBy = "unosMiembros", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private List<Comunidad> comunidadesALasQuePertenece = new ArrayList<>();

  @Transient
  private List<Incidente> incidentesAbiertos  = new ArrayList<>();

  @Transient
  private List<Incidente> incidentesCompartidosPorComunidad;

  @Column(name = "esAdmin")
  private Boolean esAdmin;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "correo_Electronico")
  private String correoElectronico;


  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private Estado estado;

  @Column(name = "nro_whatsapp")
  private String nroWhatsapp;

  @Enumerated(EnumType.STRING)
  @Column(name = "via_Notificacion")
  private NotificadorVia viaNotificacion;

  @Column(name = "horario_envio_notificacion")
  private LocalDate horarioEnvioNotificacion;

  @OneToOne
  @JoinColumn(name="usuario_id", referencedColumnName="id")
  private Usuario usuarioAsociado;


  //TODO  refactorizar ubicacion , fuera de miembro -> por ende folder ubicacion para testeo

  @Transient
  private Ubicacion ubicacionActual;

  @Enumerated(EnumType.STRING)
  @Column(name = "forma_Notificacion")
  private FormaNotificacion formaNotifi = FormaNotificacion.CUANDO_SUCEDEN;

  @Transient
  private CalculadorDistancia calculadorDistanciaAIncidente;

  @Enumerated(EnumType.STRING)
  @Column(name = "notificar_via")
  private NotificarVia notificarVia = NotificarVia.ENVIAR_POR_EMAIL;

  @OneToMany
  @JoinColumn(name = "miembro_id", referencedColumnName = "id")
  private List<Notificacion> notificacionesRecibidas = new ArrayList<>();

  @Column(/*columnDefinition = "TIME"*/)
  private LocalTime horarioNotificacion; //agrego este que es LocalTime

  @Embedded //agrego una nueva ubicación para no modificar la anterior
  private ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion ubicacionActual2;

  /* corrijo -> de DATETIME a TIMESTAMP*/
  @Column(/*columnDefinition = "TIMESTAMP"*/)
  private LocalDateTime ubicacionUltimaModif;
  @ManyToMany
  private List<Entidad> entidadesDeInteres;

  public Miembro(String nombre, String apellido, String correoElectronico, Boolean esAdmin) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.correoElectronico = correoElectronico;
    this.esAdmin = esAdmin;
    //this.comunidadesALasQuePertenece = new ArrayList<>();
    //this.incidentesAbiertos = new ArrayList<>();
    //this.incidentesCompartidosPorComunidad = new ArrayList<>();
  }

  public Miembro() {

  }

  public void aceptarloAComunidad(Comunidad unaComunidad) {
    unaComunidad.agregarMiembroAcomunidad(this);
    comunidadesALasQuePertenece.add(unaComunidad);
  }

  public void agregarAComunidad(Comunidad unaComunidad) {
    comunidadesALasQuePertenece.add(unaComunidad);
  }

  public void abrirIncidente(Incidente unIncidente) {
    incidentesAbiertos.add(unIncidente); // necesito agregar todos aquellos incidentes que reporto
    comunidadesALasQuePertenece.forEach(unaComunidad -> unaComunidad.agregarIncidenteReportados(unIncidente));
    // necesito agregar a la lista de la comunidades aquellos incidentes que reporto
    // esto hace que cada comunidad tenga la lista de incidentes que se esta abriendo


  }

  //claro , eze me dice de que lo vuelva interfaz asi no repito logica
  public void notificarSegunForma(Comunidad unaComunidad) {
    if (formaNotifi == FormaNotificacion.CUANDO_SUCEDEN) {
      //unIncidente.notificarIncidente(comunidadesALasQuePertenece); // SINCRONICO
      unaComunidad.notificarIncidente();
      this.verificarTipoNoNotificacionYRecibir2(unaComunidad);

    } else if (formaNotifi == FormaNotificacion.SIN_APUROS) {
      this.configurarHorarioNotificacion(horarioEnvioNotificacion);
      this.generarResumenDeIncidentes(unaComunidad);
      //this.verificarTipoNoNotificacionYRecibir2(unaComunidad);
      //this.notificarIncidente(comunidadALasQuePertenece) ;
    }
  }
  /*
   * meter method privado o compilacion ifTesting
   * crear metodo donde inyecto metodo de creacion
   * etiqueca precompilacion
   *
   * */
// mostrar informe cada vez que envie el incidente por mail y wpp

  /*
    public void verificarTipoNoNotificacionYRecibir(Incidente unIncidente,Comunidad unaComunidad){
      if(viaNotificacion == NotificadorVia.EMAIL){
        //this.recibirNotificacion(this.getCorreoElectronico(),unIncidente.getCabezera(),unIncidente.getDescripcion());
        this.filtrarAquellosIncidentesNoNotificados(unaComunidad);
        //unIncidente.marcarComoNotificado();
      }else{
        //this.recibirNotificacion(this.getNroWhatsapp(),unIncidente.getCabezera(),unIncidente.getDescripcion());
      }
    }
  */
  public List<Incidente> verificarTipoNoNotificacionYRecibir2(Comunidad unaComunidad) {
    if (viaNotificacion == NotificadorVia.EMAIL) {
      //this.recibirNotificacion(this.getCorreoElectronico(),unIncidente.getCabezera(),unIncidente.getDescripcion());
      return this.filtrarAquellosIncidentesNoNotificados(unaComunidad);
      //unIncidente.marcarComoNotificado();
    } else {
      //this.recibirNotificacion(this.getNroWhatsapp(),unIncidente.getCabezera(),unIncidente.getDescripcion());
      return new ArrayList<>();
    }
  }


  public void recibirNotificacion(String destinatario, String asunto, String texto) {
    // aca deberia Notificar notificador ?? --para que le haga el notificar(String destinatario,incidente.cabezera, incidente.texto)
    return;
  }

  public List<Incidente> filtrarAquellosIncidentesNoNotificados(Comunidad comunidadAsociada) {
    if (this.perteneceAcomunidad(comunidadAsociada)) {
      return comunidadAsociada.filtrarIncidentesNoNotificados();
    }
    return new ArrayList<>(); // xq no hay incidentes no notificados
  }
  // arreglar esto por favooor!

  public List<Incidente> filtrarAquellosInicidentesDentroDeLas24hsCreadas(Comunidad comunidadAsociada) {
    return this.filtrarAquellosIncidentesNoNotificados(comunidadAsociada).stream().filter(unIncidente -> unIncidente.pasoLas24hs()).collect(Collectors.toList());
  }

  public List<Incidente> generarResumenDeIncidentes(Comunidad unaComunidad) {
    return this.filtrarAquellosInicidentesDentroDeLas24hsCreadas(unaComunidad);
  }

  public void cerrarIncidente(Incidente unIncidente) {
    unIncidente.cambiarEstadoServicioPrestado(Boolean.FALSE);
    // como enviar las notificaciones a todos los miembros?
    // si el miembro perteneciente a una comunidad a la cual esta el incidente reportado es el que da de baja
    // entonces que la comunidad envie notificacion a todos los miembros de esa comunidad

  }

  public void notificarIncidentesPendientes(Comunidad comunidadPerteneciente) {
    List<Incidente> incidentesNoNotificados = comunidadPerteneciente.filtrarIncidentesNoNotificados();
  }

  public void configurarHorarioNotificacion(LocalDate horario) {
    this.horarioEnvioNotificacion = horario;
  }

  ;

  public void cambiarFormaDeNotificacion(FormaNotificacion unaForma) {
  }

  ;

  public void configurarMedioNotificacion(NotificadorVia unMedio) {
  }

  ;


  //esto es para que mediante otra funcion de "saltar notificacion manual" pueda cambiar el estado
  public Boolean verificadorDistanciaAlIncidente(double unaDistancia, Incidente unIncidente) {
    return calculadorDistanciaAIncidente.estaProximaA(unaDistancia, this.getUbicacionActual(), unIncidente);
  }

  //revisar esta modalidad
  public void revisionIncidentesManualAunaDistanciaYcambiarEstado(double unaDistancia, Incidente unIncidente, Boolean unValor) {
    if (verificadorDistanciaAlIncidente(unaDistancia, unIncidente)) {
      unIncidente.setIncidenteCerrado(unValor);
    }
  }

  // para que me devuelva el estado, si esta activo = false, si esta cerrado = true!
  public Boolean chequearEstadoIncidente(Incidente unIncidente) {
    return unIncidente.getIncidenteCerrado();
  }

  public void ingresarAcomunidad(Comunidad unaComunidad) {
    unaComunidad.agregarMiembroAcomunidad(this);
  }

  public Boolean perteneceAcomunidad(Comunidad unaComunidad) {
    return unaComunidad.miembroEstaEnEstaComunidad(this);
  }

  public void agregarIncidenteDeComunidad(Incidente unIncidente) {
    //esta funcion lo que hara es traerme los incidentes que esta en la lista incidentesReportados
    // de la comunidad , a la cual miembro deberia poder leerla
    incidentesCompartidosPorComunidad.add(unIncidente);
  }


  public Integer cantNotificacionesSinLeer() {
    return (int) this.notificacionesRecibidas.stream().filter(noti -> !noti.getFueLeido()).count();
  }
}