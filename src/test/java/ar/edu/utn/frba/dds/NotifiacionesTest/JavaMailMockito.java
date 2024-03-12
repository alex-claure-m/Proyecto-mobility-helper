package ar.edu.utn.frba.dds.NotifiacionesTest;

import ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail.NoSeCreoMailException;
import ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail.NoSeEnvioMailException;
import ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail.NoSePudoCerrarLaSesiónException;
import ar.edu.utn.frba.dds.domain.Notificador.JavaMail.JavaMailSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JavaMailMockito {

  @Test
  public void NoSePuedeEneivarMensajeMalConstruidi(){
    JavaMailSender javaMail = mock(JavaMailSender.class);
    when(javaMail.construirMensajeSoloTexto(anyString(),anyString(),anyString())).thenThrow(NoSeCreoMailException.class);
    Assertions.assertThrows(NoSeCreoMailException.class,
        () -> javaMail.construirMensajeSoloTexto("gdfsg","fdsaf","fdsafsadf"));
  }

  @Test
  public void ErrorEnDestinatarioInvalidoNoEnviaMensaje() {
    JavaMailSender javaMailSender = mock(JavaMailSender.class);
    doThrow(NoSeEnvioMailException.class).when(javaMailSender).enviarMail(any());
    Assertions.assertThrows(NoSeEnvioMailException.class,
        () -> javaMailSender.enviarMail(any()));
  }

  @Test
  public void SesionNoIniciadaNoSePuedeCerrar() {
    JavaMailSender javaMailSender = mock(JavaMailSender.class);
    doThrow(NoSePudoCerrarLaSesiónException.class).when(javaMailSender).cerrarSesion();
    Assertions.assertThrows(NoSePudoCerrarLaSesiónException.class,
        () -> javaMailSender.cerrarSesion());
  }


}
