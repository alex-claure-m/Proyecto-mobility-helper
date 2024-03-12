package ar.edu.utn.frba.dds.domain.services.ranking;

import ar.edu.utn.frba.dds.domain.services.ranking.entities.ListadoDeDatos;
import ar.edu.utn.frba.dds.domain.services.ranking.entities.ListadoDeResultados;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

// ServicioDeRanking - implementé esta clase solamente para testear el servidor
public class ServicioDeRanking {
    private static ServicioDeRanking instancia = null;
    private Retrofit retrofit;

    //TODO: obtener esto de un archivo de configuración
    private static String urlAPI = "http://localhost:9002/";

    private ServicioDeRanking() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ServicioDeRanking getInstancia() {
        if(instancia == null)
            instancia = new ServicioDeRanking();

        return instancia;
    }

    public ListadoDeResultados obtenerRanking(ListadoDeDatos listadoDeDatos) throws IOException {
        RankingService rankingService = this.retrofit.create(RankingService.class);

        Call<ListadoDeResultados> request = rankingService.ranking(listadoDeDatos);
        Response<ListadoDeResultados> response = request.execute();

        return response.body();
    }
}
