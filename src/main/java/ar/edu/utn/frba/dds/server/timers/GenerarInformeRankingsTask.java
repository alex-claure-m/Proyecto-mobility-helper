package ar.edu.utn.frba.dds.server.timers;

import ar.edu.utn.frba.dds.controllers.EntidadesController;
import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.exportador.ExportadorDeInformes;
import ar.edu.utn.frba.dds.domain.exportador.pdf.AdapterPDFBox;
import ar.edu.utn.frba.dds.domain.informes.GeneradorDeInformesDeRankings;
import ar.edu.utn.frba.dds.domain.informes.InformeDeRankings;
import ar.edu.utn.frba.dds.domain.rankings.Ranking;
import ar.edu.utn.frba.dds.domain.rankings.RankingCantidadDeIncidentesReportados;
import ar.edu.utn.frba.dds.domain.rankings.RankingTiempoPromedioCierreIncidentes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

public class GenerarInformeRankingsTask extends TimerTask {
	@Override
	public void run() {
		List<Entidad> entidades = EntidadesController.getRepositorio().buscarTodos();

		if(entidades.isEmpty())
			return;

		//instanciamos, seteamos y generamos el primer ranking
		RankingTiempoPromedioCierreIncidentes ranking1;
		ranking1 = new RankingTiempoPromedioCierreIncidentes(entidades);

		//instanciamos, seteamos y generamos el segundo ranking
		RankingCantidadDeIncidentesReportados ranking2;
		ranking2 = new RankingCantidadDeIncidentesReportados(entidades);

		//agregamos los rankings a una lista para generar el informe
		List<Ranking> rankings = new ArrayList<Ranking>();
		Collections.addAll(rankings, ranking1, ranking2);

		InformeDeRankings informe = GeneradorDeInformesDeRankings.generar(rankings);
		ExportadorDeInformes exportador = new ExportadorDeInformes(new AdapterPDFBox());

		exportador.exportar(informe);
	}
}
