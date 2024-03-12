package ar.edu.utn.frba.dds.domain.usuarios;

import ar.edu.utn.frba.dds.domain.Persistence;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Rol")
public class Rol extends Persistence {

  @Column(name = "usuario")
  @ManyToMany(mappedBy = "roles")
  private Set<Usuario> usuarios = new HashSet<>();
/*
  @Column(name = "tipo")
  @Enumerated(EnumType.STRING)
  private TipoRol tipo;
*/
  @Column(name = "tipo_de_rol")
  private String tipoRol;

  public Rol() {
  }

  public void eliminarUsuario(Usuario unUsuario){
    usuarios.remove(unUsuario);
  }
  public void agregarUsuario(Usuario unUsuario){
    usuarios.add(unUsuario);
  }
}
