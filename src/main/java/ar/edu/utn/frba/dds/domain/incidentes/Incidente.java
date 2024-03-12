package ar.edu.utn.frba.dds.domain.incidentes;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;

// 27/12 -> modificacion de longText -> text debido a postgresql que no lo toma
@Entity
@Table(name = "Incidente")
@Getter
@Setter
public class Incidente extends Persistence {

  @Column(name = "descripcion", columnDefinition = "text")
  private String descripcion;

  @Column(name = "cabezera")
  private String cabezera;
  @Column(name = "fecha_Apertura")
  private LocalDateTime fechaApertura;
  @Column(name = "fecha_Cierre")
  private LocalDateTime fechaCierre;
  @Column(name = "incidente_Cerrado")
  private Boolean incidenteCerrado;

  @Column(name = "incidente_No_Fue_Notificado")
  private Boolean incidenteNoFueNotificado;

  @ManyToOne
  @JoinColumn(name = "prestacion_id", referencedColumnName = "id")
  private PrestacionServicio unaPrestacionDeServicio;

  @ManyToOne
  @JoinColumn(name = "miembro_id", referencedColumnName = "id")
  private Miembro miembro;
  @ManyToOne
  @JoinColumn(name = "entidad_id", referencedColumnName = "id")
  private Entidad entidad;
  @ManyToOne
  @JoinColumn(name = "comunidad_id", referencedColumnName = "id")
  private Comunidad comunidad;
  @Column(name="textoDescripcion", columnDefinition = "text")
  private String textoDescripcion; //una descripción más extensa


  public Incidente(String descripcion, String cabezera , PrestacionServicio unaPrestacionDeServicio) {
    this.descripcion = descripcion;
    this.cabezera = cabezera;
    this.fechaApertura = LocalDateTime.now();
    this.incidenteCerrado = false;
    this.incidenteNoFueNotificado = true;
    this.unaPrestacionDeServicio = unaPrestacionDeServicio;
  }

  public Incidente() {

  }

  public Boolean pasoLas24hs(){
    LocalDateTime horaActual = LocalDateTime.now();
    Duration duracion = Duration.between(this.getFechaApertura(),horaActual);
    //long hours = duracion.toHours();
    long days = duracion.toDays();
    return  days >=1 ; // .. * 60 to seconds
  }

  public void simularIncidenteQuePaso24hs(){

    //LocalDateTime horaActual =
    LocalDateTime horaApertura = this.getFechaApertura().plus(1, ChronoUnit.DAYS);

    //GMT 0
    //Instant instantSimulado = horaApertura.atZone(ZoneId.systemDefault()).toInstant();

    //GMT -3 -- SEGUN SISTEMA EN REALIDAD
    //Clock relojFalsoAvanzado = Clock.fixed(instantSimulado, ZoneId.systemDefault());

    // convierto el clock fixed a LocalDateTime
    //LocalDateTime horaSimulada = LocalDateTime.now(relojFalsoAvanzado);

    this.setFechaApertura(horaApertura);

  }

  public void notificarIncidenteAComunidad(Comunidad unaComunidad ){
    unaComunidad.notificarAcadaMiembroElIncidente();
  }
  // me sirve para ver si este incidente ya fue notificado para cuando tenga que enviar la forma de notificacion
  // que sera
  public void marcarComoNotificado() {
    this.incidenteNoFueNotificado = true;
  }

  /*
 // esperar respuesta de maxi
  public void enviarNotificacion(String destinatario,String asunto, String texto){
    //hacerle un seteo notificacion!
    notificacion.notificar(destinatario,asunto,texto);
  }
//
*/
  public void cambiarEstadoServicioPrestado(Boolean unValor){
    if(this.getIncidenteCerrado()){
      unaPrestacionDeServicio.setDisponible(unValor);
    }
  }

  public int obtenerSemanaCierre(){
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return this.fechaCierre.get(weekFields.weekOfYear());
  }


  public void agregarIncidenteReportadosALaPretacionDeServicio(){
    unaPrestacionDeServicio.agregarIncidenteALaListaDeIncidentesReportados(this);
  }

  public Localizacion obtenerLocalizacionDeLaPrestacion(){
    return unaPrestacionDeServicio.localizacionDelEstablecimientoDeLaPrestacion();
  }

  //los siguientes métodos servirán para los rankings
  public Boolean fueCerrado() {
    return this.fechaCierre != null;
  }

  public Integer duracionDelIncidente() { //en horas
    if(this.fueCerrado())
      return (int) fechaApertura.until(fechaCierre, ChronoUnit.HOURS) + 1; // sumar +1 como "redondeo"

    return (int) fechaApertura.until(LocalDateTime.now(), ChronoUnit.HOURS) + 1;
  }

  //pienso que con métodos de clase se ve más fácil lo que se está comparando
  public static Boolean cuentanComoDos(Incidente unIncidente, Incidente otroIncidente) {
    return horasEntreAperturas(unIncidente, otroIncidente) > 24 || otroIncidente.fueCerrado();
  }

  public static Integer horasEntreAperturas(Incidente unIncidente, Incidente otroIncidente) {
    return (int) unIncidente.fechaApertura.until(otroIncidente.fechaApertura, ChronoUnit.HOURS);
  }

  public Integer gradoDeImpactoPara(Comunidad comunidad) {
    return 0; //aún no se tiene el criterio
  }


  //métodos para mostrar un incidente
  public String nombreDelServicio() {
    return this.unaPrestacionDeServicio.getUnServicio().getNombre();
  }
  public String nombreDelServicio2() {
    return this.unaPrestacionDeServicio.getUnServicio().getNombre() + " ("
        + this.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular().nombreMostrado() + ")";
  }
  public String lugarDelIncidente() {
    return this.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular().getNombre();
  }
  public String horaDelIncidente() {
    return this.getFechaApertura().format(DateTimeFormatter.ofPattern("dd/MM/uuuu' a las 'HH:mm:ss"));
  }
  public String nombreDeQuienAbrio() {
    return this.miembro.getNombre()+ " " + this.getMiembro().getApellido();
  }
  public String descripcionDelIncidente() {
    return this.descripcion;
  }
  public String horaDeCierreDelIncidente() {
    if(fechaCierre != null)
      return fechaCierre.format(DateTimeFormatter.ofPattern("dd/MM/uuuu' a las 'HH:mm:ss"));
    return null;
  }
  public String mostrarDisponibilidad() {
    return fechaCierre != null? "CERRADO" : "ACTIVO";
  }
  public String getDescripcion2() {
    return this.textoDescripcion;
  }
  public Boolean estaProximoA(double lat1, double lon1, int maxKm) {
    double lat2 = this.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular()
        .getUbicacionGeografica().getLat();
    double lon2 = this.getUnaPrestacionDeServicio().getUnEstablecimientoEnParticular()
        .getUbicacionGeografica().getLon();
    return (new ar.edu.utn.frba.dds.domain.miembro.Ubicacion(0,0)).distance(lat1, lon1, lat2, lon2) < maxKm;
  }
}