package ar.edu.utn.frba.dds.LocalizacionTest;

import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import ar.edu.utn.frba.dds.domain.ubicacion.LocalizacionException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LocalizacionTesteo {

  @Test
  public void mostrarMalMunicipioAbraPampa() throws IOException, LocalizacionException {
    Localizacion localizacionMunicipio = new Localizacion();
    localizacionMunicipio.agregarMunicipioAndProvinciaPorMunicipio("Abra Pampa");
    Assert.assertNotEquals("Cangrejillos", localizacionMunicipio.getMunicipio().getNombre());
  }
  @Test
  public void mostrarMunicipioCangrejillos() throws IOException, LocalizacionException {
    Localizacion localizacionMunicipio = new Localizacion();
    localizacionMunicipio.agregarMunicipioAndProvinciaPorMunicipio("Cangrejillos");
    Assert.assertEquals("Cangrejillos", localizacionMunicipio.getMunicipio().getNombre());
  }




}
