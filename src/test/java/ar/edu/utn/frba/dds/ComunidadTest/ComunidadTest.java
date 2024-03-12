package ar.edu.utn.frba.dds.ComunidadTest;

import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComunidadTest {

  @Test
  public void cantidadDeMiembrosAComunidadEsUno(){
    //Perfil unPerfil2 = new Perfil("rodrigo","tabare","rodrig@gmail.com");
    //Perfil unPerfil3 = new Perfil("golondrina","rotolo","roto@gmail.com");
    Miembro unMiembro = new Miembro("pepito","fuente","pepitofuente@gmail.com",true);
    Comunidad laComunidad = new Comunidad();
    laComunidad.agregarMiembroAcomunidad(unMiembro);
    Assertions.assertEquals(laComunidad.getUnosMiembros().size(),1);
  }
  @Test
  public void son3LosIncidentesReportadosQueFueronDadoDeAlta(){
    // no voy a hacer todos los pasos como anteriores test, solamente hare incidentes y ver la lista

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);

    Comunidad unaComunidad = new Comunidad();
    unMiembro.aceptarloAComunidad(unaComunidad);

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",null);
    Incidente banioObstruido = new Incidente("el baño esta inundado","baño Inundado",null);
    Incidente molineteAtascado = new Incidente("el molinete no deja pasar personas","molinete atascado",null);

    unMiembro.abrirIncidente(elevadorDetenido);
    unMiembro.abrirIncidente(banioObstruido);
    unMiembro.abrirIncidente(molineteAtascado);

    Assert.assertEquals(3, unaComunidad.getIncidentesReportados().size());

  }

  @Test
  public void filtrarIncidenteDaComoResultado1(){

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);

    Comunidad unaComunidad = new Comunidad();

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",null);
    Incidente banioObstruido = new Incidente("el baño esta inundado","baño Inundado",null);
    Incidente molineteAtascado = new Incidente("el molinete no deja pasar personas","molinete atascado",null);

    unMiembro.aceptarloAComunidad(unaComunidad);


    unMiembro.abrirIncidente(elevadorDetenido);
    //unMiembro.abrirIncidente(banioObstruido);
    //unMiembro.abrirIncidente(molineteAtascado);

    int incidentes = unaComunidad.filtrarIncidentesNoNotificados().size();

    System.out.println("incidentes "+ incidentes );

  }
  @Test
  public void son0LosIncientesQuePasaronLas24horas(){
    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);

    Comunidad unaComunidad = new Comunidad();

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",null);
    Incidente banioObstruido = new Incidente("el baño esta inundado","baño Inundado",null);
    Incidente molineteAtascado = new Incidente("el molinete no deja pasar personas","molinete atascado",null);

    unMiembro.aceptarloAComunidad(unaComunidad);

    unMiembro.abrirIncidente(elevadorDetenido);
    unMiembro.abrirIncidente(banioObstruido);
    unMiembro.abrirIncidente(molineteAtascado);

    //esto es un arrastre de incidente con la hora de filtrado de incidentes que paso las 24hs


    int cantidadIncidentesNoPasaron24hs = unaComunidad.filtrarAquellosIncidentesQueNoPasoLas24HsCreadas().size();

    System.out.println(cantidadIncidentesNoPasaron24hs);
  }
}
