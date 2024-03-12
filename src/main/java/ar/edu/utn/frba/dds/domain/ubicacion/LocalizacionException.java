package ar.edu.utn.frba.dds.domain.ubicacion;

public class LocalizacionException extends Throwable {
  public LocalizacionException(String motivo) {
    super("Localizacion: "+ motivo);
  }
}
