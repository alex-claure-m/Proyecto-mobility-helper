package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.validadores.Validacion;
import ar.edu.utn.frba.dds.controllers.validadores.ValidadorDeCargaMasivaDePrestadoras;
import ar.edu.utn.frba.dds.domain.EntidadPrestadora;
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

public class EntidadPrestadoraController {
	private static final File uploadDir;
	@Getter
	private static final Repositorio<EntidadPrestadora> repositorio;

	static {
		uploadDir = new File("upload");
		uploadDir.mkdir();

		repositorio = FactoryRepositorio.get(EntidadPrestadora.class);
	}

	public static ModelAndView getVerEntidadPrestadora(Request request, Response response) {
		EntidadPrestadora entidadPrestadora = repositorio.buscar(Integer.parseInt(request.params("id")));

		//para mostrar la lista de entidades asociadas
		List<Entidad> entidades = entidadPrestadora.getEntidadesAsociadas();

		Map<String, Object> model = new HashMap<>();
		model.put("entidadPrestadora", entidadPrestadora);
		model.put("entidades", entidades);
		return new ModelAndView(model, "usuarios/entidad-prestadora.hbs");
	}

	public static ModelAndView getCargarArchivo(Request request, Response response) {
		List<EntidadPrestadora> entidadPrestadoras = repositorio.buscarTodos();

		Map<String, Object> model = new HashMap<>();
		model.put("prestadora", true);
		model.put("entidadesPrestadoras", entidadPrestadoras);
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

		Validacion validacion = ValidadorDeCargaMasivaDePrestadoras.validar(textoCsv);
		if(validacion.getCodigo() == 0) {
			try {
				java.nio.file.Files.copy(input, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cargaMasivaDeEntidadesPrestadoras(uploadDir.getPath() + "/" + tempFile.getFileName().toString());
			response.redirect("/carga-masiva/prestadora");
		}

		Map<String, Object> model = new HashMap<>();
		model.put("error_descripcion", validacion.getDescripcion());
		return new ModelAndView(model, "carga-masiva.hbs");
	}

	public static void cargaMasivaDeEntidadesPrestadoras(String path) {
		List<String[]> datosCsv = CargadorDeDatosDesdeCSV.obtenerDatosDeCSV(path);
		datosCsv.remove(datosCsv.get(0));
		datosCsv.forEach(datosFila -> { //para cada fila..
			Usuario usuarioAsociado = UsuariosController.buscarPorNombre(datosFila[0]);
			if(usuarioAsociado != null && buscarPorUsuario(usuarioAsociado) == null) {
				EntidadPrestadora entidadPrestadora = new EntidadPrestadora();
				entidadPrestadora.setNombre(datosFila[1]);
				entidadPrestadora.setRubro(datosFila[2]);
				entidadPrestadora.setPais(datosFila[3]);

				//setear el usuario asociado y su rol
				entidadPrestadora.setUsuarioAsociado(usuarioAsociado);
				usuarioAsociado.setTipoRol(TipoRol.ENTIDAD_PRESTADORA);
				UsuariosController.getRepositorio().modificar(usuarioAsociado);

				repositorio.agregar(entidadPrestadora);
			}
		});
	}

	public static ModelAndView getAsociarEntidad(Request request, Response response) {
		EntidadPrestadora entidadPrestadora = repositorio.buscar(Integer.parseInt(request.queryParams("id")));
		List<Entidad> entidades = EntidadesController.getRepositorio().buscarTodos()
				.stream().filter(entidad -> !entidadPrestadora.getEntidadesAsociadas().contains(entidad)).toList();

		Map<String, Object> model = new HashMap<>();
		model.put("nombre", entidadPrestadora.getNombre());
		model.put("entidadPrestadora", entidadPrestadora);
		model.put("entidades", entidades);
		return new ModelAndView(model, "usuarios/agregarEntidad.hbs");
	}

	public static Response postAsociarEntidad(Request request, Response response) {
		Entidad entidad = EntidadesController.getRepositorio()
				.buscar(Integer.parseInt(request.queryParams("entidad_id")));
		EntidadPrestadora entidadPrestadora = repositorio.buscar(Integer.parseInt(request.queryParams("id")));

		if(!entidadPrestadora.getEntidadesAsociadas().contains(entidad)) {
			entidadPrestadora.agregarEntidadAsociada(entidad);
			repositorio.modificar(entidadPrestadora);
		}

		response.redirect("/prestadora/" + entidadPrestadora.getId());
		return response;
	}

	public static EntidadPrestadora buscarPorUsuario(Usuario usuario) {
		List<EntidadPrestadora> entidadesPrestadoras = repositorio.buscarTodos();
		for(EntidadPrestadora entidadPrestadora : entidadesPrestadoras) {
			Usuario usuarioAsociado = entidadPrestadora.getUsuarioAsociado();
			if(usuarioAsociado != null && usuarioAsociado.getNameUser().equals(usuario.getNameUser()))
				return entidadPrestadora;
		}

		return null;
	}

	public static void controlarAcceso(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(usuario != null && !usuario.tieneAlgunRol(
				TipoRol.ENTIDAD_PRESTADORA,TipoRol.ADMINISTRADOR_PLATAFORMA))
			Spark.halt(403);
	}
}
