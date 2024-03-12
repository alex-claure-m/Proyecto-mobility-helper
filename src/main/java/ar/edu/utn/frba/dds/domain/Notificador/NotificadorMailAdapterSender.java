package ar.edu.utn.frba.dds.domain.Notificador;

import ar.edu.utn.frba.dds.domain.Notificador.JavaMail.JavaMailSender;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificadorMailAdapterSender implements NotificacionAdapter{
  private JavaMailSender javaMailSender;

  private String aQuien;
  private String subject;


  public NotificadorMailAdapterSender(JavaMailSender javaMailSender, String paraQuien, String subject) {
    this.javaMailSender = javaMailSender;
    this.aQuien = paraQuien;
    this.subject = subject; //asunto del mail
  }

  @Override
  public void notificar(String destinatario, String subject, String texto){
    this.javaMailSender.enviarMail(this.javaMailSender.construirMensajeSoloTexto(destinatario,subject,texto));
    this.javaMailSender.cerrarSesion();
  }

}
