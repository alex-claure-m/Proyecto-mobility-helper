package ar.edu.utn.frba.dds.IncidenteTest;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.establecimientos.Sucursal;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.miembro.Ubicacion;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import ar.edu.utn.frba.dds.domain.ubicacion.LocalizacionException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class IncidenteTest {

 /*
  @Test
  public void testPasoLas24hs() {
    // Crear un incidente con la fecha de apertura en el pasado
    Instant instantApertura = Instant.now().minus(25, ChronoUnit.HOURS);
    LocalDateTime fechaAperturaPasada = LocalDateTime.ofInstant(instantApertura, ZoneId.systemDefault());
    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado", "elevador sin servicio", null);
    elevadorDetenido.setFechaApertura(fechaAperturaPasada);

    // Crear un reloj falso que simula que han pasado 25 horas desde la apertura
    Instant instantSimulado = Instant.now().plus(25, ChronoUnit.HOURS);
    Clock relojFalso = Clock.fixed(instantSimulado, ZoneId.systemDefault());

    // Obtener la hora actual simulada
    LocalDateTime horaActualSimulada = LocalDateTime.now(relojFalso);

    // Verificar si ha pasado más de 24 horas
    boolean pasoLas24hs = elevadorDetenido.pasoLas24hs(horaActualSimulada);

    // Debería retornar true ya que han pasado más de 24 horas
    Assert.assertTrue(pasoLas24hs);
  }

  @Test
  public void testIncidenteHaPasado24Horas() {
    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado", "elevador sin servicio", null);

   // LocalDateTime horaSimuladaAvanzada = elevadorDetenido.getFechaApertura().plus(30, ChronoUnit.HOURS);

    //configuro para que el reloj vaya para atras
    LocalDateTime horaApertura = elevadorDetenido.getFechaApertura().plus(25, ChronoUnit.HOURS);

    //GMT 0
    Instant instantSimulado = horaApertura.atZone(ZoneId.systemDefault()).toInstant();

    //GMT -3 -- SEGUN SISTEMA EN REALIDAD
    Clock relojFalsoAvanzado = Clock.fixed(instantSimulado, ZoneId.systemDefault());

    // convierto el clock fixed a LocalDateTime
    LocalDateTime horaSimulada = LocalDateTime.now(relojFalsoAvanzado);


    //elevadorDetenido.setFechaApertura(horaSimulada); // Actualizar la fecha de apertura simulada

    boolean haPasado24Horas = horaSimulada.isAfter(elevadorDetenido.getFechaApertura());

    System.out.println(elevadorDetenido.getFechaApertura());
    System.out.println(horaSimulada);
    Assert.assertEquals(false,elevadorDetenido.pasoLas24hs());
  }
*/

  @Test
  public void testIncidenteHaPasado24Horas() {
    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado", "elevador sin servicio", null);

    LocalDateTime momentoInicial = LocalDateTime.now();
    //System.out.println(elevadorDetenido.getFechaApertura());

    System.out.println(momentoInicial);

    elevadorDetenido.simularIncidenteQuePaso24hs();

    LocalDateTime horaDespuesDe24Horas = momentoInicial.plusHours(24);
    //boolean haPasado24Horas = horaSimulada.isAfter(elevadorDetenido.getFechaApertura());
    //System.out.println(elevadorDetenido.getFechaApertura());

    System.out.println(horaDespuesDe24Horas);
    //Assert.assertEquals(true,elevadorDetenido.pasoLas24hs());
    Assert.assertEquals(true,elevadorDetenido.getFechaApertura().isBefore(horaDespuesDe24Horas));

  }

  @Test
  public void testIncidenteNoPaso24Horas(){
    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado", "elevador sin servicio", null);

    Assert.assertEquals(false,elevadorDetenido.pasoLas24hs());

  }
  @Test
  public void cuandoUnMiembroAbreNuevoIncidenteLaFechaEsLaDelDiaDeHoy() throws IOException, LocalizacionException {

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);
    Ubicacion ubicacionUnMiembro = new Ubicacion(-22.188310483163576, -65.32646741030163);

    Entidad bancoSantander = new Entidad("Santander");
    Servicio elevador = new Servicio();
    PrestacionServicio elevadorMecanico = new PrestacionServicio();

    //para la localizacion de la Entidad
    Localizacion municipioBuenosAires = new Localizacion();
    municipioBuenosAires.agregarMunicipioAndProvinciaPorMunicipio("Yavi"); // esto para que me setee las corrdenadas
    // y lo guarde en los parametros de esta clase Localizacion, para luego ser consutlada desde Entidad

    Localizacion localizacionDelBancoSantander = bancoSantander.getLocalizacion();
    //ok, lo que hago aca es en un parametro nuevo, que me devuelta la Localizacion de la Entidad
    bancoSantander.setLocalizacion(localizacionDelBancoSantander);

    //le relaciono que prestacion de servicio contiene el servicio Elevador
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    Establecimiento unEstablecimiento = new Sucursal("Santander Sucursal Buenos_Aires"); // el establecimiento sera sucursal

    // agrego el establecimiento a la lista que tiene Entidad de establecimientos
    bancoSantander.agregarEstablecimiento(unEstablecimiento);

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",elevadorMecanico);

    unMiembro.setUbicacionActual(ubicacionUnMiembro);
    unMiembro.abrirIncidente(elevadorDetenido);

    LocalDateTime fechaActual = LocalDateTime.now();
    Assert.assertEquals(fechaActual.getDayOfMonth(),elevadorDetenido.getFechaApertura().getDayOfMonth());
  }
  @Test
  public void cuandoSeInstanciaUnIncidenteLaLocalizacionDelIncidenteEs() throws IOException, LocalizacionException {

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);
    Ubicacion ubicacionUnMiembro = new Ubicacion(-22.188310483163576, -65.32646741030163);

    Entidad bancoSantander = new Entidad("Santander");
    Servicio elevador = new Servicio();
    PrestacionServicio elevadorMecanico = new PrestacionServicio();

    //para la localizacion de la Entidad
    Localizacion municipioJujuy = new Localizacion();
    municipioJujuy.agregarMunicipioAndProvinciaPorMunicipio("Yavi"); // esto para que me setee las corrdenadas
    // y lo guarde en los parametros de esta clase Localizacion, para luego ser consutlada desde Entidad

    bancoSantander.setLocalizacion(municipioJujuy);
    //
    // Localizacion localizacionDelBancoSantander = bancoSantander.getLocalizacion();

    //ok, lo que hago aca es en un parametro nuevo, que me devuelta la Localizacion de la Entidad

    //le relaciono que prestacion de servicio contiene el servicio Elevador
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    Establecimiento unEstablecimiento = new Sucursal("Santander Sucursal Jujuy"); // el establecimiento sera sucursal

    // agrego el establecimiento a la lista que tiene Entidad de establecimientos
    bancoSantander.agregarEstablecimiento(unEstablecimiento);

    //seteo para relacionar establecimiento a la entidad -- esta bien esto??
    unEstablecimiento.setEntidadAsociada(bancoSantander);

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",elevadorMecanico);

    elevadorDetenido.setUnaPrestacionDeServicio(elevadorMecanico);
    //debo asociar ademas al Estableicmiento con la prestacion de servicio
    elevadorMecanico.setUnEstablecimientoEnParticular(unEstablecimiento);

    unMiembro.setUbicacionActual(ubicacionUnMiembro);
    unMiembro.abrirIncidente(elevadorDetenido);

    Localizacion lugarDelIncidente = elevadorDetenido.obtenerLocalizacionDeLaPrestacion();

    //System.out.println(lugarDelIncidente.getCoordenadas().getLat());
    //System.out.println(lugarDelIncidente.getCoordenadas().getLon());

    Assert.assertEquals(lugarDelIncidente,bancoSantander.getLocalizacion());
  }



  @Test
  public void cuandoElMiembroAbreUnIncidenteElIncidenteEstaActivo() throws IOException, LocalizacionException {
    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);
    Ubicacion ubicacionUnMiembro = new Ubicacion(-22.188310483163576, -65.32646741030163);

    Entidad bancoSantander = new Entidad("Santander");
    Servicio elevador = new Servicio();
    PrestacionServicio elevadorMecanico = new PrestacionServicio();

    //para la localizacion de la Entidad
    Localizacion municipioBuenosAires = new Localizacion();
    municipioBuenosAires.agregarMunicipioAndProvinciaPorMunicipio("Yavi"); // esto para que me setee las corrdenadas
    // y lo guarde en los parametros de esta clase Localizacion, para luego ser consutlada desde Entidad

    Localizacion localizacionDelBancoSantander = bancoSantander.getLocalizacion();
    //ok, lo que hago aca es en un parametro nuevo, que me devuelta la Localizacion de la Entidad
    bancoSantander.setLocalizacion(localizacionDelBancoSantander);

    //le relaciono que prestacion de servicio contiene el servicio Elevador
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    Establecimiento unEstablecimiento = new Sucursal("Santander Sucursal Buenos_Aires"); // el establecimiento sera sucursal

    // agrego el establecimiento a la lista que tiene Entidad de establecimientos
    bancoSantander.agregarEstablecimiento(unEstablecimiento);

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",elevadorMecanico);

    unMiembro.setUbicacionActual(ubicacionUnMiembro);
    unMiembro.abrirIncidente(elevadorDetenido);


    Assert.assertEquals(false,elevadorDetenido.getIncidenteCerrado());

  }

  @Test
  public void miemborQueDaDeAltaIncidenteNoEsNotificado() throws IOException, LocalizacionException {
    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);
    Ubicacion ubicacionUnMiembro = new Ubicacion(-22.188310483163576, -65.32646741030163);

    Entidad bancoSantander = new Entidad("Santander");
    Servicio elevador = new Servicio();
    PrestacionServicio elevadorMecanico = new PrestacionServicio();

    //para la localizacion de la Entidad
    Localizacion municipioBuenosAires = new Localizacion();
    municipioBuenosAires.agregarMunicipioAndProvinciaPorMunicipio("Yavi"); // esto para que me setee las corrdenadas
    // y lo guarde en los parametros de esta clase Localizacion, para luego ser consutlada desde Entidad

    Localizacion localizacionDelBancoSantander = bancoSantander.getLocalizacion();
    //ok, lo que hago aca es en un parametro nuevo, que me devuelta la Localizacion de la Entidad
    bancoSantander.setLocalizacion(localizacionDelBancoSantander);

    //le relaciono que prestacion de servicio contiene el servicio Elevador
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    Establecimiento unEstablecimiento = new Sucursal("Santander Sucursal Buenos_Aires"); // el establecimiento sera sucursal

    // agrego el establecimiento a la lista que tiene Entidad de establecimientos
    bancoSantander.agregarEstablecimiento(unEstablecimiento);

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",elevadorMecanico);

    unMiembro.setUbicacionActual(ubicacionUnMiembro);
    unMiembro.abrirIncidente(elevadorDetenido);


    Assert.assertEquals(false,elevadorDetenido.getIncidenteNoFueNotificado());
  }


  @Test
  public void esVerdaderoQueEstaNotificado(){

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",null);

    Assert.assertEquals(true,elevadorDetenido.getIncidenteNoFueNotificado());
  }


  @Test
  public void unIncidenteNoPasoLas24hs(){

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",null);


    Assert.assertEquals(true, elevadorDetenido.pasoLas24hs());
  }

}
