package ar.edu.utn.frba.dds.domain.usuarios.validarPassFile;

public class ContraseniaInvalidaException extends RuntimeException {
  public ContraseniaInvalidaException(String motivo) {
    super("la Contraseña "+ motivo);
  }
}
