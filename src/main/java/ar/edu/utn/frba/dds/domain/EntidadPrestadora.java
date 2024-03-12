package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.usuarios.Usuario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Entidad_Prestadora")
public class EntidadPrestadora extends Persistence {
    @Column(name="nombre")
    private String nombre;

    @Column(name="rubro")
    private String rubro;

    @Column(name="pais")
    private String pais;

    @OneToOne
    @JoinColumn(name="usuario_id", referencedColumnName="id")
    private Usuario usuarioAsociado;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="prestadora_id", referencedColumnName="id")
    private List<Entidad> entidadesAsociadas;

    public EntidadPrestadora() {
        this.entidadesAsociadas = new ArrayList<>();
    }

    public EntidadPrestadora(String[] datos) {
        this.nombre = datos[0];
        this.rubro = datos[1];
        this.pais = datos[2];
    }

    public void agregarEntidadAsociada(Entidad entidad) {
        this.entidadesAsociadas.add(entidad);
    }
}
