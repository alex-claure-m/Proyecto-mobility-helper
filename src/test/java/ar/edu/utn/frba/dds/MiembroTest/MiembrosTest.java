package ar.edu.utn.frba.dds.MiembroTest;

import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import ar.edu.utn.frba.dds.domain.miembro.*;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.establecimientos.Sucursal;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import ar.edu.utn.frba.dds.domain.ubicacion.LocalizacionException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MiembrosTest {

  @Test
  public void pepitoEsAdministrador(){
    //Perfil unPerfil2 = new Perfil("rodrigo","tabare","rodrig@gmail.com");
    //Perfil unPerfil3 = new Perfil("golondrina","rotolo","roto@gmail.com");
    Miembro unMiembro = new Miembro("pepito","fuente","pepitofuente@gmail.com",true);
    Assertions.assertEquals(true, unMiembro.getEsAdmin());
  }

  @Test
  public void unMiembroDeLaComunidadQueNoAbrioElIncidenteSabeQueTienUnIncidenteFiltradoANotificar(){
    Miembro unMiembro = new Miembro("Filipino","Ecchioto","filiEcchito@gmail.com",false);
    Miembro unMiembro2 = new Miembro("Silvina","Perez","silvipe@gmail.com",false);
    Miembro unMiembro3 = new Miembro("Julian","Hernandez","julher@gmail.com",false);

    Comunidad unaComunidad = new Comunidad();

    unMiembro.aceptarloAComunidad(unaComunidad);
    unMiembro2.aceptarloAComunidad(unaComunidad);
    unMiembro3.aceptarloAComunidad(unaComunidad);

    Entidad hospital = new Entidad("Hospital Central");
    Establecimiento unEstablecimiento = new Sucursal("Hospital Central- Norte"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    hospital.agregarEstablecimiento(unEstablecimiento);

    Servicio escalera = new Servicio();
    PrestacionServicio elevadorMecanica = new PrestacionServicio();

    elevadorMecanica.setUnServicio(escalera); // le digo che: esta prestacion de servicio responde a un Servicio baño
    Incidente escaleraRota = new Incidente("La escalera tuvo un desperfecto Mecanico","escalera rota",elevadorMecanica);
    unMiembro.abrirIncidente(escaleraRota);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setViaNotificacion(NotificadorVia.EMAIL);
    unMiembro.notificarSegunForma(unaComunidad);

    unMiembro2.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro2.setViaNotificacion(NotificadorVia.EMAIL);

    //unaComunidad.agregarIncidenteReportados(escaleraRota); -- ya esta en abrirIncidente

    //esta bien esto.
    //solamente que yo aun no termine de que un miembro2 reciba los incidentes que no fueron abiertos por miembro1
    unaComunidad.notificarAcadaMiembroElIncidente();

    //int incidenteslala = unMiembro2.filtrarAquellosIncidentesNoNotificados(unaComunidad).size();
    int incidenteListaEnMiembro2 = unMiembro2.verificarTipoNoNotificacionYRecibir2(unaComunidad).size();

    //System.out.println("incidente:" + incidenteslala);
    Assert.assertEquals(1, incidenteListaEnMiembro2);

  }


  // ESTE TESTEO ES DE PRUEBA
  @Test
  public void unMiembroDeLaComunidadQueNoAbrioElIncidenteSabePorNoAsociarSuWhatsappNoRecibeNingunIncidente(){
    Miembro unMiembro = new Miembro("Filipino","Ecchioto","filiEcchito@gmail.com",false);
    Miembro unMiembro2 = new Miembro("Silvina","Perez","silvipe@gmail.com",false);
    Miembro unMiembro3 = new Miembro("Julian","Hernandez","julher@gmail.com",false);

    Comunidad unaComunidad = new Comunidad();

    unMiembro.aceptarloAComunidad(unaComunidad);
    unMiembro2.aceptarloAComunidad(unaComunidad);
    unMiembro3.aceptarloAComunidad(unaComunidad);

    Entidad hospital = new Entidad("Hospital Central");
    Establecimiento unEstablecimiento = new Sucursal("Hospital Central- Norte"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    hospital.agregarEstablecimiento(unEstablecimiento);

    Servicio escalera = new Servicio();
    PrestacionServicio elevadorMecanica = new PrestacionServicio();

    elevadorMecanica.setUnServicio(escalera); // le digo che: esta prestacion de servicio responde a un Servicio baño
    Incidente escaleraRota = new Incidente("La escalera tuvo un desperfecto Mecanico","escalera rota",elevadorMecanica);
    unMiembro.abrirIncidente(escaleraRota);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setViaNotificacion(NotificadorVia.WHATSAPP);
    unMiembro.notificarSegunForma(unaComunidad);
    //unaComunidad.agregarIncidenteReportados(escaleraRota); -- ya esta en abrirIncidente

    unMiembro2.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro2.setViaNotificacion(NotificadorVia.WHATSAPP);

    //esta bien esto.
    //solamente que yo aun no termine de que un miembro2 reciba los incidentes que no fueron abiertos por miembro1
    unaComunidad.notificarAcadaMiembroElIncidente();

    // es una prueba para ver si pisa la funcion , y que me deberia devolver 0 debido a que no hay funcion
    // definida en la parte else para cuando hay un whatsapp, implementar jajaj
    int incidenteListaEnMiembro2 = unMiembro2.verificarTipoNoNotificacionYRecibir2(unaComunidad).size();


    // System.out.println("incidente:" + incidenteListaEnMiembro2);
    // y que quiero probar con esto? de que funcionaria esa funcion.
    Assert.assertEquals(0, incidenteListaEnMiembro2);

  }

  @Test
  public void unMiembroDeLaComunidadQueNoAbrioElIncidenteSabeTiene2IncidentesParaSerNotificados(){
    Miembro unMiembro = new Miembro("Filipino","Ecchioto","filiEcchito@gmail.com",false);
    Miembro unMiembro2 = new Miembro("Silvina","Perez","silvipe@gmail.com",false);
    Miembro unMiembro3 = new Miembro("Julian","Hernandez","julher@gmail.com",false);

    Comunidad unaComunidad = new Comunidad();

    unMiembro.aceptarloAComunidad(unaComunidad);
    unMiembro2.aceptarloAComunidad(unaComunidad);
    unMiembro3.aceptarloAComunidad(unaComunidad);

    Entidad hospital = new Entidad("Hospital Central");
    Establecimiento unEstablecimiento = new Sucursal("Hospital Central- Norte"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    hospital.agregarEstablecimiento(unEstablecimiento);

    Servicio escalera = new Servicio();
    PrestacionServicio elevadorMecanica = new PrestacionServicio();

    elevadorMecanica.setUnServicio(escalera); // le digo che: esta prestacion de servicio responde a un Servicio baño
    Incidente escaleraRota = new Incidente("La escalera tuvo un desperfecto Mecanico","escalera rota",elevadorMecanica);
    Incidente escaleraSucia = new Incidente("la escalera esta muy sucia","escalera sucia",elevadorMecanica);
    unMiembro.abrirIncidente(escaleraRota);
    unMiembro.abrirIncidente(escaleraSucia);

    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setViaNotificacion(NotificadorVia.WHATSAPP);
    unMiembro.notificarSegunForma(unaComunidad);

    //unaComunidad.agregarIncidenteReportados(escaleraRota); -- ya esta en abrirIncidente

    unMiembro2.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro2.setViaNotificacion(NotificadorVia.EMAIL);

    //esta bien esto.
    //solamente que yo aun no termine de que un miembro2 reciba los incidentes que no fueron abiertos por miembro1
    unaComunidad.notificarAcadaMiembroElIncidente();

    int incidenteListaEnMiembro2 = unMiembro2.verificarTipoNoNotificacionYRecibir2(unaComunidad).size();


    System.out.println("incidente:" + incidenteListaEnMiembro2);
    // y que quiero probar con esto? de que funcionaria esa funcion.
    //Assert.assertEquals(2, incidenteListaEnMiembro2);

  }

  @Test
  public void darDeBajaUnIncidente(){}

  @Test
  public void verificarSiSuModoNotificacionEsWhatsapp(){
    Miembro unMiembro = new Miembro("Roberto","Puccio","rpuccio@gmail.com",false);
    unMiembro.setNroWhatsapp("1153112241");
    unMiembro.configurarMedioNotificacion(NotificadorVia.WHATSAPP);
    Assertions.assertEquals(NotificadorVia.WHATSAPP.getEstado(),"whatsapp" );
  }

  @Test
  public void unMiembroDeUnaComunidadDaDeAAltaUnIncidente(){
    Miembro unMiembro = new Miembro("Alfonso","Rodriguez","alfro@gmail.com",true);
    Miembro unMiembro2 = new Miembro("Silvina","Perez","silvipe@gmail.com",false);
    Miembro unMiembro3 = new Miembro("Julian","Hernandez","julher@gmail.com",false);

    Comunidad unaComunidad = new Comunidad();
    unMiembro.aceptarloAComunidad(unaComunidad);
    unMiembro2.aceptarloAComunidad(unaComunidad);
    unMiembro3.aceptarloAComunidad(unaComunidad);

    Entidad supermercadoElSuperChinoMonopoly = new Entidad("Supermercado super chino monopolizador");
    Servicio banios = new Servicio();
    PrestacionServicio banio = new PrestacionServicio();

    banio.setUnServicio(banios); // le digo che: esta prestacion de servicio responde a un Servicio baño
    Establecimiento unEstablecimiento = new Sucursal("Ji-Hao Market"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    supermercadoElSuperChinoMonopoly.agregarEstablecimiento(unEstablecimiento);

    Incidente banioInundado = new Incidente("hinodoro tapado con papel higienico","baño inundado",banio);
    unMiembro.abrirIncidente(banioInundado);


    Assertions.assertEquals(1,unMiembro.getIncidentesAbiertos().size());
  }

  /*
    //A testear!
    @Test
    public void unMiembroDeUnaComunidadDaDeAAltaUnIncidenteYDebeRecibirNotificacion(){
    Miembro unMiembro = new Miembro("Alfonso","Rodriguez","alfro@gmail.com",true);
    Miembro unMiembro2 = new Miembro("Silvina","Perez","silvipe@gmail.com",false);
    Miembro unMiembro3 = new Miembro("Julian","Hernandez","julher@gmail.com",false);

    Comunidad unaComunidad = new Comunidad();
    unMiembro.aceptarloAComunidad(unaComunidad);
    unMiembro2.aceptarloAComunidad(unaComunidad);
    unMiembro3.aceptarloAComunidad(unaComunidad);

    Entidad supermercadoElSuperChinoMonopoly = new Entidad("Supermercado super chino monopolizador");
    Servicio banios = new Servicio();
    PrestacionServicio banio = new PrestacionServicio();

    banio.setUnServicio(banios); // le digo che: esta prestacion de servicio responde a un Servicio baño
    Establecimiento unEstablecimiento = new Sucursal("Ji-Hao Market"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    supermercadoElSuperChinoMonopoly.agregarEstablecimiento(unEstablecimiento);

    Incidente banioInundado = new Incidente("hinodoro tapado con papel higienico","baño inundado",banio);
    unMiembro.abrirIncidente(banioInundado);


    Assertions.assertEquals(1,unMiembro.getIncidentesAbiertos().size());
  }
  */
  @Test
  public void existen3MiembrosDeUnaComunidadQueTienenElIncidenteEnSuLista(){
    Miembro unMiembro = new Miembro("Alfonso","Rodriguez","alfro@gmail.com",true);
    Miembro unMiembro2 = new Miembro("Silvina","Perez","silvipe@gmail.com",false);
    Miembro unMiembro3 = new Miembro("Julian","Hernandez","julher@gmail.com",false);


    Comunidad unaComunidad = new Comunidad();
    unMiembro.aceptarloAComunidad(unaComunidad);
    unMiembro2.aceptarloAComunidad(unaComunidad);
    unMiembro3.aceptarloAComunidad(unaComunidad);

    Entidad supermercadoElSuperChinoMonopoly = new Entidad("Supermercado super chino monopolizador");
    Servicio banios = new Servicio();
    PrestacionServicio banio = new PrestacionServicio();

    banio.setUnServicio(banios); // le digo che: esta prestacion de servicio responde a un Servicio baño
    Establecimiento unEstablecimiento = new Sucursal("Ji-Hao Market"); // el establecimiento sera sucursal
    // agrego el establecimiento hacia la entidad
    supermercadoElSuperChinoMonopoly.agregarEstablecimiento(unEstablecimiento);

    Incidente banioInundado = new Incidente("hinodoro tapado con papel higienico","baño inundado",banio);
    unMiembro.abrirIncidente(banioInundado);

    Assertions.assertEquals(1,unaComunidad.getIncidentesReportados().size());
  }
  @Test
  public void verificarSiEstaA1Con55kmDeIncidente() throws IOException, LocalizacionException {

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);
    Ubicacion ubicacionUnMiembro = new Ubicacion(-22.186252893663585, -65.32948423143813);

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

    CalculadorDistancia calculadorDistancia = new CalculadorDistanciaAlgoritmo();

    unMiembro.setCalculadorDistanciaAIncidente(calculadorDistancia);

    Localizacion lugarDelIncidente = elevadorDetenido.obtenerLocalizacionDeLaPrestacion();

    //System.out.println(lugarDelIncidente.getCoordenadas().getLat());
    //System.out.println(lugarDelIncidente.getCoordenadas().getLon());

    //System.out.println("esta cerca: " + unMiembro.verificadorDistanciaAlIncidente(2.0,elevadorDetenido));

    // esta coordenada obtenida esta a 1,55km de distancia del punto del municipio
    Assert.assertEquals(true, unMiembro.verificadorDistanciaAlIncidente(1.55,elevadorDetenido));
  }

  @Test
  public void verificarSiEstaA11Con49kmDeIncidente() throws IOException, LocalizacionException {

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);
    Ubicacion ubicacionUnMiembro = new Ubicacion(-22.21900707733039, -65.23253738314997);

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

    CalculadorDistancia calculadorDistancia = new CalculadorDistanciaAlgoritmo();

    unMiembro.setCalculadorDistanciaAIncidente(calculadorDistancia);

    Localizacion lugarDelIncidente = elevadorDetenido.obtenerLocalizacionDeLaPrestacion();

    //System.out.println(lugarDelIncidente.getCoordenadas().getLat());
    //System.out.println(lugarDelIncidente.getCoordenadas().getLon());

    //System.out.println("esta cerca: " + unMiembro.verificadorDistanciaAlIncidente(2.0,elevadorDetenido));



    // esta coordenada obtenida esta a 11.98km de distancia del punto del municipio
    Assert.assertEquals(true, unMiembro.verificadorDistanciaAlIncidente(11.49,elevadorDetenido));
  }

  @Test
  public void verificarSiEstaA15kmDeIncidente() throws IOException, LocalizacionException {

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);
    Ubicacion ubicacionUnMiembro = new Ubicacion(-22.142270715729484, -65.45404483647344);

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

    CalculadorDistancia calculadorDistancia = new CalculadorDistanciaAlgoritmo();

    unMiembro.setCalculadorDistanciaAIncidente(calculadorDistancia);

    Localizacion lugarDelIncidente = elevadorDetenido.obtenerLocalizacionDeLaPrestacion();

    //System.out.println(lugarDelIncidente.getCoordenadas().getLat());
    //System.out.println(lugarDelIncidente.getCoordenadas().getLon());

    //System.out.println("esta cerca: " + unMiembro.verificadorDistanciaAlIncidente(2.0,elevadorDetenido));



    // esta coordenada obtenida esta a 13km de distancia del punto del municipio
    Assert.assertEquals(false, unMiembro.verificadorDistanciaAlIncidente(16,elevadorDetenido));
  }

  @Test
  public void son3losIncidentesDadoDeAltaQueNoPasaronLas24hs(){
    // no voy a hacer todos los pasos como anteriores test, solamente hare incidentes y ver la lista

    Miembro unMiembro = new Miembro("Cecilia","Puccio","cecipuccio@gmail.com",true);


    Comunidad unaComunidad = new Comunidad();
    unMiembro.aceptarloAComunidad(unaComunidad); //

    Incidente elevadorDetenido = new Incidente("el elevador se quedo atascado","elevador sin servicio",null);
    Incidente banioObstruido = new Incidente("el baño esta inundado","baño Inundado",null);
    Incidente molineteAtascado = new Incidente("el molinete no deja pasar personas","molinete atascado",null);

    unMiembro.abrirIncidente(elevadorDetenido);
    unMiembro.abrirIncidente(banioObstruido);
    unMiembro.abrirIncidente(molineteAtascado);

    Assert.assertEquals(3, unMiembro.filtrarAquellosIncidentesNoNotificados(unaComunidad).size());
  }
}
