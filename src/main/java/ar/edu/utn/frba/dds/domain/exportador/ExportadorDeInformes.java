package ar.edu.utn.frba.dds.domain.exportador;

import ar.edu.utn.frba.dds.domain.exportador.pdf.AdapterExportarComoPDF;
import ar.edu.utn.frba.dds.domain.informes.InformeDeRankings;

// Esta clase en realidad sólo exporta a formato PDF,
// por lo que debería llamarse 'ExportadorDeInformesComoPDF'

public class ExportadorDeInformes {
    // usamos un Adapter por si luego queremos cambiar de biblioteca fácilmente.
    private AdapterExportarComoPDF adapter;

    public ExportadorDeInformes(AdapterExportarComoPDF adapter) {
        this.adapter = adapter;
    }

    //hace la exportación y devuelve la ruta del archivo
    public String exportar(InformeDeRankings informe) {
        return this.adapter.exportar(informe);
    }
}