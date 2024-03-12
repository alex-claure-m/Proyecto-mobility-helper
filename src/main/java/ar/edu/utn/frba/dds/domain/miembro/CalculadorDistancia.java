package ar.edu.utn.frba.dds.domain.miembro;

import ar.edu.utn.frba.dds.domain.incidentes.Incidente;

public interface CalculadorDistancia {
  double calculoDistanciaEntreDosParesCoordenadas(Ubicacion ubicacionMiembro, Incidente unIncidente);

  Boolean estaProximaA(double unaDistanciaAproxi,Ubicacion ubicacionMiembro, Incidente unIncidente);
}
