package ar.edu.utn.frba.dds.ServicesGeoRefTest;

import ar.edu.utn.frba.dds.domain.services.georef.entities.*;
import ar.edu.utn.frba.dds.domain.services.georef.ApiGeoRefException;
import ar.edu.utn.frba.dds.domain.services.georef.ServicioGeoref;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class GeoRefTest {
/*
  @BeforeEach
  public void setUp() {
    ServicioGeoref  service = ServicioGeoref.getInstance();
  }
*/
  @Test
  public void devuelveProvincias() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoProvincias listadoProvincias = service.listadoDeProvincias();
    for(Provincia unaProvincia : listadoProvincias.provincias){
      System.out.println(unaProvincia.getId() + ", " + unaProvincia.getNombre() +", Lat: " + unaProvincia.getCentroide().getLat() +", Long: "+  unaProvincia.getCentroide().getLon());
    }
    Assert.assertTrue(true);
    Assert.assertEquals(24, listadoProvincias.getProvincias().size());
  }
  @Test
  public void devuelveProvinciasPorId42_laPampa() throws IOException, ApiGeoRefException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoProvincias listadoProvincias = service.listadoProvinciaPorId("42");
    String nombreProvincia = "";
    for(Provincia unaProvincia : listadoProvincias.provincias){
      System.out.println(unaProvincia.getId() + " " + unaProvincia.getNombre() + ", lat: "+ unaProvincia.getCentroide().getLat() + ", long: "+ unaProvincia.getCentroide().getLon());
      nombreProvincia = unaProvincia.getNombre();
    }
    Assert.assertEquals("La Pampa", nombreProvincia);
  }
  @Test
  public void devuelveProvinciasPorId70_SanJuan() throws IOException, ApiGeoRefException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoProvincias listadoProvincias = service.listadoProvinciaPorId("70");
    String nombreProvincia = "";
    for(Provincia unaProvincia : listadoProvincias.provincias){
      System.out.println(unaProvincia.getId() + " " + unaProvincia.getNombre()+ ", lat: "+ unaProvincia.getCentroide().getLat() + ", long: "+ unaProvincia.getCentroide().getLon());
      nombreProvincia = unaProvincia.getNombre();
    }
    Assert.assertEquals("San Juan", nombreProvincia);
  }

  @Test
  public void devuelveNombrePorProvinciaElegida_id30_entreRios() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoProvincias provincia = service.obtenerProvinciaPorMunicipio(Integer.parseInt("30"));
    String nombrProv = "";
    for(Provincia unaProvincia : provincia.provincias){
      System.out.println(unaProvincia.getId() + " " + unaProvincia.getNombre() + ", lat: "+ unaProvincia.getCentroide().getLat() + ", long: "+ unaProvincia.getCentroide().getLon());
      nombrProv = unaProvincia.getNombre();
    }
    Assert.assertEquals("Entre Ríos",nombrProv);
  }

  @Test
  public void devuelveMunicipios() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoMunicipios listadoMunicipio = service.listadoDeMunicipios();
    for(Municipio unMunicipio : listadoMunicipio.municipios){
      System.out.println(unMunicipio.getId() + " - " + unMunicipio.getNombre()+ ", lat: "+ unMunicipio.getCentroide().getLat() + ", long: "+ unMunicipio.getCentroide().getLon());
    }
    Assert.assertTrue(true);
  }

  @Test
  public void devuelveCantidadMunicipiosPorNombreMunicipio_2Municipios() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoMunicipios listadoMunicipio = service.listadoDeMunicipiosPorNombre("Yavi");
    int cantMunic = 0 ;
    for(Municipio unMunicipio : listadoMunicipio.municipios){
      System.out.println(unMunicipio.getId() + " " + unMunicipio.getProvincia().getNombre() + ", lat: "+ unMunicipio.getCentroide().getLat() + ", long: "+ unMunicipio.getCentroide().getLon());
      cantMunic++;
    }
   // Assert.assertTrue(true);
    Assert.assertEquals(2, listadoMunicipio.getMunicipios().size());
  }

  @Test
  public void devuelveMalCantidadMunicipiosPorNombreMunicipio_1Municipios() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoMunicipios listadoMunicipio = service.listadoDeMunicipiosPorNombre("Pozo de Piedra");
    int cantMunic = 0 ;
    for(Municipio unMunicipio : listadoMunicipio.municipios){
      System.out.println(unMunicipio.getId() + " " + unMunicipio.getProvincia().getNombre()+ ", lat: "+ unMunicipio.getCentroide().getLat() + ", long: "+ unMunicipio.getCentroide().getLon());
      cantMunic++;
    }
    // Assert.assertTrue(true);
    Assert.assertEquals(1, listadoMunicipio.getMunicipios().size());
  }



  @Test
  public void devuelveMunicipioConProvincia() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoMunicipios listadoMunicipios = service.listadoDeMunicipioPorIdConProvincia("386273");
    List<Municipio> municipios = listadoMunicipios.getMunicipios();
    for(Municipio unMunicipio : municipios){

      System.out.println(unMunicipio.getId() + " - "
          + unMunicipio.getNombre() + " - "
          + unMunicipio.getProvincia().getId() + " - "
          + unMunicipio.getProvincia().getNombre() + " - "
          + ", lat: "+ unMunicipio.getCentroide().getLat() + ", long: "+ unMunicipio.getCentroide().getLon());

    }
    Assert.assertTrue(true);
  }


  @Test
  public void devuelveMunicipioConProvinciaMedianteParametros() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    // TODO
    // DEVUELVE PERO NO ROMPE, ASI QUE HACERLE EXCEPCIONES!
    ListadoMunicipios listadoMunicipios = service.listadoMunicipioAndProvincia("Caa Yarí","Misiones");
    List<Municipio> municipios = listadoMunicipios.getMunicipios();
    String munic ="";
    for(Municipio unMunicipio : municipios){

      System.out.println(unMunicipio.getId() + " - "
          + unMunicipio.getNombre() + " - "
          + unMunicipio.getProvincia().getId() + " - "
          + unMunicipio.getProvincia().getNombre() + " - "
          + ", lat: "+ unMunicipio.getCentroide().getLat() + " - "
          + ", long: "+ unMunicipio.getCentroide().getLon());

      munic = unMunicipio.getNombre();
    }
    //Assert.assertTrue(true);
    Assert.assertEquals("Caa Yarí",munic );
  }



  //me esta devolviendo 10 departamentos, pero por que asi esta limitado desde la API
  @Test
  public void devuelveDepartamento() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoDepartamentos listadoDepartamentos = service.listadoDeDepartamentos();
    for(Departamento unDePartamento : listadoDepartamentos.departamentos){
      System.out.println(unDePartamento.getId() + " " + unDePartamento.getNombre());
    }
    Assert.assertTrue(true);
  }

  @Test
  public void devuelveDepartamentoPorId22112_ohiggins() throws IOException {
    ServicioGeoref  service = ServicioGeoref.getInstance();
    ListadoDepartamentos listadoDepartamentos = service.listadoDeDepartamentosPorId("22112");
    List<Departamento> deparatentos = listadoDepartamentos.getDepartamentos();
    String depa = "";
    for(Departamento unDePartamento : deparatentos){
      System.out.println(
          " ID_Departamento: " + unDePartamento.getId() + " \n " +
              "Nombre_Departamento: " +unDePartamento.getNombre() + " \n " +
              "Provincia_departamento:" + unDePartamento.getProvincia().getId() + " \n " +
              "Nombre_provincia_departamento: "+ unDePartamento.getProvincia().getNombre() );
      depa = unDePartamento.getNombre();
    }
    Assert.assertEquals("O'Higgins", depa);
  }





}
