package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.*;
import ar.edu.utn.frba.dds.controllers.InformeSemanalController;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import ar.edu.utn.frba.dds.spark.utils.BooleanHelper;
import ar.edu.utn.frba.dds.spark.utils.HandlebarsTemplateEngineBuilder;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private static HandlebarsTemplateEngine engine;

    private static void initEngine() {
        Router.engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
                .build();
    }

    public static void init() {
        Router.initEngine();
        Spark.staticFileLocation("/public");
        Router.configure();
    }

    private static void configure() {
      Spark.get("/", (req, res) -> {
        res.redirect("/inicio");
        return res;
      });

      //si un recurso no se encontró: en lugar de tirar error, tirar 404.
      Spark.exception(NoResultException.class, (e, req, res) -> res.status(404));

      //pantalla de login
      Spark.get("/login", LoginController::getLogin, engine);
      Spark.get("/logoff", (req, res) -> {
        LoginController.terminarSesion(req);
        res.redirect("/login");
        return res;
      });
      Spark.post("/login", LoginController::postLogin, engine); //post de login
      Spark.post("/logoff", LoginController::postLogoff); //post de logoff

      //forzar auntenticación ante cada request
      Spark.before((req, res) -> {
        String rutaActual = req.pathInfo();
        if(!rutaActual.contains("/log") && !rutaActual.equals("/registrarse")
            && !rutaActual.contains("/api")) {
          if(LoginController.existeSesion(req) == Boolean.FALSE) {
            res.redirect("/login");
            Spark.halt();
          }
        }
      });

      //pantalla de inicio
      Spark.get("/inicio", InicioController::getInicio, engine);
      //preferencias
      Spark.get("/preferencias", InicioController::getPreferencias, engine);
      Spark.post("/preferencias", InicioController::postPreferencias, engine);


      Spark.get("/registrarse", RegistroController::getRegistro, engine); //devolver pantalla de registro
      Spark.post("/registrarse", RegistroController::postRegistro, engine); //recibir datos y devolver pantalla de registro

      //incidentes - listar, abrir, cerrar y consultar
      Spark.path("/incidentes", () -> {
        Spark.before("/*", IncidentesController::controlarAcceso);
        Spark.before("/cerrar", IncidentesController::controlarCierreDeIncidente);
        Spark.get("/ver", IncidentesController::getVerIncidentes, engine);
        Spark.get("/abrir", IncidentesController::getCrearIncidente, engine);
        Spark.post("/abrir", IncidentesController::postCrearIncidente, engine);
        Spark.post("/cerrar", IncidentesController::postCerrarIncidente);
      });

      //ruta para obtener información de los incidentes, sin pasar por logueo.
      //agregada para poder mostrar el listado en el stack asignado de la entrega 7.
      Spark.get("/api/incidentes/ver", IncidentesController::getIncidentes);

        //nosotros
        Spark.get("/nosotros", (req, res) -> {
          return new ModelAndView(null, "nosotros.hbs");
        }, new HandlebarsTemplateEngine());

      //comunidades - abrir, listar, consultar
        /*
        Spark.get("/comunidades", (req, res) -> {
            res.redirect("comunidad/gestion_comunidad");
            return res;
        });
         */
      Spark.get("/comunidades", (req, res) -> {
      Map<String, Object> model = new HashMap<>();
      // Agrega los datos necesarios al modelo, si es necesario
      return new ModelAndView(model, "comunidad/gestion_comunidad.hbs");
      }, new HandlebarsTemplateEngine());


      Spark.before("/comunidad/*", ComunidadController::controlarAcceso);
      Spark.get("/ver_comunidades", ComunidadController::getVerComunidades, engine);
      // Pantalla para crear comunidad
      Spark.get("/comunidad/crear_comunidad", ComunidadController::getCrearComunidad, engine);
      // Procesar creación de comunidad
      Spark.post("/comunidad/crear_comunidad", ComunidadController::postCrearComunidad);
      // para esta declaracion de ruta, mira como esta en ver_comunidades.hbs -> el href que lo redirecciona - linea 55
      Spark.get("/comunidad/ver_comunidad/:id", ComunidadController::getVerComunidad,engine);


      Spark.before("/comunidad/sacar", ComunidadController::controlarManipulacion);
      Spark.get("/comunidad/sacar", ComunidadController::postSacarDeComunidad);
      Spark.post("/comunidad/unirse", ComunidadController::postUnirseComunidad);
      Spark.post("/comunidad/salir", ComunidadController::postDejarComunidad);
      //editar y remover una comunidad
      Spark.get("/comunidad/editar/:id", ComunidadController::getEditarComunidad, engine);
      Spark.post("/comunidad/editar/:id", ComunidadController::postEditarComunidad, engine);
      Spark.get("/comunidad/remover/:id", ComunidadController::removerComunidad);

      Spark.get("/servicios", (req, res) -> {
      Map<String, Object> model = new HashMap<>();
      // Agrega los datos necesarios al modelo, si es necesario
      return new ModelAndView(model, "servicio/gestion_servicios.hbs");
      }, new HandlebarsTemplateEngine());


      Spark.get("/ver_servicios", ServicioController::getVerServicios, engine);
      // Pantalla para crear servicio
      Spark.get("/servicio/crear_servicio", ServicioController::getCrearServicio, engine);
      // Procesar creación de servicio
      Spark.post("/servicio/crear_servicio", ServicioController::postCrearServicio);
      // falta ver servicio paticular

      Spark.get("/usuarios/ver", UsuariosController::getVerUsuarios, engine);
      //Esto agrego nada más para poder redireccionar a la pantalla propia de cada tipo de usuario
      Spark.get("/ver-datos-usuario", (req, res) -> {
        ar.edu.utn.frba.dds.domain.usuarios.Usuario usuario = UsuariosController.getRepositorio()
            .buscar(Integer.parseInt(req.queryParams("id")));

        if(usuario.tieneRol(TipoRol.ENTIDAD_PRESTADORA)) {
          res.redirect("/prestadora/" + EntidadPrestadoraController.buscarPorUsuario(usuario).getId());
        }
        else if(usuario.tieneRol(TipoRol.ORGANISMO_DE_CONTROL)) {
          res.redirect("/reguladora/" + OrganismoDeControlController.buscarPorUsuario(usuario).getId());
        }
        else
          res.redirect("/miembros/ver?id=" + MiembrosController.buscarPorUsuario(usuario).getId());

        return res;
      });


      //miembros - ver (falta MODIFICAR)
      Spark.path("/miembros", () -> {
        Spark.before("/*", MiembrosController::controlarAcceso);
        Spark.get("/ver", MiembrosController::getVerMiembro, engine);
        Spark.post("/privilegio", MiembrosController::postDarPrivilegio); //para dar/sacar privilegio
        Spark.post("/setearUbicacion", MiembrosController::setearUbicacion);
        Spark.post("/marcarInteres", MiembrosController::postMarcarInteres);
        Spark.get("/remover", MiembrosController::removerMiembro);
      });


      //entidades - listar, ver, crear, modificar y remover
      Spark.path("/entidades", () -> {
        Spark.before("/*", EntidadesController::controlarManipulacion);
        Spark.get("/ver", EntidadesController::getVerEntidades, engine);
        Spark.get("/crear", EntidadesController::getCrearEntidad, engine);
        Spark.post("/crear", EntidadesController::postCrearEntidad, engine);
        Spark.get("/modificar", EntidadesController::getModificarEntidad, engine);
        Spark.post("/modificar", EntidadesController::postModificarEntidad, engine);
        Spark.get("/asociar", EntidadesController::getAgregarEstablecimiento, engine);
        Spark.post("/asociar", EntidadesController::postAgregarEstablecimiento);
        Spark.get("/remover", EntidadesController::removerEntidad);
      });

      //establecimientos - listar, ver, crear y remover (falta MODIFICAR)
      Spark.path("/establecimientos", () -> {
        Spark.before("/*", EstablecimientosController::controlarManipulacion);
        Spark.get("/ver", EstablecimientosController::getVerEstablecimientos, engine);
        Spark.get("/crear", EstablecimientosController::getCrearEstablecimiento, engine);
        Spark.post("/crear", EstablecimientosController::postCrearEstablecimiento, engine);
        Spark.get("/remover", EstablecimientosController::removerEstablecimiento);
      });

			//prestaciones - ver, crear y REMOVER
			Spark.path("/prestaciones", () -> {
        Spark.before("/crear", PrestacionServicioController::controlarManipulacion);
        Spark.before("/remover", PrestacionServicioController::controlarManipulacion);
				Spark.get("/ver", PrestacionServicioController::getVerPrestacion, engine);
				Spark.get("/crear", PrestacionServicioController::getCrearPrestacion, engine);
				Spark.post("/crear", PrestacionServicioController::postCrearPrestacion, engine);
				Spark.get("/remover", PrestacionServicioController::removerPrestacionServicio);
			});

      //notificaciones - listar, ver y REMOVER
      Spark.path("/notificaciones", () -> {
        Spark.before("/*", NotificacionesController::controlarAcceso);
        Spark.get("/ver", NotificacionesController::getVerNotificaciones, engine);
        Spark.get("/remover", NotificacionesController::removerNotificacion);
      });

      //entidades prestadoras y organismos de control
      Spark.before("/prestadora/*", EntidadPrestadoraController::controlarAcceso);
      Spark.get("/prestadora/asociar", EntidadPrestadoraController::getAsociarEntidad, engine);
      Spark.post("/prestadora/asociar", EntidadPrestadoraController::postAsociarEntidad);
      Spark.get("/prestadora/:id", EntidadPrestadoraController::getVerEntidadPrestadora, engine);
      Spark.before("/reguladora/*", OrganismoDeControlController::controlarAcceso);
      Spark.get("/reguladora/asociar", OrganismoDeControlController::getAsociarEntidad, engine);
      Spark.post("/reguladora/asociar", OrganismoDeControlController::postAsociarEntidad);
      Spark.get("/reguladora/:id", OrganismoDeControlController::getVerOrganismoDeControl, engine);

      //carga masiva de entidades prestadoras y organismos de control
      Spark.before("/carga-masiva/prestadora", EntidadPrestadoraController::controlarAcceso);
      Spark.before("/carga-masiva/reguladora", OrganismoDeControlController::controlarAcceso);
      Spark.get("/carga-masiva/prestadora", EntidadPrestadoraController::getCargarArchivo, engine);
      Spark.post("/carga-masiva/prestadora", EntidadPrestadoraController::postCargarArchivo, engine);
      Spark.get("/carga-masiva/reguladora", OrganismoDeControlController::getCargarArchivo, engine);
      Spark.post("/carga-masiva/reguladora", OrganismoDeControlController::postCargarArchivo, engine);

      //informes de ranking semanales
      Spark.before("/informe", InformeSemanalController::controlarAcceso);
      Spark.get("/informe", InformeSemanalController::getVerInforme, engine);
      Spark.post("/informe", InformeSemanalController::postVerInforme);
    }
}
