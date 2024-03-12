package ar.edu.utn.frba.dds.domain.ubicacion;


import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.services.georef.ServicioGeoref;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Centroide;
import ar.edu.utn.frba.dds.domain.services.georef.entities.ListadoMunicipios;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Municipio;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Provincia;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.IOException;

@Entity
@Table(name = "localizacion")
@Getter
@Setter
public class Localizacion  extends Persistence {


  @Embedded
  private Municipio municipio;
  @Embedded
  private Provincia provincia;


  public Municipio getMunicipio() {
    return municipio;
  }

  public String getNombreMunicipio() {
    return municipio.getNombre();
  }
  public int getIdMunicipio(){
    return municipio.getId();
  }

  public Centroide getCoordenadas(){
    return this.getMunicipio().getCentroide();
  }


  public void agregarMunicipioAndProvinciaPorMunicipio(String muni) throws IOException, LocalizacionException {
    try{
      ListadoMunicipios municipioPorNombre = this.buscarMunicipioPorNombre(muni);
      municipio = municipioPorNombre.getMunicipios().get(0);
      provincia = municipio.getProvincia();
      /*
      if(municipioDeseado.getNombre() == muni){
        this.setMunicipio(municipioDeseado);
        this.setProvincia(provinciaDeseada);
      }
      */
    }catch(IOException e){
      throw new LocalizacionException("Municipalidad Erronea");
    }
  }


  public ListadoMunicipios buscarMunicipioPorNombre(String nombreMunicipio) throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.getInstance();
    return servicioGeoref.listadoDeMunicipiosPorNombre(nombreMunicipio);
  }



}
