package ar.edu.utn.frba.dds.domain.usuarios;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.usuarios.validarPassFile.ContraseniaInvalidaException;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.servicios.Servicio;
import ar.edu.utn.frba.dds.domain.ubicacion.Localizacion;
import ar.edu.utn.frba.dds.domain.usuarios.validarPassFile.VerificarPassAdapter;
import ar.edu.utn.frba.dds.domain.usuarios.validarPassFile.VerificarPassOpenFile;
import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Entity
@Table(name = "Usuario")

@Getter
@Setter
public class Usuario extends Persistence {

  @Column(name = "nombre_usuario")
  private String nameUser;

  @Column(name = "password")
  private String passwordUser;

  @Column(name="roles")
  @ManyToMany(fetch = FetchType.EAGER) // FetchType.EAGER para cargar roles al mismo tiempo que el usuario.
  @JoinTable(
      name = "Usuario_Rol",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "rol_id")
  )
  private Set<Rol> roles = new HashSet<>();

  @Column
  @Enumerated(EnumType.STRING)
  private TipoRol tipoRol;

  //considero que la persona para tener un incidente, tiene que conocer y saber
  //que servicio es del que le interesa, cual es el chiste sino, que solamente tenga
  //localizacion sin un servicio. ?


  @Transient
  private Servicio servicioDeInteres;
  @Transient
  private Entidad EntidadDeInteres;
  @Transient
  private Localizacion localizacionDeInteres;

  @Transient
  private Localizacion localidadUbicada;


  private static Integer longitudPass = 8;

  public Usuario(String nameUser, String passwordUser) {
    this.nameUser = nameUser;
    validarLongitudPass(passwordUser);
    // encriptarPassword(passwordUser);
    // - mejor no la compliquemos - this.passwordUser = this.encriptarPassword(passwordUser);;
  }

  public Usuario() {

  }



  //Verifiers SHALL require subscriber-chosen memorized secrets to be at least 8 characters in length

  public void validarLongitudPass(String pass) {
    if (pass.length() < longitudPass.intValue()) {
      throw new ContraseniaInvalidaException("La contraseña debe tener al menos 8 caracteres");
    }else{
      this.verificarPassContraTxtDe10KPeoresPass(pass);
    }
  }
  @Transient
  VerificarPassAdapter validador10k = new VerificarPassOpenFile(); // preguntar si poniendolo asi cuenta como adapter

  private void verificarPassContraTxtDe10KPeoresPass(String passwordUser) {
    validador10k.verificarPassDesdeFile(passwordUser);
  }

  public String encriptarPassword(String password){
    //el salto para aumentar su seguridad
    String passHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
    return passHash;
  }
  public Boolean checkPass(String passUser, String passHash){
    this.encriptarPassword(passUser);
    return BCrypt.checkpw(passUser,passHash );
  }

  public void agregarRol(Rol rol) {
    roles.add(rol);
    rol.agregarUsuario(this);
  }

  public void removerRol(Rol rol) {
    roles.remove(rol);
    rol.eliminarUsuario(this);
  }



  // ----------------------- TODO EL TEMA DE INCIDENTE -------------------------------
  public void notificarIncidente(Incidente incidente) {
    // TODO
    // aca de hecho lo que deberia ir es que el usuario notifique el incidente a la clase INCIDENTE
    // y la clase INCIDENTE lo que debera hacer es notificar a todos los usuarios mediante algun MAIL
    // claro ... ahi esta lo gracioso, como hago que notifique?
    // HACER VALIDACION CON TRY - CATCH
    // de que exista el Incidente y que pertenezca a la locacion de interes
    // y catch :=> si no existe excepcion de NotFoundIncidente!

    //incidente.notificarIncidente();
  }

  public boolean esLocacionDeInteres(Localizacion unaLocalizacion){
    return unaLocalizacion == localizacionDeInteres;
  }


  public String getNameUser() {
    return nameUser;
  }

  public void agregarMunicipioyProvincia(String unMunicipio, String unaProvincia){
  }


  public Boolean tieneRol(String nombreDeRol) {
    for(Rol rol : this.getRoles()) {
      if(rol.getTipoRol().equalsIgnoreCase(nombreDeRol))
        return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

  public Boolean tieneRol(TipoRol tipoRol) {
    return this.tipoRol == tipoRol;
  }

  public Boolean tieneAlgunRol(TipoRol ... tipoRoles) {
    for(TipoRol tipoRol : tipoRoles) {
      if(tipoRol == this.tipoRol)
        return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }
}
