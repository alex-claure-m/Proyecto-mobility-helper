import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.establecimientos.Sucursal;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.miembro.Estado;
import ar.edu.utn.frba.dds.domain.miembro.FormaNotificacion;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.miembro.NotificadorVia;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Municipio;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Provincia;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import ar.edu.utn.frba.dds.domain.usuarios.Rol;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;

public class MainExample implements WithSimplePersistenceUnit {

  public static void main (String[] args) {
    //new MainExample().servicio_comunidad();
    new MainExample().agregar_usuario_rol();
    //new MainExample().testearEstablecimientosConIncidentes();
  }

  public void servicio_comunidad(){
    Comunidad unComunidad = new Comunidad();
    unComunidad.setNombre("Comunidad 1");

    Miembro unMiembro = new Miembro();
    unMiembro.setNombre("Entrega04");
    unMiembro.setApellido("Persiste_Servicio_comunidad");
    unMiembro.setCorreoElectronico("andara?@gmail.com");
    unMiembro.setNroWhatsapp("223156123");
    unMiembro.setViaNotificacion(NotificadorVia.EMAIL);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setEstado(Estado.AFECTADO);

    Servicio unServicio = new Servicio();
    unServicio.setNombre("CleanFast");
    unServicio.setComunidadAsociada(unComunidad);

    unComunidad.agregarMiembroAcomunidad(unMiembro);
    unComunidad.agregarServicioCreados(unServicio);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    entityManager().persist(unMiembro);
    entityManager().persist(unServicio);
    entityManager().persist(unComunidad);
    tx.commit();
  }
  public void agregar_usuario_rol(){
    Usuario unUser = new Usuario();
    unUser.setNameUser("Ian");
    unUser.setPasswordUser("12345678");
    Rol unRol = new Rol();
    Rol unRol2 = new Rol();
    unRol.setTipoRol("miembro");
    //unRol2.setTipoRol("administrador");
    //unRol.setTipo(TipoRol.MIEMBRO);
    //unRol2.setTipo(TipoRol.ADMINISTRADOR_PLATAFORMA);

    unUser.agregarRol(unRol);
    unUser.agregarRol(unRol2);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    entityManager().persist(unRol);
    entityManager().persist(unRol2);
    entityManager().persist(unUser);
    tx.commit();
  }

  public void agregar_miembro(){
    Miembro unMiembro = new Miembro();
    unMiembro.setNombre("Robin");
    unMiembro.setApellido("Hood");
    unMiembro.setCorreoElectronico("robincito@gmail.com");
    unMiembro.setNroWhatsapp("112264301");
    unMiembro.setViaNotificacion(NotificadorVia.EMAIL);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setEstado(Estado.AFECTADO);

    Miembro otroMiembro = new Miembro();
    otroMiembro.setNombre("Selmirita");
    otroMiembro.setApellido("La_lange");
    otroMiembro.setCorreoElectronico("laMinionBenPeola@gmail.com");
    otroMiembro.setNroWhatsapp("132431841");
    otroMiembro.setEsAdmin(true);
    otroMiembro.setViaNotificacion(NotificadorVia.WHATSAPP);
    otroMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    otroMiembro.setEstado(Estado.OBSERVADOR);


    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    entityManager().persist(otroMiembro);
    entityManager().persist(unMiembro);

    tx.commit();

  }
  //esto funciona!
  public void agregar_comunidad_entidad_establecimiento_incidente_miembro(){
    Comunidad unComunidad = new Comunidad();
    unComunidad.setNombre("Comunidad 2 ");

    Miembro unMiembro = new Miembro();
    unMiembro.setNombre("Ian");
    unMiembro.setApellido("Mambru");
    unMiembro.setCorreoElectronico("i_a_n@gmail.com");
    unMiembro.setNroWhatsapp("11246481");
    unMiembro.setViaNotificacion(NotificadorVia.EMAIL);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setEstado(Estado.AFECTADO);

    unComunidad.agregarMiembroAcomunidad(unMiembro);

    Entidad bancoSantander = new Entidad();
    bancoSantander.setNombre("Santander");

    Servicio elevador = new Servicio();
    elevador.setNombre("elevador");
    elevador.setComunidadAsociada(unComunidad);

    PrestacionServicio elevadorMecanico = new PrestacionServicio();
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    elevadorMecanico.setDisponible(Boolean.TRUE);
    Sucursal sucursalJujuy = new Sucursal("Santander Sucursal Jujuy"); // el establecimiento sera sucursal

    // agrego el establecimiento a la lista que tiene Entidad de establecimientos
    bancoSantander.agregarEstablecimiento(sucursalJujuy);

    elevadorMecanico.setUnEstablecimientoEnParticular(sucursalJujuy);

    //seteo para relacionar establecimiento a la entidad -- esta bien esto??
    sucursalJujuy.setEntidadAsociada(bancoSantander);

    Incidente elevadorDetenido = new Incidente();
    elevadorDetenido.setDescripcion("incidente en el elevador piso 1");
    elevadorDetenido.setCabezera("incidente piso 1");
    elevadorDetenido.setFechaApertura(LocalDateTime.now());

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    entityManager().persist(unMiembro);
    entityManager().persist(unComunidad);
    entityManager().persist(elevador);
    entityManager().persist(elevadorMecanico);
    entityManager().persist(sucursalJujuy);
    entityManager().persist(bancoSantander);
    entityManager().persist(elevadorDetenido);

    tx.commit();

  }


  //esta version lo que trato es que miembro abra incidente
  // entonces ese incidente estara en la lista de comunidad
  public void agregar_comunidad_entidad_estableciento_incidente_miembro2() {
    Comunidad unComunidad = new Comunidad();
    unComunidad.setNombre("Comunidad 3 ");

    Miembro unMiembro = new Miembro();
    unMiembro.setNombre("Selmira");
    unMiembro.setApellido("Lansh");
    unMiembro.setCorreoElectronico("MeLlamanJudas@gmail.com");
    unMiembro.setNroWhatsapp("1231561");
    unMiembro.setViaNotificacion(NotificadorVia.EMAIL);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setEstado(Estado.AFECTADO);
    unMiembro.setEsAdmin(Boolean.TRUE);

    unComunidad.agregarMiembroAcomunidad(unMiembro);
    // agrego miembro a la comunidad

    Entidad bancoSantander = new Entidad();
    bancoSantander.setNombre("Santander");
    // instnacio las entidades

    Servicio elevador = new Servicio();
    elevador.setNombre("elevador");
    elevador.setComunidadAsociada(unComunidad);
    //instancio el servicio y lo asocio a la cmunidad


    PrestacionServicio elevadorMecanico = new PrestacionServicio();
    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador
    elevadorMecanico.setDisponible(Boolean.TRUE);
    Sucursal sucursalJujuy = new Sucursal("Santander Sucursal Jujuy"); // el establecimiento sera sucursal
    // instancio la prestacion de servocio que le corresponde al servicio.
    // agrego el establecimiento a la lista que tiene Entidad de establecimientos
    bancoSantander.agregarEstablecimiento(sucursalJujuy);

    elevadorMecanico.setUnEstablecimientoEnParticular(sucursalJujuy);

    //seteo para relacionar establecimiento a la entidad -- esta bien esto??
    sucursalJujuy.setEntidadAsociada(bancoSantander);

    Incidente elevadorDetenido = new Incidente();
    elevadorDetenido.setDescripcion("incidente en el elevador piso 2");
    elevadorDetenido.setCabezera("incidente piso 2");
    elevadorDetenido.setFechaApertura(LocalDateTime.now());

    unMiembro.abrirIncidente(elevadorDetenido);


    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();


    entityManager().persist(elevador);
    entityManager().persist(elevadorMecanico);
    entityManager().persist(sucursalJujuy);
    entityManager().persist(bancoSantander);
    entityManager().persist(elevadorDetenido);
    entityManager().persist(unComunidad);
    entityManager().persist(unMiembro);

    tx.commit();
  }

  public void agregar_entidad_prestacion_servicio(){
    Entidad bancoSantander = new Entidad();
    bancoSantander.setNombre("Santander");

    Establecimiento sucursalAlmagro = new Sucursal();
    sucursalAlmagro.setNombre("Almagro bank");

    PrestacionServicio elevadorMecanico = new PrestacionServicio();
    elevadorMecanico.setUnEstablecimientoEnParticular(sucursalAlmagro);

    Servicio elevador = new Servicio();
    elevador.setNombre("elevador");

    elevadorMecanico.setUnServicio(elevador);

    bancoSantander.agregarEstablecimiento(sucursalAlmagro);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(elevador);
    entityManager().persist(elevadorMecanico);
    entityManager().persist(sucursalAlmagro);
    entityManager().persist(bancoSantander);

    tx.commit();
  }

  // este lo tengo que arreglar
  public void agregar_Incidente_Comunidad_Miembro_Entidad_Prestacion_Servicio(){
    Miembro unMiembro = new Miembro();
    unMiembro.setNombre("Ian");
    unMiembro.setApellido("Mambru");
    unMiembro.setCorreoElectronico("i_a_n@gmail.com");
    unMiembro.setNroWhatsapp("11246481");
    unMiembro.setViaNotificacion(NotificadorVia.EMAIL);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setEstado(Estado.AFECTADO);

    Entidad bancoSantander = new Entidad();
    bancoSantander.setNombre("Santander");

    Servicio elevador = new Servicio();
    elevador.setNombre("elevador");

    PrestacionServicio elevadorMecanico = new PrestacionServicio();

    elevadorMecanico.setUnServicio(elevador); // le digo che: esta prestacion de servicio responde a un Servicio Elevador

    Establecimiento unEstablecimiento = new Sucursal("Santander Sucursal Jujuy"); // el establecimiento sera sucursal

    // agrego el establecimiento a la lista que tiene Entidad de establecimientos
    bancoSantander.agregarEstablecimiento(unEstablecimiento);

    //seteo para relacionar establecimiento a la entidad -- esta bien esto??
    unEstablecimiento.setEntidadAsociada(bancoSantander);

    Incidente elevadorDetenido = new Incidente();
    elevadorDetenido.setDescripcion("incidente en el elevador piso 1");
    elevadorDetenido.setCabezera("incidente piso 1");
    elevadorDetenido.setFechaApertura(LocalDateTime.now());


    unMiembro.abrirIncidente(elevadorDetenido);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(unMiembro);
    tx.commit();
  }




  public void agregacionMiembro_Comunidad(){
    Comunidad laComunidad2 = new Comunidad();
    laComunidad2.setNombre("la comunidad - backUp");

    Miembro unMiembro = new Miembro();
    unMiembro.setNombre("Filiberto");
    unMiembro.setApellido("Ruttolini");
    unMiembro.setCorreoElectronico("FiliRutolini@gmail.com");
    unMiembro.setNroWhatsapp("115123441");
    unMiembro.setViaNotificacion(NotificadorVia.EMAIL);
    unMiembro.setFormaNotifi(FormaNotificacion.CUANDO_SUCEDEN);
    unMiembro.setEstado(Estado.AFECTADO);

    laComunidad2.agregarMiembroAcomunidad(unMiembro);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(laComunidad2);
    entityManager().persist(unMiembro);
    tx.commit();

  }

  public void recuperarListaDeEntidades(){
    List<Entidad> entidades = entityManager().createQuery("from " + Entidad.class.getName()).getResultList();

    System.out.println(entidades);
  }
  public void recuperarEntidadPorNombre(){
    Entidad unaEntidad = (Entidad) entityManager()
        //mantener bien los espacios entre cada palabra reservada del pseudo - sql
        .createQuery("from " + Entidad.class.getName()+ " where nombre = :nombre")
        .setParameter("nombre", "Banco Juan")
        .getSingleResult(); // single record es para que devuelva un solo registro con ese nombre

  }

  private void buscarEntidadPorId(){
    // cuando hago un find sucede un proceso de HIDRATACION
    // LO QUE HACE ES crear una clase nueva Entidad pero rellenando con los datos de la base de datos
    // por eso necesita constructor vacio

    Entidad unaEntidad = entityManager().find(Entidad.class, 1);
    System.out.println(unaEntidad);
  }


  private void guardarEntidad(){
    Entidad unaEntidad = new Entidad();
    unaEntidad.setNombre("Banco Robert");

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(unaEntidad);
    tx.commit();

  }


  public void testearEntidadesConEstablecimientos() {
    Entidad unaEntidad = new Entidad();
    unaEntidad.setNombre("una_entidad");

    Sucursal unaSucursal = new Sucursal();
    unaSucursal.setNombre("una_sucursal");

    Sucursal otraSucursal = new Sucursal();
    otraSucursal.setNombre("otra_sucursal");

    unaEntidad.agregarEstablecimiento(unaSucursal);
    unaEntidad.agregarEstablecimiento(otraSucursal);
    unaSucursal.setEntidadAsociada(unaEntidad);
    otraSucursal.setEntidadAsociada(unaEntidad);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    entityManager().persist(unaSucursal);
    entityManager().persist(otraSucursal);

    entityManager().persist(unaEntidad);
    tx.commit();


    //recuperar entidad
    Entidad entidad = entityManager().find(Entidad.class, 3);
    System.out.println(entidad.getNombre());
    for(Establecimiento e : entidad.getEstablecimientos()) {
      System.out.println(e.getNombre());
    }

  }

  public void testearEstablecimientosConIncidentes() {
    Sucursal unaSucursal = new Sucursal();
    unaSucursal.setNombre("unaSucursal");

    PrestacionServicio unaPrestacion = new PrestacionServicio();
    unaPrestacion.setUnServicio(new Servicio("servicioEscaleras"));

    PrestacionServicio otraPrestacion = new PrestacionServicio();
    otraPrestacion.setUnServicio(new Servicio("servicioPuertaPrincipal"));

    Incidente incidenteEscaleras = new Incidente("de unaPrestacion", "incidenteEscaleras", unaPrestacion);
    Incidente incidentePuertaPrincipal= new Incidente("de unaPrestacion", "incidentePuertaPrincipal", unaPrestacion);
    Incidente otroIncidente = new Incidente("de otraPrestacion", "otroIncidente", otraPrestacion);

    unaPrestacion.agregarIncidente(incidenteEscaleras);
    unaPrestacion.agregarIncidente(incidentePuertaPrincipal);
    otraPrestacion.agregarIncidente(otroIncidente);

    unaSucursal.agregarPrestacion(unaPrestacion);
    unaSucursal.agregarPrestacion(otraPrestacion);


    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(unaPrestacion.getUnServicio());
    entityManager().persist(otraPrestacion.getUnServicio());
    entityManager().persist(unaPrestacion);
    entityManager().persist(otraPrestacion);
    entityManager().persist(incidenteEscaleras);
    entityManager().persist(incidentePuertaPrincipal);
    entityManager().persist(otroIncidente);
    entityManager().persist(unaSucursal);
    tx.commit();

    System.out.println();


    //recuperar unaSucursal
    Sucursal sucu = (Sucursal) entityManager()
            .createQuery("from " + Establecimiento.class.getName() + " where nombre = :nombre")
                    .setParameter("nombre", "unaSucursal").getSingleResult();;
    System.out.println(sucu.getNombre());
    for(PrestacionServicio pr : sucu.getPrestaciones()) {
      System.out.println("Servicio: " + pr.getUnServicio().getNombre());

      for(Incidente incidente : pr.getIncidentesReportados()) {
        System.out.println(
                "Incidente: " + incidente.getCabezera() + " - " + incidente.getDescripcion() +
                " - Servicio: " + incidente.getUnaPrestacionDeServicio().getUnServicio().getNombre());
      }
    }
  }

  //TODO: PROBAR EL CASO: Una LineaDeSubte pasa por varias Estaciones, y por una Estacion pueden pasar varias LineasDeSubte
  //esto es un ManyToMany de Entidad a Establecimiento (corregir??)

  public void testearUsuariosYComunidades() {
    /*Usuario unUsuario = new Usuario("unUsuario", "pass_1");
    Usuario otroUsuario = new Usuario("otroUsuario", "pass_2");

    //CONSULTAR TEMA DE USUARIO ...
     */
  }

  //test que en realidad tendría que hacerse con la API rest
  public void testLocalizacionEntidad() {
    Entidad unaEntidad = new Entidad();
    unaEntidad.setNombre("unaEntidad");

    Localizacion unaLocalizacion = new Localizacion();
    Provincia unaProvincia = new Provincia();
    unaProvincia.setNombre("unaProvincia");
    Municipio unMunicipio = new Municipio();
    unMunicipio.setNombre("unMunicipio");
    unaLocalizacion.setProvincia(unaProvincia);
    unaLocalizacion.setMunicipio(unMunicipio);
    unaEntidad.setLocalizacion(unaLocalizacion);

    System.out.println("--persistiendo localización");

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(unaLocalizacion);
    entityManager().persist(unaEntidad);
    tx.commit();

    unaEntidad = (Entidad) entityManager()
        .createQuery("FROM " + Entidad.class.getName() + " WHERE nombre = 'unaEntidad'").getSingleResult();

    System.out.println(String.format("%s está ubicada en %s, de %s",
        unaEntidad.getNombre(),
        unaEntidad.getLocalizacion().getMunicipio().getNombre(),
        unaEntidad.getLocalizacion().getProvincia().getNombre()));
  }
}
