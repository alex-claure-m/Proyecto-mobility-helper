package ar.edu.utn.frba.dds.domain.servicios.UsuariosTest;

import ar.edu.utn.frba.dds.domain.usuarios.AuthService;
import ar.edu.utn.frba.dds.domain.usuarios.Rol;
import ar.edu.utn.frba.dds.domain.usuarios.TipoRol;
import ar.edu.utn.frba.dds.domain.usuarios.validarPassFile.ContraseniaInvalidaException;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;


public class UsersText {
  /*
  @Test
  public void laContraseniaEsDebilSegunTop10K(){
    try{
      new Usuario("user2","12345678");
    }catch (ContraseniaInvalidaException exception){

      Assertions.assertTrue(exception.getMessage().contains("demasiado debil"));
    }
  }
  @Test
  public void laContraseniaNoEsDebilSegunTop10KPass(){
    try{
      new Usuario("user3","122333444455555");
    }catch (ContraseniaInvalidaException exception){

      Assertions.assertFalse(exception.getMessage().contains("demasiado debil"));
    }
  }


  @Test
  public void laContraseniaFueHasheada(){
    Usuario unUser = new Usuario("user4","a la grande le puse cuca");
    Boolean checkContrasenia = unUser.checkPass("a la grande le puse cuca",unUser.getPasswordUser());
    System.out.println(checkContrasenia);
    Assertions.assertTrue(checkContrasenia, unUser.getPasswordUser());

  }
  @Test
  public void cantidadLongitud(){
    String algo = "jkljkljklj";
    algo.length();
    Assertions.assertEquals(algo.length(), 10);
  }
  @Test
  public void agregaRolYseAutoriza(){
    Usuario unUser = new Usuario("user4","a la grande le puse cuca");
    Rol unRol = new Rol();
    unRol.setTipo(TipoRol.MIEMBRO);
    unUser.agregarRol(unRol);

    AuthService authService = new AuthService();
    Assert.assertEquals(true,authService.tieneRolAutorizado(unUser,unRol));
  }

  @Test
  public void noSeAutorizaPorqueNoTieneRol(){
    Usuario unUser = new Usuario("user4","a la grande le puse cuca");
    Rol unRol = new Rol();
    unRol.setTipo(TipoRol.ADMINISTRADOR_PLATAFORMA);

    AuthService authService = new AuthService();
    Assert.assertEquals(false,authService.tieneRolAutorizado(unUser,unRol));
  }
  @Test
  public void hay2RolesYSeAutoriza(){
    Usuario unUser = new Usuario("user4","a la grande le puse cuca");
    Rol unRol = new Rol();
    Rol unRol2 = new Rol();
    unRol2.setTipo(TipoRol.MIEMBRO);
    unRol.setTipo(TipoRol.ADMINISTRADOR_PLATAFORMA);
    unUser.agregarRol(unRol);
    unUser.agregarRol(unRol2);

    AuthService authService = new AuthService();
    Assert.assertEquals(true,authService.tieneRolAutorizado(unUser,unRol));
    Assert.assertEquals(true,authService.tieneRolAutorizado(unUser,unRol2));
  }
  @Test
  public void hay2RolesYSeAutorizaSolo1(){
    Usuario unUser = new Usuario("user4","a la grande le puse cuca");
    Rol unRol = new Rol();
    Rol unRol2 = new Rol();
    unRol2.setTipo(TipoRol.MIEMBRO);
    unRol.setTipo(TipoRol.ADMINISTRADOR_PLATAFORMA);
    unUser.agregarRol(unRol);


    AuthService authService = new AuthService();
    Assert.assertEquals(true,authService.tieneRolAutorizado(unUser,unRol));
    Assert.assertEquals(false,authService.tieneRolAutorizado(unUser,unRol2));
  }
  @Test
  public void hay2RolesPeroNoSeAutorizaNiguno(){
    Usuario unUser = new Usuario("user4","a la grande le puse cuca");
    Rol unRol = new Rol();
    Rol unRol2 = new Rol();
    unRol2.setTipo(TipoRol.MIEMBRO);
    unRol.setTipo(TipoRol.ADMINISTRADOR_PLATAFORMA);

    AuthService authService = new AuthService();
    Assert.assertEquals(false,authService.tieneRolAutorizado(unUser,unRol));
    Assert.assertEquals(false,authService.tieneRolAutorizado(unUser,unRol2));
  }
  */
}
