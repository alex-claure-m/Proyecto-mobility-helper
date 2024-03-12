package ar.edu.utn.frba.dds.domain.services.fusioncomunidad;

import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.Comunidad;
import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.Recomendacion;
import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.listados.ListadoDeFusion;
import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.listados.ListadoDeRecomendaciones;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

//interfaz para usar con Retrofit e integrar con el Servicio-1 (Grupo 1).
public interface MergeCommunityService {

	//***RECOMENDACIONES DE FUSIÓN***
	@POST("api/recommendations")
	Call<ListadoDeRecomendaciones> recomendacion(@Body List<Comunidad> comunidades);

	//***FUSIÓN DE DOS COMUNIDADES***
	@POST("api/fusion")
	Call<ListadoDeFusion> fusion(@Body Recomendacion recomendacion);
}
