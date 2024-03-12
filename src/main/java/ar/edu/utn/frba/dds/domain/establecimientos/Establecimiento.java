package ar.edu.utn.frba.dds.domain.establecimientos;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//la teoria de single table , creara una sola tabla
@Table(name = "establecimiento")
@DiscriminatorColumn(name = "tipo")
public abstract class Establecimiento extends Persistence {
   @Column(name = "nombre")
   private String nombre;

   @OneToMany(mappedBy = "unEstablecimientoEnParticular")
   //mappedby son para las relaciones unidireccionales, donde le digo
   // que Prestaciones Conoce al Establecimiento
   // y que justamente desde la clase PrestacionServicio hay un JoinColumn(name ..etc)
   private List<PrestacionServicio> prestaciones;

   @ManyToOne
   @JoinColumn(name = "entidadAsociada_id", referencedColumnName = "id")
   private Entidad entidadAsociada;

   @Embedded
   private Ubicacion ubicacionGeografica; //esto es latitud/longitud

   public Establecimiento(){
      this.nombre = nombre;
      this.prestaciones = new ArrayList<>();
   }

   // este es el metodo abstracto para que por cada Sucursal, pregunte o diga si esta disponible o no
   public abstract Boolean estaDisponibleElServicioPrestado(PrestacionServicio prestacion);


   public void agregarPrestacionDeServicioAlEstablecimiento(PrestacionServicio unaPrestacion){
      prestaciones.add(unaPrestacion);
   }
   public Localizacion localizacionDeLaEntidad(){
      return entidadAsociada.getLocalizacion();

   }
   
   //los siguientes métodos servirán para los rankings
   public Integer duracionTotalDeIncidentes() {
      return this.prestaciones.stream().mapToInt(PrestacionServicio::duracionTotalDeIncidentes).sum();
   }

   public Integer cantidadDeIncidentesCerrados() {
      return this.prestaciones.stream().mapToInt(PrestacionServicio::cantidadDeIncidentesCerrados).sum();
   }

   public Integer cantidadDeIncidentesReportados() {
      return this.prestaciones.stream().mapToInt(PrestacionServicio::cantidadDeIncidentesReportados).sum();
   }

   public void agregarPrestacion(PrestacionServicio prestacion) {
      this.prestaciones.add(prestacion);
   }

   public Integer cantidadDeIncidentesNoResueltos() {
      return this.cantidadDeIncidentesReportados() - this.cantidadDeIncidentesCerrados();
   }

   //métodos para mostrar información de este establecimiento en vistas
   public abstract String nombreMostrado();

   public String ubicacionMostrada() {
      return String.format("lat: %2.4f - lon: %2.4f",
          this.ubicacionGeografica.getLat(), this.ubicacionGeografica.getLon());
   }
}