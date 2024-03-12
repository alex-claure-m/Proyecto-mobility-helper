package ar.edu.utn.frba.dds.CargadoresTest;

import ar.edu.utn.frba.dds.domain.EntidadPrestadora;
import ar.edu.utn.frba.dds.domain.OrganismoDeControl;
import ar.edu.utn.frba.dds.domain.cargadores.CargadorDeDatosDesdeCSV;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class CargadorDeDatosDesdeCSVTest {
    @Test
    public void seCarganLasEntidadesPrestadoras() {
        String path = "src/test/java/ar/edu/utn/frba/dds/CargadoresTest/EntidadesPrestadoras.csv";
        List<String[]> datosCsv = CargadorDeDatosDesdeCSV.obtenerDatosDeCSV(path);

        List<EntidadPrestadora> instancias = new ArrayList<>();
        datosCsv.forEach(datosFila -> instancias.add(new EntidadPrestadora(datosFila)));

        Assertions.assertEquals("Trenes Argentinos", datosCsv.get(1)[0]);
        Assertions.assertEquals("Santander Río", datosCsv.get(2)[0]);
    }

    @Test
    public void seCarganLosOrganismosDeControl() {
        String path = "src/test/java/ar/edu/utn/frba/dds/CargadoresTest/OrganismosDeControl.csv";
        List<String[]> datosCsv = CargadorDeDatosDesdeCSV.obtenerDatosDeCSV(path);

        List<OrganismoDeControl> instancias = new ArrayList<>();
        datosCsv.forEach(datosFila -> instancias.add(new OrganismoDeControl(datosFila)));

        Assertions.assertEquals("CNRT", datosCsv.get(1)[1]);
        Assertions.assertEquals("Banco Central de la República Argentina", datosCsv.get(2)[0]);
    }
}
