package ar.edu.utn.frba.dds.domain.usuarios;

public class AuthService {
  public boolean tieneRolAutorizado(Usuario unUser, Rol unRol){
    return unUser.getRoles().stream().anyMatch(rol -> rol == unRol);
  }

}
