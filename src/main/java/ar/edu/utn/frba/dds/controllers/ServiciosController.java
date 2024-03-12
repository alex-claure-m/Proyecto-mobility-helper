package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;

public class ServiciosController {
	private static final Repositorio<PrestacionServicio> repositorio;

	static {
		repositorio = FactoryRepositorio.get(PrestacionServicio.class);
	}

	public static void crearServicio(String nombre) {
		Servicio servicio = new Servicio();
		servicio.setNombre(nombre);

		repositorio.agregar(servicio);
	}
}
