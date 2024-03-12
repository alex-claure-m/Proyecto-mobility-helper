package ar.edu.utn.frba.dds.NotifiacionesTest;

import ar.edu.utn.frba.dds.domain.Notificador.TwilioSender.TwilioSender;
import org.junit.jupiter.api.Test;

public class TwilioTest {
*/
  @Test
  public void envioMensaje(){
    // es un testeo forzado por qeu bueno, que tan practico es pasarle la sid y el token de esa forma? hjaja
    TwilioSender unaNoti = new TwilioSender(b,a);
    unaNoti.setNumeroTelefonicoPersonaAEnviar("5491154201847");
    unaNoti.setAsunto("llamado de emergencia 911!!");
    unaNoti.inicializarCuenta();
    unaNoti.envioMensajeWhatsapp(unaNoti.getNumeroTelefonicoPersonaAEnviar(),unaNoti.getAsunto(),"que tendra el ptetizo!!");
    System.out.println("Envio por las dudas");
  }
*/
}
