package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.ServicioFusionDeComunidad;
import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Servicio1IntegracionTest {
	Comunidad com1, com2;
	Miembro miemb1, miemb2, miemb3, miemb4, miemb5;
	Establecimiento est1, est2;
	Servicio serv1;

	@BeforeEach
	public void init() {
		com1 = new Comunidad("1", "comunidad-1", "0", 1);
		com2 = new Comunidad("2", "comunidad-2", "0", 1);

		miemb1 = new Miembro("1", "miembro-1");
		miemb2 = new Miembro("2", "miembro-2");
		miemb3 = new Miembro("3", "miembro-3");
		miemb4 = new Miembro("4", "miembro-4");
		miemb5 = new Miembro("5", "miembro-5");

		est1 = new Establecimiento("1", "establecimiento-1");
		est2 = new Establecimiento("2", "establecimiento-2");

		serv1 = new Servicio("1", "servicio-1");


		//agregar los miembros
		Collections.addAll(com1.miembroList, miemb1, miemb2, miemb3, miemb4);
		Collections.addAll(com2.miembroList, miemb2, miemb3, miemb4, miemb5);

		//agregar los establecimientos
		Collections.addAll(com1.establecimientoList, est1);
		Collections.addAll(com2.establecimientoList, est1);

		//agregar los servicios
		Collections.addAll(com1.servicioList, serv1);
		Collections.addAll(com2.servicioList, serv1);
	}

	@Test
	public void testRecomendaciones() throws IOException {
		//armar el listado de comunidades a enviar
		List<Comunidad> comunidades = new ArrayList<>();
		Collections.addAll(comunidades, com1, com2);

		//obtener el listado de recomendaciones - llama a la API
		List<Recomendacion> recomendaciones = ServicioFusionDeComunidad.recomendaciones(comunidades);

		//imprimimos cada recomendación (par de comunidades fusionables)
		System.out.println("comunidades fusionables: ");
		recomendaciones.forEach(recomendacion ->
			System.out.println(recomendacion.comunidad1.nombre + " con " + recomendacion.comunidad2.nombre));

		Assertions.assertNotNull(recomendaciones);
	}

	@Test
	public void testFusion() throws IOException {
		//armamos una recomendación y la enviamos para fusionar - llama a la API
		Comunidad comunidadFusionada = ServicioFusionDeComunidad.fusion(new Recomendacion(com1, com2));

		//imprimimos el nombre de la comunidad fusionada y sus miembros
		System.out.println(comunidadFusionada.nombre + ":");
		comunidadFusionada.miembroList.forEach(miembro ->
						System.out.println("miembro " + miembro.id + ": " + miembro.nombre));

		Assertions.assertNotNull(comunidadFusionada);
	}

	@Test
	public void lasComunidadesFueronFusionadas() throws IOException {
		Comunidad comunidadFusionada = ServicioFusionDeComunidad.fusion(new Recomendacion(com1, com2));
		Assertions.assertEquals(5, comunidadFusionada.miembroList.size());
	}

	@Test
	public void noHayFusionPorFaltaDeMiembrosEnComun() throws IOException {
		com2.miembroList.remove(miemb2); //removemos un miembro para testear si no hay fusión
		Assertions.assertNull(ServicioFusionDeComunidad.fusion(new Recomendacion(com1, com2)));
	}

	@Test
	public void noHayFusionPorqueLaUltimaFueHaceMenosDe6Meses() throws IOException {
		//com1.ultimaFusion = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		//com2.ultimaFusion = LocalDateTime.now().plusMonths(5).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		Comunidad comunidadFusionada = ServicioFusionDeComunidad.fusion(new Recomendacion(com1, com2));

		//la comunidad acaba der fusionada, no debería poder fusionarse otra vez
		Assertions.assertNull(ServicioFusionDeComunidad.fusion(new Recomendacion(comunidadFusionada, com2)));
	}

	@Test
	public void seRecomendaLaFusionDeLasDosComunidades() throws IOException {
		List<Comunidad> comunidades = new ArrayList<>();
		Collections.addAll(comunidades, com1, com2);

		List<Recomendacion> recomendaciones = ServicioFusionDeComunidad.recomendaciones(comunidades);
		Assertions.assertTrue(
						recomendaciones.get(0).comunidad1.nombre.equals("comunidad-1") &&
						recomendaciones.get(0).comunidad2.nombre.equals("comunidad-2")
		);
	}
}
