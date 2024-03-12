package ar.edu.utn.frba.dds.domain.services.ranking;

import ar.edu.utn.frba.dds.domain.services.ranking.entities.ListadoDeDatos;
import ar.edu.utn.frba.dds.domain.services.ranking.entities.ListadoDeResultados;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RankingService {
    @POST("ranking")
    Call<ListadoDeResultados> ranking(@Body ListadoDeDatos listadoDeDatos);
}
