package ar.edu.utn.frba.dds.domain.cargadores;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Cargador genérico, implementa lógica común al cargador de
// entidades prestadoras y al cargador de organismos de control.

public class CargadorDeDatosDesdeCSV {
    // este método parsea el CSV completo y va creando las instancias con los datos leídos
    public static List<String[]> obtenerDatosDeCSV(String path) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<String[]> datosCsv = null;
        try {
            datosCsv = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        //List<E> instancias = new ArrayList<>();
        //datosCsv.forEach(datosFila -> instancias.add(new (datosFila)));

        return datosCsv;
    }
}
