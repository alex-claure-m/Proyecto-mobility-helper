package ar.edu.utn.frba.dds.controllers;


import ar.edu.utn.frba.dds.domain.comunidad.Comunidad;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioController {

  @Getter
  private static Repositorio<Servicio> repositorio = FactoryRepositorio.get(Servicio.class);

  public static ModelAndView getCrearServicio(Request request, Response response) {
    LoginController.forzarLogueo(request, response);

    // Código para mostrar la página de alta de servicio
    return new ModelAndView(null, "/servicio/crear_servicio.hbs");
  }

  public static ModelAndView postCrearServicio(Request request, Response response) {
    LoginController.forzarLogueo(request, response);

    // Obtener los datos del formulario
    String nombre = request.queryParams("nombre");

    if (nombre != null && !nombre.isEmpty()) {
      Servicio servicio = new Servicio();
      servicio.setNombre(nombre);
      repositorio.agregar(servicio);
      response.redirect("/ver_servicios"); // no va route del folder (osea /comunidad)
    } else {
      Map<String, Object> model = new HashMap<>();
      model.put("creacion_error", true);
      return new ModelAndView(model, "servicio/crear_servicio.hbs");
    }
    return null;
  }

  public static ModelAndView getVerServicios(Request request, Response response) {
    // Verificar si el usuario ha iniciado sesión y obtener información del usuario
    LoginController.forzarLogueo(request, response);
    Map<String, Object> model = new HashMap<>();
    List<Servicio> servicios = repositorio.buscarTodos();
    model.put("servicios", servicios); //Nota: no funciona sin la dependencia 'handlebars 4.3.1'!!!

    return new ModelAndView(model, "servicio/ver_servicios.hbs");
  }


}
