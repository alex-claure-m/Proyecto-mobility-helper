package ar.edu.utn.frba.dds.domain.informes;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Sólo consideraremos exportar informes de rankings,
// por lo que no creamos una clase más genérica (e.g. Exportable)

@Getter
public class InformeDeRankings {
    private String nombre;
    private List<String> datos;

    public InformeDeRankings(String nombre) {
        this.nombre = nombre;
        this.datos = new ArrayList<String>();
    }

    public void agregarDatos(String ... datos) {
        Collections.addAll(this.datos, datos);
    }
}