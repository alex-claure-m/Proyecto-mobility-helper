package ar.edu.utn.frba.dds.domain.usuarios.validarPassFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class VerificarPassOpenFile implements VerificarPassAdapter{

  @Override
  public void verificarPassDesdeFile(String unPass) {
    try{
      File f = new File("passwordsTop10k.txt");
      Scanner s = new Scanner(f);
      while (s.hasNextLine()) {
        String line = s.nextLine();
        if(line.equals(unPass)){
          throw new ContraseniaInvalidaException("es demasiado debil");
        }
      }
    } catch (FileNotFoundException e) {
      throw new ArchivoNoEncontradoException("no existe o no fue encontrado");
    }
  }
}
