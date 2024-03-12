package ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.listados;

import ar.edu.utn.frba.dds.domain.services.fusioncomunidad.moldes.Comunidad;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListadoDeFusion {
	@SerializedName("mergedCommunity")
	public List<Comunidad> fusion;

	public ListadoDeFusion() {
		this.fusion = new ArrayList<>();
	}
}
