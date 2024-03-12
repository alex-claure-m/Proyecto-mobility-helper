package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.validadores.ValidadorDeCargaMasivaDeReguladoras;
import ar.edu.utn.frba.dds.domain.OrganismoDeControl;
import ar.edu.utn.frba.dds.domain.cargadores.CargadorDeDatosDesdeCSV;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganismoDeControlController {
	private static final File uploadDir;
	@Getter
	private static final Repositorio<OrganismoDeControl> repositorio;

	static {
		uploadDir = new File("upload");
		uploadDir.mkdir();

		repositorio = FactoryRepositorio.get(OrganismoDeControl.class);
	}

	public static ModelAndView getVerOrganismoDeControl(Request request, Response response) {
		OrganismoDeControl organismoDeControl = repositorio.buscar(Integer.parseInt(request.params("id")));

		Map<String, Object> model = new HashMap<>();
		model.put("organismoDeControl", organismoDeControl);
		model.put("entidades", organismoDeControl.getEntidadesMonitoreadas());
		return new ModelAndView(model, "usuarios/organismo-de-control.hbs");
	}

	public static ModelAndView getCargarArchivo(Request request, Response response) {
		List<OrganismoDeControl> organismosDeControl =
				FactoryRepositorio.get(OrganismoDeControl.class).buscarTodos();

		Map<String, Object> model = new HashMap<>();
		model.put("reguladora", true);
		model.put("organismosDeControl", organismosDeControl);
		return new ModelAndView(model, "carga-masiva.hbs");
	}

	public static ModelAndView postCargarArchivo(Request request, Response response) {
		//esto sólo funciona para el servidor Jetty
		request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

		String textoCsv = null;
		InputStream input = null;
		java.nio.file.Path tempFile = null;
		try {
			tempFile = java.nio.file.Files.createTempFile(uploadDir.toPath(), "", "");

			input = request.raw().getPart("uploaded_file").getInputStream();
			//textoCsv = new String(input.readAllBytes());
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}

		Validacion validacion = ValidadorDeCargaMasivaDeReguladoras.validar(textoCsv);
		if(validacion.getCodigo() == 0) {
			try {
				java.nio.file.Files.copy(input, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cargaMasivaDeOrganismosDeControl(uploadDir.getPath() + "/" + tempFile.getFileName().toString());
			response.redirect("/carga-masiva/reguladora");
		}

		Map<String, Object> model = new HashMap<>();
		model.put("error_descripcion", validacion.getDescripcion());
		return new ModelAndView(model, "carga-masiva.hbs");
	}

	public static void cargaMasivaDeOrganismosDeControl(String path) {
		List<String[]> datosCsv = CargadorDeDatosDesdeCSV.obtenerDatosDeCSV(path);
		datosCsv.remove(datosCsv.get(0));
		datosCsv.forEach(datosFila -> { //para cada fila..
			Usuario usuarioAsociado = UsuariosController.buscarPorNombre(datosFila[0]);
			if(usuarioAsociado != null && buscarPorUsuario(usuarioAsociado) == null) {
				OrganismoDeControl organismoDeControl = new OrganismoDeControl();
				organismoDeControl.setNombreCompleto(datosFila[1]);
				organismoDeControl.setAbreviatura(datosFila[2]);

				//setear el usuario asociado y su rol
				organismoDeControl.setUsuarioAsociado(usuarioAsociado);
				usuarioAsociado.setTipoRol(TipoRol.ORGANISMO_DE_CONTROL);
				UsuariosController.getRepositorio().modificar(usuarioAsociado);

				repositorio.agregar(organismoDeControl);
			}
		});
	}

	public static ModelAndView getAsociarEntidad(Request request, Response response) {
		OrganismoDeControl organismoDeControl = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		List<Entidad> entidades = EntidadesController.getRepositorio().buscarTodos()
				.stream().filter(entidad -> !organismoDeControl.getEntidadesMonitoreadas().contains(entidad)).toList();

		Map<String, Object> model = new HashMap<>();
		model.put("nombre", organismoDeControl.getAbreviatura());
		model.put("organismoDeControl", organismoDeControl);
		model.put("entidades", entidades);
		return new ModelAndView(model, "usuarios/agregarEntidad.hbs");
	}

	public static Response postAsociarEntidad(Request request, Response response) {
		Entidad entidad = EntidadesController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("entidad_id")));
		OrganismoDeControl organismoDeControl = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		if(!organismoDeControl.getEntidadesMonitoreadas().contains(entidad)) {
			organismoDeControl.agregarEntidadMonitoreada(entidad);
			repositorio.modificar(organismoDeControl);
		}

		response.redirect("/reguladora/" + organismoDeControl.getId());
		return response;
	}

	public static OrganismoDeControl buscarPorUsuario(Usuario usuario) {
		List<OrganismoDeControl> organismosDeControl = repositorio.buscarTodos();
		for(OrganismoDeControl organismoDeControl : organismosDeControl) {
			Usuario usuarioAsociado = organismoDeControl.getUsuarioAsociado();
			if(usuarioAsociado != null && usuarioAsociado.getNameUser().equals(usuario.getNameUser()))
				return organismoDeControl;
		}

		return null;
	}

	public static void controlarAcceso(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(!usuario.tieneAlgunRol(
				TipoRol.ADMINISTRADOR_PLATAFORMA, TipoRol.ORGANISMO_DE_CONTROL))
			Spark.halt(403);
	}
}
