package ar.edu.utn.frba.dds.PrestacionServiciosTest;

import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class PrestacionDeServicioTest {

  @Test
  public void elNombreDeLaPrestacionDeServicioEsBanio(){
    Servicio banio = new Servicio();
    banio.setNombre("baños");
    PrestacionServicio banioParaEmpleados = new PrestacionServicio();
    banioParaEmpleados.asociarPrestacionAservicio(banio);

    Assert.assertEquals("baños",banioParaEmpleados.getUnServicio().getNombre());
  }


}
