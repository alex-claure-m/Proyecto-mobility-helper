package ar.edu.utn.frba.dds.domain.exportador.pdf;

import ar.edu.utn.frba.dds.domain.informes.InformeDeRankings;

public interface AdapterExportarComoPDF {
    public abstract String exportar(InformeDeRankings informe);
}