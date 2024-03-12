package ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesWhatsapp;

public class ExcepcionDeAutenticacion extends RuntimeException {
  public ExcepcionDeAutenticacion(String s) {
    super(s);

  }
}
