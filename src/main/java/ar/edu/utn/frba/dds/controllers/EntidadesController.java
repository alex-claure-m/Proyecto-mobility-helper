package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.validadores.ValidadorDeDatosDeEntidad;
import ar.edu.utn.frba.dds.controllers.values.DatosDeEntidad;
import ar.edu.utn.frba.dds.domain.EntidadPrestadora;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.entidad.TipoDeEntidad;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Municipio;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Provincia;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
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

public class EntidadesController {
	@Getter
	private static final Repositorio<Entidad> repositorio;

	static {
		repositorio = FactoryRepositorio.get(Entidad.class);
	}

	//getVerEntidad - muestra información de una entidad en particular
	public static ModelAndView getVerEntidad(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);
		Entidad entidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		List<Establecimiento> establecimientos = entidad.getEstablecimientos();
		EntidadPrestadora entidadPrestadora = EntidadPrestadoraController.buscarPorUsuario(usuario);

		Map<String, Object> model = new HashMap<>();
		model.put("entidad", entidad);
		model.put("establecimientos", establecimientos);
		model.put("usuario", usuario); //para mostrar nombre del user
		if(usuario.tieneRol(TipoRol.ADMINISTRADOR_PLATAFORMA) || (entidadPrestadora != null
				&& entidadPrestadora.getEntidadesAsociadas().contains(entidad))) {
			model.put("puedeModificar", true);
		}

		if(usuario.tieneAlgunRol(TipoRol.MIEMBRO,
				TipoRol.ADMINISTRADOR_COMUNIDAD, TipoRol.ADMINISTRADOR_PLATAFORMA))
			model.put("puedeVerInteres", true);
		if(MiembrosController.buscarPorUsuario(usuario).getEntidadesDeInteres().contains(entidad))
			model.put("estaInteresado", true);

		return new ModelAndView(model, "entidades/entidad.hbs");
	}

	//getVerEntidades - muestra un listado de entidades o una sola
	public static ModelAndView getVerEntidades(Request request, Response response) {
		if(request.queryParams("id") != null)
			return getVerEntidad(request, response);

		List<Entidad> entidades = repositorio.buscarTodos();

		Map<String, Object> model = new HashMap<>();
		model.put("entidades", entidades);
		model.put("usuario", UsuariosController.obtenerDeRequest(request)); //para mostrar nombre del user
		return new ModelAndView(model, "entidades/entidades.hbs");
	}

	public static ModelAndView getCrearEntidad(Request request, Response response) {
		Map<String, Object> model = new HashMap<>();
		if(request.queryParams("prestadora_id") != null) {
			int prestadoraId = Integer.parseInt(request.queryParams("prestadora_id"));
			EntidadPrestadora entidadPrestadora =
					FactoryRepositorio.get(EntidadPrestadora.class).buscar(prestadoraId);

			model.put("entidadPrestadora", entidadPrestadora);
		}

		return new ModelAndView(model, "entidades/crear.hbs");
	}

	public static ModelAndView postCrearEntidad(Request request, Response response) {
		DatosDeEntidad datosDeEntidad = new DatosDeEntidad();
		datosDeEntidad.nombre = request.queryParams("nombre");
		datosDeEntidad.tipoDeEntidad = request.queryParams("tipo_id");
		datosDeEntidad.nombreMunicipio = request.queryParams("nombre-mncp");
		datosDeEntidad.nombreProvincia = request.queryParams("nombre-prov");
		datosDeEntidad.prestadoraId = request.queryParams("prestadora_id");

		Validacion validacion = ValidadorDeDatosDeEntidad.validar(datosDeEntidad);
		if(validacion.getCodigo() == 0) {
			crearEntidad(datosDeEntidad);

			//volver a la pantalla de la entidad prestadora que me generó
			response.redirect("/prestadora/" + datosDeEntidad.prestadoraId);
		}

		Map<String, Object> model = new HashMap<>();
		model.put("error_descripcion", validacion.getDescripcion()); //mostrar el error
		return new ModelAndView(model, "entidades/crear.hbs");
	}

	public static void crearEntidad(DatosDeEntidad datosDeEntidad) {
		Entidad entidad = new Entidad();
		entidad.setNombre(datosDeEntidad.nombre);

		//setear tipo de entidad
		if(datosDeEntidad.tipoDeEntidad.equals("1"))
			entidad.setTipoDeEntidad(TipoDeEntidad.TRANSPORTE_PUBLICO);
		else if(datosDeEntidad.tipoDeEntidad.equals("2"))
			entidad.setTipoDeEntidad(TipoDeEntidad.EMPRESA_PRESTADORA);
		else
			entidad.setTipoDeEntidad(TipoDeEntidad.SIN_DEFINIR);

		//setear la localización
		Localizacion localizacion = new Localizacion();
		Provincia provincia = new Provincia();
		provincia.setNombre(datosDeEntidad.nombreProvincia);
		localizacion.setProvincia(provincia);

		if(!datosDeEntidad.nombreMunicipio.isBlank()) {
			Municipio municipio = new Municipio();
			municipio.setNombre(datosDeEntidad.nombreMunicipio);
			localizacion.setMunicipio(municipio);
		}

		entidad.setLocalizacion(localizacion);
		FactoryRepositorio.get(Localizacion.class).agregar(localizacion);

		//agregar esta entidad a la prestadora
		int prestadoraId = Integer.parseInt(datosDeEntidad.prestadoraId);
		EntidadPrestadora entidadPrestadora =
				FactoryRepositorio.get(EntidadPrestadora.class).buscar(prestadoraId);

		entidadPrestadora.agregarEntidadAsociada(entidad);

		repositorio.agregar(entidad);
	}

	public static void modificarEntidad(Entidad entidad, DatosDeEntidad datosDeEntidad) {
		entidad.setNombre(datosDeEntidad.nombre);

		//setear tipo de entidad
		if(datosDeEntidad.tipoDeEntidad.equals("1"))
			entidad.setTipoDeEntidad(TipoDeEntidad.TRANSPORTE_PUBLICO);
		else if(datosDeEntidad.tipoDeEntidad.equals("2"))
			entidad.setTipoDeEntidad(TipoDeEntidad.EMPRESA_PRESTADORA);
		else
			entidad.setTipoDeEntidad(TipoDeEntidad.SIN_DEFINIR);

		Localizacion localizacion = new Localizacion();
		Provincia provincia = new Provincia();
		provincia.setNombre(datosDeEntidad.nombreProvincia);
		localizacion.setProvincia(provincia);

		if(!datosDeEntidad.nombreMunicipio.isBlank()) {
			Municipio municipio = new Municipio();
			municipio.setNombre(datosDeEntidad.nombreMunicipio);
			localizacion.setMunicipio(municipio);
		}

		entidad.setLocalizacion(localizacion);
		FactoryRepositorio.get(Localizacion.class).agregar(localizacion);

		repositorio.modificar(entidad);
	}

	public static ModelAndView getModificarEntidad(Request request, Response response) {
		Entidad entidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		Map<String, Object> model = new HashMap<>();
		model.put("modificar", true);
		model.put("nombreEntidad", entidad.getNombre());
		model.put("nombreProvincia", entidad.getLocalizacion().getProvincia().getNombre());
		if(entidad.getLocalizacion().getMunicipio() != null)
			model.put("nombreMunicipio", entidad.getLocalizacion().getMunicipio().getNombre());
		return new ModelAndView(model, "entidades/crear.hbs");
	}

	public static ModelAndView postModificarEntidad(Request request, Response response) {
		DatosDeEntidad datosDeEntidad = new DatosDeEntidad();
		datosDeEntidad.nombre = request.queryParams("nombre");
		datosDeEntidad.tipoDeEntidad = request.queryParams("tipo_id");
		datosDeEntidad.nombreMunicipio = request.queryParams("nombre-mncp");
		datosDeEntidad.nombreProvincia = request.queryParams("nombre-prov");

		Validacion validacion = ValidadorDeDatosDeEntidad.validar(datosDeEntidad);
		if(validacion.getCodigo() == 0) {
			Entidad entidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

			modificarEntidad(entidad, datosDeEntidad);
			response.redirect("/entidades/ver?id=" + entidad.getId());
		}

		Map<String, Object> model = new HashMap<>();
		model.put("error_descripcion", validacion.getDescripcion()); //mostrar el error
		return new ModelAndView(model, "entidades/crear.hbs");
	}

	public static ModelAndView getAgregarEstablecimiento(Request request, Response response) {
		Entidad entidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		List<Establecimiento> establecimientos = EstablecimientosController.getRepositorio().buscarTodos();

		Map<String, Object> model = new HashMap<>();
		model.put("entidad", entidad);
		String tipo = "";
		if(entidad.getTipoDeEntidad() == TipoDeEntidad.TRANSPORTE_PUBLICO) {
			tipo = "Estación";
			model.put("establecimientos", establecimientos.stream() //mostrar sólo estaciones
					.filter(establ -> establ.nombreMostrado().contains("Estación")
					&& !entidad.getEstablecimientos().contains(establ)).toList());;
		}
		else if(entidad.getTipoDeEntidad() == TipoDeEntidad.EMPRESA_PRESTADORA) {
			tipo = "Sucursal";
			model.put("establecimientos", establecimientos.stream() //mostrar sólo sucursales
					.filter(establ -> establ.nombreMostrado().contains("Sucursal")
					&& !entidad.getEstablecimientos().contains(establ)).toList());
		}
		model.put("tipo", tipo);
		return new ModelAndView(model, "entidades/agregarEstablecimiento.hbs");
	}

	public static Response postAgregarEstablecimiento(Request request, Response response) {
		Entidad entidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		Establecimiento establecimiento = EstablecimientosController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("establecimiento_id")));

		if(!entidad.getEstablecimientos().contains(establecimiento)) {
			entidad.agregarEstablecimiento(establecimiento);
			repositorio.modificar(entidad);
		}

		response.redirect("/entidades/ver?id=" + entidad.getId());
		return response;
	}

	public static Response removerEntidad(Request request, Response response) {
		Entidad entidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		if(entidad != null) {
			EntidadPrestadoraController.getRepositorio().buscarTodos()
							.forEach(entidadPrestadora -> entidadPrestadora.getEntidadesAsociadas().remove(entidad));
			OrganismoDeControlController.getRepositorio().buscarTodos()
					.forEach(entidadPrestadora -> entidadPrestadora.getEntidadesMonitoreadas().remove(entidad));
			repositorio.eliminar(entidad);
			response.redirect("/entidades/ver");
		} else {
			response.status(404);
		}

		return response;
	}

	public static void controlarManipulacion(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(request.pathInfo().contains("ver") || request.pathInfo().contains("crear"))
			return;

		if(usuario.tieneRol(TipoRol.ADMINISTRADOR_PLATAFORMA))
			return;

		if(usuario.tieneRol(TipoRol.ENTIDAD_PRESTADORA)) {
			Entidad entidad = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
			if(entidad == null)
				return;
			if(EntidadPrestadoraController.buscarPorUsuario(usuario).getEntidadesAsociadas().contains(entidad))
				return;
		}

		Spark.halt(403);
	}
}
