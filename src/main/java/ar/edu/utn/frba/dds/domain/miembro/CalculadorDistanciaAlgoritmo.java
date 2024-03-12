package ar.edu.utn.frba.dds.domain.miembro;

import ar.edu.utn.frba.dds.domain.incidentes.Incidente;

import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public class CalculadorDistanciaAlgoritmo implements CalculadorDistancia{

  @Override
  public double calculoDistanciaEntreDosParesCoordenadas(Ubicacion ubicacionMiembro, Incidente localizacionIncidente) {
    double startLat = ubicacionMiembro.getLat();
    double startLong = ubicacionMiembro.getLongitud();
    double endLat = localizacionIncidente.obtenerLocalizacionDeLaPrestacion().getCoordenadas().getLat();
    double endLong = localizacionIncidente.obtenerLocalizacionDeLaPrestacion().getCoordenadas().getLon();

    double toRad = 0.0174532925;
    double dLat = (endLat - startLat) * toRad;
    double dLon = (endLong - startLong) * toRad;
    double a = sin(dLat / 2) * sin(dLat / 2) + cos(startLat * toRad) * cos(endLat * toRad) * sin(dLon / 2) * sin(dLon / 2);
    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
    return 6371 * c;
  }

  @Override
  public Boolean estaProximaA(double unaDistanciaAproxi, Ubicacion ubicacionMiembro, Incidente unIncidente) {
    double distanciaEntreLosPares = this.calculoDistanciaEntreDosParesCoordenadas(ubicacionMiembro, unIncidente);
    return unaDistanciaAproxi < distanciaEntreLosPares;
  }

}
