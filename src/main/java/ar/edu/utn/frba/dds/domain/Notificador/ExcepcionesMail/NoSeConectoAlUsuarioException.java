package ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail;

public class NoSeConectoAlUsuarioException extends RuntimeException {
  public NoSeConectoAlUsuarioException(String s) {
    super(s);
  }
}
