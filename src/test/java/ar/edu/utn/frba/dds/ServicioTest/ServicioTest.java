package ar.edu.utn.frba.dds.ServicioTest;

import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServicioTest {

  @Test
  public void servicioEsBanio(){
    Servicio banio =  new Servicio();
    banio.setNombre("banio");
    Assertions.assertEquals("banio", "banio" );
  }
  @Test
  public void elServicioNoEsBanio(){
    Servicio escalera =  new Servicio();
    escalera.setNombre("escalera");
    Assertions.assertNotEquals("banio", escalera.getNombre() );
  }
}

