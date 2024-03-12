package ar.edu.utn.frba.dds.domain.informes;

import ar.edu.utn.frba.dds.domain.rankings.Ranking;
import ar.edu.utn.frba.dds.domain.rankings.Registro;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorDeInformesDeRankings {

    //este es el método que se debe llamar semanalmente
    public static InformeDeRankings generar(List<Ranking> rankings) {
        InformeDeRankings informe = new InformeDeRankings("informeSemanal");

        //agregar título como dato
        informe.agregarDatos(tituloDelInforme(), "/n", "/n");

        //concatenar los datos de cada ranking
        for (Ranking ranking : rankings) {
            informe.agregarDatos(ranking.tituloDelRanking(), "/n");

            int contador = 1;
            for (Registro<String, String> dato : ranking.datosComoString()) {
                //agregar una entrada en texto para el informe
                String entrada = String.format("%d. %s: %s",
                                        contador++,
                                        dato.getObjeto(),
                                        dato.getValor());

                informe.agregarDatos(entrada, "/n");
            }

            //*acá podríamos agregar un String que indique nueva página*

            informe.agregarDatos("/n", "/n");
        }

        return informe;
    }

    public static String tituloDelInforme() {
        return String.format("Informe semanal - (%s)",
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    //no es necesario instanciar esta clase
    private GeneradorDeInformesDeRankings() { }
}