package ar.edu.utn.frba.dds.domain.Notificador;

import ar.edu.utn.frba.dds.domain.Notificador.TwilioSender.TwilioSender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificadorWhatsappAdapterSender implements NotificacionAdapter {

  private TwilioSender twilioSender;


  @Override
  public void notificar(String destinatario, String asunto, String texto) {
    this.twilioSender.envioMensajeWhatsapp(destinatario,asunto,texto);
  }
}