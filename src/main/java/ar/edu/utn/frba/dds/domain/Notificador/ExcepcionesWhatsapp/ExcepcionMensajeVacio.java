package ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesWhatsapp;

public class ExcepcionMensajeVacio extends RuntimeException {
  public ExcepcionMensajeVacio(String s) {
    super(s);
  }
}
