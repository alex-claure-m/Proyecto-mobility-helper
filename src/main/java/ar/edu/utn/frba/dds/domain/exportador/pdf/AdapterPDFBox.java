package ar.edu.utn.frba.dds.domain.exportador.pdf;

import ar.edu.utn.frba.dds.domain.informes.InformeDeRankings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

// esta clase es una modificación de:
// https://github.com/dds-utn/modulo-exportador/blob/master/src/main/java/estrategias/exportacion/pdf/AdapterApachePDFBox.java

public class AdapterPDFBox implements AdapterExportarComoPDF {

    public String exportar(InformeDeRankings informe) {
        String rutaDelArchivo = informe.getNombre() + ".pdf";

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try {
            PDPageContentStream pageContent = new PDPageContentStream(document, page);

            pageContent.beginText();
            pageContent.setFont(PDType1Font.COURIER, 12);
            pageContent.setLeading(14.5f);
            pageContent.newLineAtOffset(25, 700);

            for (String dato : informe.getDatos()) {
                switch (dato) {
                    case "/n":
                        pageContent.newLine();
                        break;
                    default:
                        pageContent.showText(dato);
                        break;
                }
            }

            pageContent.endText();
            pageContent.close();
            document.save(rutaDelArchivo);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rutaDelArchivo;
    }
}