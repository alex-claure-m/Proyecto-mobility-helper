package ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail;

public class NoSeEnvioMailException extends RuntimeException {
  public NoSeEnvioMailException(String s) {
    super(s);
  }
}
