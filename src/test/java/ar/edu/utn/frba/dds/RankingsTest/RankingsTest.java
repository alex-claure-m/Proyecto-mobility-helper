package ar.edu.utn.frba.dds.RankingsTest;

import ar.edu.utn.frba.dds.domain.entidad.Entidad;
import ar.edu.utn.frba.dds.domain.establecimientos.Establecimiento;
import ar.edu.utn.frba.dds.domain.establecimientos.Sucursal;
import ar.edu.utn.frba.dds.domain.exportador.ExportadorDeInformes;
import ar.edu.utn.frba.dds.domain.exportador.pdf.AdapterPDFBox;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import ar.edu.utn.frba.dds.domain.informes.GeneradorDeInformesDeRankings;
import ar.edu.utn.frba.dds.domain.informes.InformeDeRankings;
import ar.edu.utn.frba.dds.domain.rankings.Ranking;
import ar.edu.utn.frba.dds.domain.rankings.RankingCantidadDeIncidentesReportados;
import ar.edu.utn.frba.dds.domain.rankings.RankingGradoDeImpactoDeLosIncidentes;
import ar.edu.utn.frba.dds.domain.rankings.RankingTiempoPromedioCierreIncidentes;
import ar.edu.utn.frba.dds.domain.servicios.PrestacionServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingsTest {
    //todas las entidades del sistema estarán acá
    List<Entidad> entidades = new ArrayList<>();
    LocalDateTime hoy = LocalDateTime.now();

    Entidad lineaBdeSubte = new Entidad("Línea B de Subte");
    Establecimiento estacionMedrano = new Sucursal("Estación Medrano");
    PrestacionServicio banios = new PrestacionServicio();

    Entidad bancoSantander = new Entidad("Banco Santander Río");
    Establecimiento sucursalAlmagro = new Sucursal("Sucursal Almagro");
    PrestacionServicio ascensor = new PrestacionServicio();
    PrestacionServicio escalera = new PrestacionServicio();

    Entidad bancoNacion = new Entidad("Banco Nación");
    Establecimiento sucursalBariloche = new Sucursal("Sucursal Bariloche");
    PrestacionServicio puertaFrontal = new PrestacionServicio();

    //incidentes que pueden haber
    Incidente incidenteAscensor, incidenteEscalera1, incidentePuertaFrontal,
            incidenteBanios, incidenteEscalera2;

    @BeforeEach
    public void init() {
        entidades.add(bancoSantander);
        entidades.add(bancoNacion);
        entidades.add(lineaBdeSubte);

        incidenteAscensor = new Incidente("","", ascensor);
        incidenteEscalera1 = new Incidente("","", escalera);
        incidentePuertaFrontal = new Incidente("","", puertaFrontal);
        incidenteBanios = new Incidente("","", banios);
        incidenteEscalera2 = new Incidente("","", escalera);

        //lineaBdeSubte - estacionMedrano - banios
        lineaBdeSubte.agregarEstablecimiento(estacionMedrano); //la lineaBdeSubte tendrá a la estacionMedrano
        estacionMedrano.agregarPrestacion(banios); //la estacionMedrano ofrecerá baniosEstacioMedrano
        banios.agregarIncidente(incidenteBanios); //creamos un incidente para baniosEstacioMedrano

        //bancoSantander - sucursalAlmagro - ascensor y escalera
        bancoSantander.agregarEstablecimiento(sucursalAlmagro);
        sucursalAlmagro.agregarPrestacion(ascensor);
        sucursalAlmagro.agregarPrestacion(escalera);
        ascensor.agregarIncidente(incidenteAscensor);
        escalera.agregarIncidente(incidenteEscalera1);
        escalera.agregarIncidente(incidenteEscalera2); //este no se cerrará

        //bancoNacion - sucursalBariloche - puertaFrontal
        bancoNacion.agregarEstablecimiento(sucursalBariloche);
        sucursalBariloche.agregarPrestacion(puertaFrontal);
        puertaFrontal.agregarIncidente(incidentePuertaFrontal);

        //vamos a simular los cierres para que tenga sentido el ranking
        incidenteAscensor.setFechaCierre(hoy.plusHours(10)); //este se cerró en 10 horas
        incidenteEscalera1.setFechaCierre(hoy.plusHours(40)); //este se cerró mucho después de 24hs
        incidentePuertaFrontal.setFechaCierre(hoy.plusHours(24)); //este se cerro justo en un día
        incidenteBanios.setFechaCierre(hoy.plusHours(48)); //este se cerró en 2 días

        /*
        bancoNacion                         bancoSantander                          lineaBdeSubte
          sucursalBariloche                   sucursalAlmagro                         estacionMedrano
            puertaFrontal                       ascensor                                banios
              incidentePuertaFrontal (24hs)       incidenteAscensor (10hs)                incidenteBanios (48hs)
                                                 escalera
                                                   incidenteEscalera1 (40hs)
                                                   incidenteEscalera2 (no cerrado)
         */
    }

    @Test
    public void testTiempoPromedioCierreDeIncidentes() {
        //la entidad bancoSantander tiene 2 incidentes cerrados y el tiempo total es 50 horas
        Assertions.assertEquals(25, bancoSantander.tiempoPromedioCierreDeIncidentes());
        Assertions.assertEquals(24, bancoNacion.tiempoPromedioCierreDeIncidentes());
        Assertions.assertEquals(48, lineaBdeSubte.tiempoPromedioCierreDeIncidentes());
    }

    @Test
    public void testDelPrimerRanking() {
        RankingTiempoPromedioCierreIncidentes ranking;
        ranking = new RankingTiempoPromedioCierreIncidentes(entidades);

        //la entidad Linea B de Subte tendría que ser la que más
        //tiempo promedio de cierres de incidentes tuvo (2 días).
        Assertions.assertEquals(lineaBdeSubte, ranking.getDato(0));
        Assertions.assertEquals(bancoSantander, ranking.getDato(1));
        Assertions.assertEquals(bancoNacion, ranking.getDato(2));
    }

    @Test
    void testCantidadDeIncidentesReportados() {
        //en este test, instanciamos más incidentes para la entidad bancoNacion
        Incidente incidentePuertaFrontal2 = new Incidente("", "", puertaFrontal);
        Incidente incidentePuertaFrontal3 = new Incidente("", "", puertaFrontal);
        Incidente incidentePuertaFrontal4 = new Incidente("", "", puertaFrontal);

        incidentePuertaFrontal2.setFechaApertura(hoy.plusHours(30));
        incidentePuertaFrontal2.setFechaCierre(hoy.plusHours(48));
        puertaFrontal.agregarIncidente(incidentePuertaFrontal2);

        incidentePuertaFrontal3.setFechaApertura(hoy.plusHours(35));
        puertaFrontal.agregarIncidente(incidentePuertaFrontal3);

        incidentePuertaFrontal4.setFechaApertura(hoy.plusHours(60));
        puertaFrontal.agregarIncidente(incidentePuertaFrontal4);

        //hasta acá 'puertaFrontal' tendría:
        /*
          incidente1 (abierto a las 0hs - CERRADO)
          incidente2 (abierto a las 30hs - CERRADO)
          incidente3 (abierto a las 35hs - NO CERRADO)
          incidente4 (abierto a las 60hs - NO CERRADO)
          deberían contarse como tres incidentes reportados
         */

        Assertions.assertEquals(2, bancoSantander.cantidadDeIncidentesReportados());
        Assertions.assertEquals(1, lineaBdeSubte.cantidadDeIncidentesReportados());
        Assertions.assertEquals(3, bancoNacion.cantidadDeIncidentesReportados());
    }

    @Test
    public void testDelSegundoRanking() {
        RankingCantidadDeIncidentesReportados ranking;
        ranking = new RankingCantidadDeIncidentesReportados(entidades);

        //este es el que más incidentes reportados tiene (dos, uno que cerró y otro que no)
        Assertions.assertEquals(bancoSantander, ranking.getDato(0));
    }


    @Test
    public void testDelTercerRanking() {
        RankingGradoDeImpactoDeLosIncidentes ranking;
        ranking = new RankingGradoDeImpactoDeLosIncidentes(entidades);

        /*
        gradoDeImpacto = Sumatoria( t de resolución ) + Cant. Incidentes No Resueltos * CNF

        bancoSantander: 50 + 1 * CNF
        lineaBdeSubte: 48 + 0 * CNF
        bancoNacion: 24 + 0 * CNF
         */

        Assertions.assertEquals(bancoSantander, ranking.getDato(0));
        Assertions.assertEquals(lineaBdeSubte, ranking.getDato(1));
        Assertions.assertEquals(bancoNacion, ranking.getDato(2));
    }


    @Test
    public void testGenerarInformeDeRankings() {
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

        Assertions.assertEquals("informeSemanal.pdf", exportador.exportar(informe));
    }
}
