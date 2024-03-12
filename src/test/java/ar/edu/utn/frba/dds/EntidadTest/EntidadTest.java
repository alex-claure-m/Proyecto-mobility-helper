package ar.edu.utn.frba.dds.EntidadTest;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.establecimientos.Sucursal;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import ar.edu.utn.frba.dds.domain.ubicacion.LocalizacionException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class EntidadTest {

  @Test
  public void entidadEnYaviCuyaLatitudCoordenadaEs() throws IOException, LocalizacionException {
    Entidad bancoSantander = new Entidad("Santander");
    Servicio elevador = new Servicio();
    PrestacionServicio elevadorMecanico = new PrestacionServicio();
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    Establecimiento unEstablecimiento = new Sucursal("Santander Sucursal Yavi"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    bancoSantander.agregarEstablecimiento(unEstablecimiento);


    //para la localizacion de la Entidad
    Localizacion municipioYavi = new Localizacion();
    municipioYavi.agregarMunicipioAndProvinciaPorMunicipio("Yavi"); // esto para que me setee las corrdenadas
    // y lo guarde en los parametros de esta clase Localizacion, para luego ser consutlada desde Entidad
    bancoSantander.setLocalizacion(municipioYavi);
    Localizacion localizacionDelBancoSantander = bancoSantander.getLocalizacion();

    Assert.assertEquals(-22.1949119799291, localizacionDelBancoSantander.getCoordenadas().getLat(),0);
  }

  @Test
  public void entidadEnYaviCuyaLongitudCoordenadaEs() throws IOException, LocalizacionException {
    Entidad bancoSantander = new Entidad("Santander");
    Servicio elevador = new Servicio();
    PrestacionServicio elevadorMecanico = new PrestacionServicio();
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    Establecimiento unEstablecimiento = new Sucursal("Santander Sucursal Yavi"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    bancoSantander.agregarEstablecimiento(unEstablecimiento);


    //para la localizacion de la Entidad
    Localizacion municipioYavi = new Localizacion();
    municipioYavi.agregarMunicipioAndProvinciaPorMunicipio("Yavi"); // esto para que me setee las corrdenadas
    // y lo guarde en los parametros de esta clase Localizacion, para luego ser consutlada desde Entidad
    bancoSantander.setLocalizacion(municipioYavi);
    Localizacion localizacionDelBancoSantander = bancoSantander.getLocalizacion();

    Assert.assertEquals(-65.3412864273208, localizacionDelBancoSantander.getCoordenadas().getLon(),0);
  }

  @Test
  public void laEntidadBancoSantanderTiene2Sucursales(){
    //en este caso son de jujuy
    Entidad bancoSantander = new Entidad("Santander");

    Establecimiento sucursalAguasCalientes = new Sucursal("Sucursal: Aguas Calientes");
    Establecimiento sucursalPampaBlanca = new Sucursal("Sucursal: Pampa Blanca");

    bancoSantander.agregarEstablecimiento(sucursalAguasCalientes);
    bancoSantander.agregarEstablecimiento(sucursalPampaBlanca);

    Assert.assertEquals(2,bancoSantander.getEstablecimientos().size());
  }

  @Test
  public void laEntidadHospitalNoTieneSolo1Sucursal_tiene2(){
    //en este caso seran todos de salta
    Entidad hospitalNinios = new Entidad("Hospital Dr Hector");

    Establecimiento sucursalTolarGrande = new Sucursal("Sucursal:Tolar Grande");
    Establecimiento sucursalCafayate= new Sucursal("Sucursal: Cafayate");

    hospitalNinios.agregarEstablecimiento(sucursalTolarGrande);
    hospitalNinios.agregarEstablecimiento(sucursalCafayate);

    Assert.assertNotEquals(1,hospitalNinios.getEstablecimientos().size());
  }


}
