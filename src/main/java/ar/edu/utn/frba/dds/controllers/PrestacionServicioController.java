package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.validadores.ValidadorDeDatosDeServicio;
import ar.edu.utn.frba.dds.controllers.values.DatosDeServicio;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrestacionServicioController {
	@Getter
	private static final Repositorio<PrestacionServicio> repositorio;

	static {
		repositorio = FactoryRepositorio.get(PrestacionServicio.class);
	}

	public static ModelAndView getCrearPrestacion(Request request, Response response) {
		Establecimiento establecimiento = EstablecimientosController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("establecimiento_id")));

		if(establecimiento != null) {
			Map<String, Object> model = new HashMap<>();
			model.put("nombreEstablecimiento", establecimiento.nombreMostrado());
			return new ModelAndView(model, "prestaciones/crear.hbs");
		}

		response.status(404);
		return null;
	}

	public static ModelAndView postCrearPrestacion(Request request, Response response) {
		DatosDeServicio datosDeServicio = new DatosDeServicio();
		datosDeServicio.nombre = request.queryParams("nombre");
		datosDeServicio.establecimientoId = request.queryParams("establecimiento_id");

		Validacion validacion = ValidadorDeDatosDeServicio.validar(datosDeServicio);
		if(validacion.getCodigo() == 0) {
			crearPrestacionServicio(datosDeServicio);
			response.redirect("/establecimientos/ver?id=" + datosDeServicio.establecimientoId);
			return null;
		}

		Map<String, Object> model = new HashMap<>();
		Establecimiento establecimiento = EstablecimientosController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("establecimiento_id")));
		model.put("nombreEstablecimiento", establecimiento.nombreMostrado());
		model.put("error_descripcion", validacion.getDescripcion()); //mostrar el error
		return new ModelAndView(model, "prestaciones/crear.hbs");
	}

	public static void crearPrestacionServicio(DatosDeServicio datosDeServicio) {
		PrestacionServicio prestacionServicio = new PrestacionServicio();

		Servicio servicio = new Servicio();
		servicio.setNombre(datosDeServicio.nombre);
		prestacionServicio.setUnServicio(servicio);
		FactoryRepositorio.get(Servicio.class).agregar(servicio);

		Establecimiento establecimiento = EstablecimientosController.getRepositorio()
				.buscar(Integer.parseInt(datosDeServicio.establecimientoId));

		establecimiento.agregarPrestacion(prestacionServicio);
		prestacionServicio.setUnEstablecimientoEnParticular(establecimiento);

		EstablecimientosController.getRepositorio().modificar(establecimiento);

		repositorio.agregar(prestacionServicio);
	}

	public static ModelAndView getVerPrestacion(Request request, Response response) {
		PrestacionServicio prestacionServicio = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		List<Incidente> incidentesReportados = prestacionServicio.getIncidentesReportados();

		Map<String, Object> model = new HashMap<>();
		model.put("incidentes", incidentesReportados);
		model.put("prestacionServicio", prestacionServicio);
		if(prestacionServicio.estaDisponible())
			model.put("estaDisponible", true);
		return new ModelAndView(model, "prestaciones/prestacion.hbs");
	}

	public static Response removerPrestacionServicio(Request request, Response response) {
		PrestacionServicio prestacionServicio =
				repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		if(prestacionServicio != null) {
			EstablecimientosController.getRepositorio().buscarTodos().
					forEach(establ -> {
						establ.getPrestaciones().remove(prestacionServicio);
						EstablecimientosController.getRepositorio().modificar(establ);
					});

			response.redirect("/establecimientos/ver?id="
					+ prestacionServicio.getUnEstablecimientoEnParticular().getId());
			prestacionServicio.setUnEstablecimientoEnParticular(null);
			repositorio.eliminar(prestacionServicio);
		} else {
			response.status(404);
		}

		return response;
	}

	public static void controlarManipulacion(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(!usuario.tieneAlgunRol(TipoRol.MIEMBRO, TipoRol.ADMINISTRADOR_PLATAFORMA))
			Spark.halt(403);
	}
}
