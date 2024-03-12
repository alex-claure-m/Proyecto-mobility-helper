package ar.edu.utn.frba.dds.domain.usuarios.validarPassFile;

public class ArchivoNoEncontradoException extends RuntimeException {
  public ArchivoNoEncontradoException(String motivo) {
    super("El Archivo" + motivo);
  }
}
