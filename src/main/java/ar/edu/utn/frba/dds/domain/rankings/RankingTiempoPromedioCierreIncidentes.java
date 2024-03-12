package ar.edu.utn.frba.dds.domain.rankings;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;

import java.util.*;

public class RankingTiempoPromedioCierreIncidentes implements Ranking {
    private List<Registro<Entidad, Integer>> datos;

    public RankingTiempoPromedioCierreIncidentes(List<Entidad> entidades) {
        this.datos = new ArrayList<Registro<Entidad, Integer>>();

        for(Entidad entidad : entidades)
            datos.add(new Registro<Entidad, Integer>(entidad, entidad.tiempoPromedioCierreDeIncidentes()));

        this.datos.sort(Comparator.comparing(Registro::getValor));
    }

    public String tituloDelRanking() {
        return "Ranking: Tiempo Promedio de Cierre de Incidentes";
    }

    public List<Registro<String, String>> datosComoString() {
        List<Registro<String, String>> datos = new ArrayList<Registro<String, String>>();

        for(Registro<Entidad, Integer> dato : this.datos) {
            String nombre = dato.getObjeto().getNombre();
            String valor = dato.getValor().toString() + "hs";

            datos.add(new Registro<String, String>(nombre, valor));
        }

        return datos;
    }

    public Entidad getDato(Integer id) {
        return this.datos.get(id).getObjeto();
    }
}