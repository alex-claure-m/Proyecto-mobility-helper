package ar.edu.utn.frba.dds.domain.services.fusioncomunidad;

import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.Comunidad;
import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.Recomendacion;
import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.listados.ListadoDeFusion;
import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.listados.ListadoDeRecomendaciones;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class ServicioFusionDeComunidad {
	private static final Retrofit retrofit;

	static {
		//TO-DO: obtener esto de un archivo de configuración
		String urlAPI = "https://service-01-merge-community-utn-production.up.railway.app/";

		retrofit = new Retrofit.Builder()
						.baseUrl(urlAPI)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
	}

	public static List<Recomendacion> recomendaciones(List<Comunidad> comunidades) throws IOException {
		MergeCommunityService mergeCommunityService = retrofit.create(MergeCommunityService.class);

		Call<ListadoDeRecomendaciones> request = mergeCommunityService.recomendacion(comunidades);
		Response<ListadoDeRecomendaciones> response = request.execute();

		return response.body().recomendaciones;
	}

	public static Comunidad fusion(Recomendacion recomendacion) throws IOException {
		MergeCommunityService mergeCommunityService = retrofit.create(MergeCommunityService.class);

		Call<ListadoDeFusion> request = mergeCommunityService.fusion(recomendacion);
		Response<ListadoDeFusion> response = request.execute();

		//NOTA: la API por alguna razón devuelve un listado de comunidades,
		//y el tercer elemento de este listado es la comunidad fusionada.
		return response.body().fusion.get(2);
	}
}
