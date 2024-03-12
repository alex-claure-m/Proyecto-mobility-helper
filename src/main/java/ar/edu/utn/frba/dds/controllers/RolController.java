package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.usuarios.Rol;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.factories.FactoryRepositorio;
import lombok.Getter;

import java.util.List;
import java.util.Set;

public class RolController {
  @Getter
  private static final Repositorio<Rol> repositorio;



  static {
    repositorio = FactoryRepositorio.get(Rol.class);
  }
  public static Rol buscarPorId(Integer idRol) {
    return repositorio.buscar(idRol);
  }

  public static Rol buscarRolPorNombre(String nombreRol) {
    // Buscar el rol por su nombre en la base de datos
    List<Rol> roles = FactoryRepositorio.get(Rol.class).buscarTodos();
    for (Rol rol : roles) {
      if (rol.getTipoRol().equalsIgnoreCase(nombreRol)) {
        return rol;
      }
    }
    return null; // Si no se encuentra el rol, devolver null
  }
}
