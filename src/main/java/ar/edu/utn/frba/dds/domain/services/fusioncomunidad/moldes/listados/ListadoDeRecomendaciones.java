package ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.listados;

import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.Recomendacion;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListadoDeRecomendaciones {
	@SerializedName("possibleMerges")
	public List<Recomendacion> recomendaciones;

	public ListadoDeRecomendaciones() {
		this.recomendaciones = new ArrayList<>();
	}
}
