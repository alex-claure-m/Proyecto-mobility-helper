package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class InformeSemanalController {
	private static final Path pathArchivo = Path.of("informeSemanal.pdf");

	public static ModelAndView getVerInforme(Request request, Response response) {
		Map<String, Object> model = new HashMap<>();
		if(Files.notExists(pathArchivo)) {
			model.put("no_disponible", true);
			return new ModelAndView(model, "informes.hbs");
		}

		BasicFileAttributes attributes = null;
		try {
			attributes = Files.readAttributes(pathArchivo, BasicFileAttributes.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//obtener la fecha de creación del informe para mostrar
		String fecha = LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd/MM/uuuu' a las 'HH:mm:ss"));
		model.put("archivo_fecha", fecha);

		String fileNameString = pathArchivo.getFileName().toString();
		String formato = fileNameString.substring(fileNameString.lastIndexOf(".") + 1);
		model.put("archivo_formato", formato.toUpperCase());

		String tamanio = Long.toString(1 + attributes.size() / 1000);
		model.put("archivo_tamanio", tamanio + " KB");

		return new ModelAndView(model, "informes.hbs");
	}

	public static Response postVerInforme(Request request, Response response) {
		if(Files.notExists(pathArchivo)) {
			response.status(404);
			return response;
		}

		HttpServletResponse raw = response.raw();
		raw.setContentType("application/pdf");

		try {
			byte[] bytes = Files.readAllBytes(pathArchivo);

			raw.getOutputStream().write(bytes);
			raw.getOutputStream().flush();
			raw.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public static void controlarAcceso(Request request, Response response) {
		Usuario usuario = UsuariosController.obtenerDeRequest(request);

		if(!usuario.tieneAlgunRol(TipoRol.ENTIDAD_PRESTADORA,
				TipoRol.ORGANISMO_DE_CONTROL, TipoRol.ADMINISTRADOR_PLATAFORMA))
			Spark.halt(403);
	}
}
