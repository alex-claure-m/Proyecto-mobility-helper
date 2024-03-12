package ar.edu.utn.frba.dds.domain.rankings;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.services.ranking.ApiServicioException;
import ar.edu.utn.frba.dds.domain.services.ranking.ServicioDeRanking;
import ar.edu.utn.frba.dds.domain.services.ranking.entities.Dato;
import ar.edu.utn.frba.dds.domain.services.ranking.entities.ListadoDeDatos;
import ar.edu.utn.frba.dds.domain.services.ranking.entities.ListadoDeResultados;
import ar.edu.utn.frba.dds.domain.services.ranking.entities.Resultado;

import java.io.IOException;
import java.util.*;

public class RankingGradoDeImpactoDeLosIncidentes implements Ranking {
    private List<Registro<Entidad, Double>> datos;

    public RankingGradoDeImpactoDeLosIncidentes(List<Entidad> entidades) {
        this.datos = new ArrayList<Registro<Entidad, Double>>();

        try {
            this.generar(entidades);
        } catch (ApiServicioException | IOException e) {
            e.printStackTrace();
        }
    }

    private void generar(List<Entidad> entidades) throws IOException {
        ListadoDeDatos listadoDeDatos = new ListadoDeDatos();

        //armamos listado de datos
        Integer id = 1;
        Map<Integer, Entidad> indices = new HashMap<Integer, Entidad>(); //para recordar qué entidad representa tal id
        for(Entidad entidad : entidades) {
            Dato dato = new Dato();
            dato.entidadId = id;
            dato.tiempoTotal = entidad.tiempoTotalDeResolucionDeIncidentes();
            dato.cantAfectados = entidad.cantidadDeMiembrosAfectados();
            dato.cantNoResueltos = entidad.cantidadDeIncidentesNoResueltos();

            indices.put(id++, entidad); //asociar este id con esta entidad
            listadoDeDatos.datos.add(dato);
        }

        //enviamos los datos a la API para que nos devuelva los resultados
        ListadoDeResultados listadoDeResultados =
                ServicioDeRanking.getInstancia().obtenerRanking(listadoDeDatos);

        //ahora debemos llenar nuestra lista con datos
        for(Resultado resultado : listadoDeResultados.resultados) {
            Entidad entidad = indices.get(resultado.entidadId); //recuperar la entidad por id
            Double gradoDeImpacto = resultado.gradoDeImpacto;

            this.datos.add(new Registro<Entidad, Double>(entidad, gradoDeImpacto));
        }

        //por último ordenamos nuestra lista de datos por valor (gradoDeImpacto)
        this.datos.sort(Comparator.comparing(Registro::getValor));
    }

    public String tituloDelRanking() {
        return "Ranking: Grado de Impacto de las Problemáticas";
    }

    public List<Registro<String, String>> datosComoString() {
        List<Registro<String, String>> datos = new ArrayList<Registro<String, String>>();

        for(Registro<Entidad, Double> dato : this.datos) {
            String nombre = dato.getObjeto().getNombre();
            String valor = dato.getValor().toString();

            datos.add(new Registro<String, String>(nombre, valor));
        }

        return datos;
    }

    public Entidad getDato(Integer id) {
        return this.datos.get(id).getObjeto();
    }
}