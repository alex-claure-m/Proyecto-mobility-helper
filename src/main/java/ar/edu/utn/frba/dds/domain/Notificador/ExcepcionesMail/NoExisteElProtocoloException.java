package ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail;

public class NoExisteElProtocoloException extends RuntimeException {
  public NoExisteElProtocoloException(String s) {
    super(s);
  }
}
