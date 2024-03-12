package ar.edu.utn.frba.dds.domain.servicios;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "Prestacion_servicio")
@Getter
@Setter
public class PrestacionServicio extends Persistence {

  @ManyToOne //
  @JoinColumn(name = "servicio_id", referencedColumnName = "id")
  private Servicio unServicio;

  @Column(name = "disponible")
  private Boolean disponible;

  @OneToMany(mappedBy = "unaPrestacionDeServicio")
  //esto era para rankings .. pero que no lo usa al final
  private List<Incidente> incidentesReportados = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name= "establecimiento", referencedColumnName = "id")
  // el que va a apuntar siempre esta del lado de manytoone, SIEMPR
  // la one to one puede ser bidireccional, todas las demas son unidireccionales
  private Establecimiento unEstablecimientoEnParticular;


  public PrestacionServicio() {

  }

  public void cambiarEstadoDisponibilidadServicio(Boolean unEstado){
    disponible = unEstado;
  }

  public Localizacion localizacionDelEstablecimientoDeLaPrestacion(){
    return unEstablecimientoEnParticular.localizacionDeLaEntidad();
  }

  public Boolean elServicioEstaInterrumpidoPorIncidente(Incidente unIncidenteReportado){
    return this.getDisponible();
  }
  public void agregarIncidenteALaListaDeIncidentesReportados(Incidente unIncidente){
    incidentesReportados.add(unIncidente);
  }

  public void asociarPrestacionAservicio(Servicio unServicio){
    this.unServicio = unServicio;
  }


  //los siguientes métodos servirán para los rankings
  public Integer duracionTotalDeIncidentes() {
    return this.incidentesReportados.stream().
            filter(Incidente::fueCerrado).
            mapToInt(Incidente::duracionDelIncidente).sum();
  }

  public Integer cantidadDeIncidentesCerrados() {
    return (int) this.incidentesReportados.stream().filter(Incidente::fueCerrado).count();
  }

  public Integer cantidadDeIncidentesReportados() {
    //return cantidadDeIncidentesReportados(this.incidentesReportados);
    return this.incidentesReportados.size();
  }

  private static Integer cantidadDeIncidentesReportados(List<Incidente> incidentes) {
    if(incidentes.size() < 2)
      return incidentes.size();

    Incidente unIncidente = incidentes.get(0); //el primero de la lista
    List<Incidente> otrosIncidentes = incidentes.subList(1, incidentes.size()); //los demás

    for (Incidente otroIncidente : otrosIncidentes) {
      if(Incidente.cuentanComoDos(unIncidente, otroIncidente)) {
        int otroIncidenteId = otrosIncidentes.indexOf(otroIncidente);
        int otrosIncidentesSize = otrosIncidentes.size();

        otrosIncidentes = otrosIncidentes.subList(otroIncidenteId, otrosIncidentesSize);

        return cantidadDeIncidentesReportados(otrosIncidentes) + 1;
      }

    }

    return 1; //contar al menos uno si no hay otros que hagan contar como dos
  }

  public void agregarIncidente(Incidente incidente) {
    this.incidentesReportados.add(incidente);
  }


  public String nombreDelServicio() {
    return this.getUnServicio().getNombre();
  }
  public String nombreDelEstablecimiento() {
    return this.getUnEstablecimientoEnParticular().getNombre();
  }
  public Boolean estaDisponible() {
    return this.incidentesReportados.stream().allMatch(Incidente::fueCerrado);
  }
}