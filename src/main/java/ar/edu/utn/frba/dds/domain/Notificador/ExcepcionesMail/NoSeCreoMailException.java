package ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail;

public class NoSeCreoMailException extends RuntimeException {
  public NoSeCreoMailException(String s) {
    super(s);
  }
}
