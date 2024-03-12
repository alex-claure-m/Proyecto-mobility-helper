package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.validadores.establecimientos.ValidadorDeDatosDeEstacion;
import ar.edu.utn.frba.dds.controllers.validadores.establecimientos.ValidadorDeDatosDeSucursal;
import ar.edu.utn.frba.dds.controllers.values.establecimientos.DatosDeEstacion;
import ar.edu.utn.frba.dds.controllers.values.establecimientos.DatosDeSucursal;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.entidad.TipoDeEntidad;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.establecimientos.Estacion;
import ar.edu.utn.frba.dds.domain.establecimientos.Sucursal;
import ar.edu.utn.frba.dds.domain.establecimientos.TipoDeEstacion;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstablecimientosController {
	@Getter
	private static final Repositorio<Establecimiento> repositorio;

	static {
		repositorio = FactoryRepositorio.get(Establecimiento.class);
	}

	public static ModelAndView getVerEstablecimiento(Request request, Response response) {
		Establecimiento establecimiento = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		if(establecimiento != null) {
			if (establecimiento.nombreMostrado().contains("Estación"))
				return getVerEstacion(request, response);
			else if (establecimiento.nombreMostrado().contains("Sucursal"))
				return getVerSucursal(request, response);
		}

		response.status(404);
		return null;
	}

	public static ModelAndView getVerEstablecimientos(Request request, Response response) {
		if(request.queryParams("id") != null)
			return getVerEstablecimiento(request, response);

		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		List<Establecimiento> establecimientos = repositorio.buscarTodos();

		Map<String, Object> model = new HashMap<>();
		model.put("establecimientos", establecimientos);
		model.put("usuario", usuario); //para mostrar nombre del user

		return new ModelAndView(model, "establecimientos/establecimientos.hbs");
	}

	public static ModelAndView getCrearEstablecimiento(Request request, Response response) {
		Entidad entidad = FactoryRepositorio.get(Entidad.class)
				.buscar(Integer.parseInt(request.queryParams("entidad_id")));

		if(entidad.getTipoDeEntidad() == TipoDeEntidad.TRANSPORTE_PUBLICO)
			return getCrearEstacion(request, response);
		else if(entidad.getTipoDeEntidad() == TipoDeEntidad.EMPRESA_PRESTADORA)
			return getCrearSucursal(request, response);

		response.status(404);
		return null;
	}

	public static ModelAndView postCrearEstablecimiento(Request request, Response response) {
		Entidad entidad = EntidadesController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("entidad_id")));

		if(entidad.getTipoDeEntidad() == TipoDeEntidad.TRANSPORTE_PUBLICO) {
			DatosDeEstacion datosDeEstacion = new DatosDeEstacion();
			datosDeEstacion.nombre = request.queryParams("nombre");
			datosDeEstacion.lat = request.queryParams("lat");
			datosDeEstacion.lon = request.queryParams("lon");
			datosDeEstacion.tipoDeEstacion = request.queryParams("tipo_id");
			datosDeEstacion.entidadId = request.queryParams("entidad_id");

			Validacion validacion = ValidadorDeDatosDeEstacion.validar(datosDeEstacion);
			if(validacion.getCodigo() == 0) {
				Estacion estacion = crearEstacion(datosDeEstacion);
				entidad.agregarEstablecimiento(estacion);
				EntidadesController.getRepositorio().modificar(entidad);
				response.redirect("/entidades/ver?id=" + datosDeEstacion.entidadId);
			}

			Map<String, Object> model = new HashMap<>();
			model.put("es_estacion", true);
			model.put("tipo", "Estación");
			model.put("error_descripcion", validacion.getDescripcion()); //mostrar el error
			return new ModelAndView(model, "establecimientos/crear.hbs");
		}
		else if(entidad.getTipoDeEntidad() == TipoDeEntidad.EMPRESA_PRESTADORA) {
			DatosDeSucursal datosDeSucursal = new DatosDeSucursal();
			datosDeSucursal.nombre = request.queryParams("nombre");
			datosDeSucursal.lat = request.queryParams("lat");
			datosDeSucursal.lon = request.queryParams("lon");
			datosDeSucursal.entidadId = request.queryParams("entidad_id");

			Validacion validacion = ValidadorDeDatosDeSucursal.validar(datosDeSucursal);
			if(validacion.getCodigo() == 0) {
				Sucursal sucursal = crearSucursal(datosDeSucursal);
				entidad.agregarEstablecimiento(sucursal);
				EntidadesController.getRepositorio().modificar(entidad);
				response.redirect("/entidades/ver?id=" + datosDeSucursal.entidadId);
			}

			Map<String, Object> model = new HashMap<>();
			model.put("tipo", "Sucursal");
			model.put("error_descripcion", validacion.getDescripcion()); //mostrar el error
			return new ModelAndView(model, "establecimientos/crear.hbs");
		}

		response.status(404);
		return null;
	}


	public static ModelAndView getVerEstacion(Request request, Response response) {
		Estacion estacion = (Estacion) repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		List<PrestacionServicio> prestaciones = estacion.getPrestaciones();

		Map<String, Object> model = new HashMap<>();
		model.put("establecimiento", estacion);
		model.put("tipoDeEstacion", estacion.getTipoDeEstacion());
		model.put("prestaciones", prestaciones);
		if(UsuariosController.obtenerDeRequest(request)
				.tieneAlgunRol(TipoRol.ADMINISTRADOR_PLATAFORMA,TipoRol.ENTIDAD_PRESTADORA)) { //modificar este (no todas deberían poder)
			model.put("puedeModificar", true);
		}
		return new ModelAndView(model, "establecimientos/establecimiento.hbs");
	}

	public static ModelAndView getCrearEstacion(Request request, Response response) {
		Map<String, Object> model = new HashMap<>();
		model.put("esEstacion", true);
		model.put("tipo", "Estación");
		return new ModelAndView(model, "establecimientos/crear.hbs");
	}

	public static Estacion crearEstacion(DatosDeEstacion datosDeEstacion) {
		Estacion estacion = new Estacion();
		estacion.setNombre(datosDeEstacion.nombre);

		//setear tipo de estacion
		if(datosDeEstacion.tipoDeEstacion.equals("1"))
			estacion.setTipoDeEstacion(TipoDeEstacion.TREN);
		else if(datosDeEstacion.tipoDeEstacion.equals("2"))
			estacion.setTipoDeEstacion(TipoDeEstacion.SUBTE);
		else if(datosDeEstacion.tipoDeEstacion.equals("3"))
			estacion.setTipoDeEstacion(TipoDeEstacion.COLECTIVO);
		else
			estacion.setTipoDeEstacion(TipoDeEstacion.ND);

		Double lat = Double.parseDouble(datosDeEstacion.lat);
		Double lon = Double.parseDouble(datosDeEstacion.lon);
		estacion.setUbicacionGeografica(new Ubicacion(lat, lon));

		repositorio.agregar(estacion);
		return estacion;
	}


	public static ModelAndView getVerSucursal(Request request, Response response) {
		Sucursal sucursal = (Sucursal) repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		List<PrestacionServicio> prestaciones = sucursal.getPrestaciones();

		Map<String, Object> model = new HashMap<>();
		model.put("establecimiento", sucursal);
		model.put("entidadAsociada", sucursal.getEntidadAsociada());
		model.put("prestaciones", prestaciones);
		if(UsuariosController.obtenerDeRequest(request)
				.tieneAlgunRol(TipoRol.ADMINISTRADOR_PLATAFORMA,TipoRol.ENTIDAD_PRESTADORA)) { //modificar este (no todas deberían poder)
			model.put("puedeModificar", true);
		}
		return new ModelAndView(model, "establecimientos/establecimiento.hbs");
	}

	public static ModelAndView getCrearSucursal(Request request, Response response) {
		Map<String, Object> model = new HashMap<>();
		model.put("tipo", "Sucursal");
		return new ModelAndView(model, "establecimientos/crear.hbs");
	}

	public static Sucursal crearSucursal(DatosDeSucursal datosDeSucursal) {
		Sucursal sucursal = new Sucursal();
		sucursal.setNombre(datosDeSucursal.nombre);

		Double lat = Double.parseDouble(datosDeSucursal.lat);
		Double lon = Double.parseDouble(datosDeSucursal.lon);
		sucursal.setUbicacionGeografica(new Ubicacion(lat, lon));

		int entidadId = Integer.parseInt(datosDeSucursal.entidadId);
		Entidad entidadAsociada = FactoryRepositorio.get(Entidad.class).buscar(entidadId);

		sucursal.setEntidadAsociada(entidadAsociada);

		repositorio.agregar(sucursal);
		return sucursal;
	}

	public static Response removerEstablecimiento(Request request, Response response) {
		Establecimiento establecimiento = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		if(establecimiento != null) {
			establecimiento.getPrestaciones().clear();
			EntidadesController.getRepositorio().buscarTodos().
					forEach(entidad -> entidad.getEstablecimientos().remove(establecimiento));
			repositorio.eliminar(establecimiento);
			response.redirect("/establecimientos/ver");
		} else {
			response.status(404);
		}

		return response;
	}

	public static void controlarManipulacion(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(request.pathInfo().contains("ver"))
			return;

		if(!usuario.tieneAlgunRol(TipoRol.ENTIDAD_PRESTADORA,
				TipoRol.ORGANISMO_DE_CONTROL, TipoRol.ADMINISTRADOR_PLATAFORMA))
			Spark.halt(403);
	}
}
