package ar.edu.utn.frba.dds.domain.services.georef;

public class ApiGeoRefException extends Throwable {
  public ApiGeoRefException(String motivo) {
    super("la Conexion con GeoRef fallo debido a: " + motivo);
  }
}
