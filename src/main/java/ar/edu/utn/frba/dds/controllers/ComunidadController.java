package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import ar.edu.utn.frba.dds.domain.miembro.Miembro;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComunidadController {
  @Getter
  private static Repositorio<Comunidad> repositorio = FactoryRepositorio.get(Comunidad.class);

  //getCrearComunidad - devuelve la pantalla para crear una comunidad
  public static ModelAndView getCrearComunidad(Request request, Response response) {
    return new ModelAndView(null, "/comunidad/crear_comunidad.hbs");
  }

  //postCrearComunidad - procesa la creación de una comunidad
  public static ModelAndView postCrearComunidad(Request request, Response response) {
    String nombre = request.queryParams("nombre");

    if (nombre != null && !nombre.isEmpty()) {
      Comunidad comunidad = new Comunidad();
      comunidad.setNombre(nombre);

      //guardar el usuario que la creó
      Usuario creadaPor = UsuariosController.obtenerDeRequest(request);
      Miembro miembro = MiembrosController.buscarPorUsuario(creadaPor);
      comunidad.setCreadaPor(creadaPor);
      comunidad.agregarMiembroAcomunidad(miembro);
      miembro.agregarAComunidad(comunidad);

      repositorio.agregar(comunidad);
      response.redirect("/ver_comunidades"); // no va route del folder (osea /comunidad)
    } else {
      Map<String, Object> model = new HashMap<>();
      model.put("creacion_error", "El nombre de la comunidad no es válido");
      return new ModelAndView(model, "comunidad/crear_comunidad.hbs");
    }
    return null;
  }

  public static ModelAndView getVerComunidades(Request request, Response response) {
    Usuario usuario = UsuariosController.obtenerDeRequest(request);
    Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

    Map<String, Object> model = new HashMap<>();
    List<Comunidad> comunidades = repositorio.buscarTodos();
    model.put("comunidades", comunidades); //Nota: no funciona sin la dependencia 'handlebars 4.3.1'!!!
    model.put("misComunidades", miembro.getComunidadesALasQuePertenece());
    return new ModelAndView(model, "comunidad/ver_comunidades.hbs");
  }
/*
  public static ModelAndView getUnirseComunidad(Request request, Response response) {
    Map<String, Object> model = new HashMap<>();
    List<Comunidad> comunidades = repositorio.buscarTodos();

    // Si hay comunidades, se activa vista_unirse
    boolean vista_unirse = !comunidades.isEmpty();

    System.out.println("vusta:" + vista_unirse);

    model.put("comunidades", comunidades);
    model.put("vista_unirse", true);
    return new ModelAndView(model, "comunidad/ver_comunidades.hbs");
  }
*/
  public static Response postUnirseComunidad(Request request, Response response) {
    Comunidad comunidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
    Miembro miembro = MiembrosController.buscarPorUsuario(UsuariosController.obtenerDeRequest(request));

    if(comunidad == null) {
      response.status(404);
      return response;
    }

    miembro.aceptarloAComunidad(comunidad);
    MiembrosController.getRepositorio().modificar(miembro);
    repositorio.modificar(comunidad);

    response.redirect("/ver_comunidades");
    return response;
  }

  public static Response postDejarComunidad(Request request, Response response) {
    Comunidad comunidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
    Miembro miembro = MiembrosController.buscarPorUsuario(UsuariosController.obtenerDeRequest(request));

    if(comunidad == null || !miembro.perteneceAcomunidad(comunidad)) {
      response.status(404);
      return response;
    }

    comunidad.getUnosMiembros().remove(miembro);
    miembro.getComunidadesALasQuePertenece().remove(comunidad);
    MiembrosController.getRepositorio().modificar(miembro);
    repositorio.modificar(comunidad);

    response.redirect("/ver_comunidades");
    return response;
  }

  public static Response postSacarDeComunidad(Request request, Response response) {
    Comunidad comunidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
    Miembro miembro = MiembrosController.getRepositorio()
        .buscar(Integer.parseInt(request.queryParams("miembro_id")));

    if(comunidad.getUnosMiembros().contains(miembro)) {
      comunidad.getUnosMiembros().remove(miembro);
      miembro.getComunidadesALasQuePertenece().remove(comunidad);
      MiembrosController.getRepositorio().modificar(miembro);
      repositorio.modificar(comunidad);
    }

    response.redirect("/comunidad/ver_comunidad/" + comunidad.getId());
    return response;
  }

  //todo ver el tema de por que no quiere traer una comunidad en particular
  // esto con su ver_comunidad.hbs
  public static ModelAndView getVerComunidad(Request request, Response response) {
    Usuario usuario = UsuariosController.obtenerDeRequest(request);
    Miembro miembro = MiembrosController.buscarPorUsuario(usuario);

    Comunidad comunidad =  repositorio.buscar(Integer.parseInt(request.params("id")));
    if (comunidad != null) {
      Map<String, Object> model = new HashMap<>();
      model.put("comunidad", comunidad);
      model.put("miembros", comunidad.getUnosMiembros());
      if(!miembro.perteneceAcomunidad(comunidad))
        model.put("puedeUnirse", true);
      if(usuario.tieneAlgunRol(TipoRol.ADMINISTRADOR_COMUNIDAD, TipoRol.ADMINISTRADOR_PLATAFORMA)) {
        model.put("puedeModificar", true);
      }

      return new ModelAndView(model, "comunidad/ver_comunidad.hbs");
    } else {
     // response.status(404);
      System.out.println("no hay comunidad asociada");
      return null;
    }
  }

  public static ModelAndView getEditarComunidad(Request request, Response response) {
    Map<String, Object> model = new HashMap<>();
    model.put("comunidad", repositorio.buscar(Integer.parseInt(request.params("id"))));
    return new ModelAndView(model, "/comunidad/editar_comunidad.hbs");
  }

  public static ModelAndView postEditarComunidad(Request request, Response response) {
    String nombre = request.queryParams("nombre");

    Map<String, Object> model = new HashMap<>();
    if (nombre != null && !nombre.isEmpty()) {
      Comunidad comunidad = repositorio.buscar(Integer.parseInt(request.params("id")));
      comunidad.setNombre(nombre);

      repositorio.modificar(comunidad);
      response.redirect("/comunidad/ver_comunidad/" + comunidad.getId());
    }
    else {
      model.put("creacion_error", "El nombre de la comunidad no es válido");
    }

    return new ModelAndView(model, "/comunidad/editar_comunidad.hbs");
  }

  public static Response removerComunidad(Request request, Response response) {
    Comunidad comunidad = repositorio.buscar(Integer.parseInt(request.params("id")));

    if(comunidad != null) {
      comunidad.getUnosMiembros()
          .forEach(miemb -> {
            miemb.getComunidadesALasQuePertenece().remove(comunidad);
            MiembrosController.getRepositorio().modificar(miemb);
          });
      comunidad.setUnosMiembros(Collections.emptyList());
      repositorio.eliminar(comunidad);
      response.redirect("/ver_comunidades");
    } else {
      response.status(404);
    }

    return response;
  }

  public static void controlarAcceso(Request request, Response response) {
    Usuario usuario = UsuariosController.obtenerDeRequest(request);

    //si no tiene ninguno de estos roles, bloquear el acceso
    if(!usuario.tieneAlgunRol(TipoRol.MIEMBRO,
        TipoRol.ADMINISTRADOR_COMUNIDAD, TipoRol.ADMINISTRADOR_PLATAFORMA))
      Spark.halt(403);
  }

  public static void controlarManipulacion(Request request, Response response) {
    Usuario usuario = UsuariosController.obtenerDeRequest(request);

    if(!usuario.tieneAlgunRol(TipoRol.ADMINISTRADOR_COMUNIDAD, TipoRol.ADMINISTRADOR_PLATAFORMA))
      Spark.halt(403);
  }
}
