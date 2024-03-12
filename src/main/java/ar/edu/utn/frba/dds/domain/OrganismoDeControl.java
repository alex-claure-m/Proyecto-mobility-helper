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
@Table(name = "Organismo_control")
public class OrganismoDeControl extends Persistence {
    @Column(name="abreviatura")
    private String abreviatura;

    @Column(name="nombre_completo")
    private String nombreCompleto;

    @OneToOne
    @JoinColumn(name="usuario_id", referencedColumnName="id")
    private Usuario usuarioAsociado;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Entidad> entidadesMonitoreadas;

    public OrganismoDeControl() {
        this.entidadesMonitoreadas = new ArrayList<>();
    }

    public OrganismoDeControl(String[] datos) {
        this.nombreCompleto = datos[0];
        this.abreviatura = datos[1];
    }

    public void agregarEntidadMonitoreada(Entidad entidad) {
        this.entidadesMonitoreadas.add(entidad);
    }
}
