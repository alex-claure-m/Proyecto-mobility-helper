package ar.edu.utn.frba.dds.domain.Notificador.TwilioSender;

import ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesWhatsapp.ExcepcionDeAutenticacion;
import ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesWhatsapp.ExcepcionMensajeVacio;
import com.twilio.Twilio;
import com.twilio.exception.AuthenticationException;
import com.twilio.exception.InvalidRequestException;
import com.twilio.rest.api.v2010.account.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TwilioSender {

  // TODO
  //arreglar algunos parametros para el tema de MOCKITO

  private String ACCOUNT_SID;
  private String AUTH_TOKEN;

  private String numeroTelefonicoPersonaAEnviar;
  // 5491154201847
  private String asunto;

  public static final String numeroServicio= "whatsapp:+14155238886";


  public TwilioSender(String ACCOUNT_SID, String AUTH_TOKEN) {
    this.ACCOUNT_SID = ACCOUNT_SID;
    this.AUTH_TOKEN = AUTH_TOKEN;
  }



  public void inicializarCuenta(){
    try{
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    catch(AuthenticationException e){
      throw new ExcepcionDeAutenticacion("Error a la hora de  autenticar, revise sus credenciales");
    }
  }
  public void envioMensajeWhatsapp(String numeroTelefonicoPersonaAEnviar,String asunto,String descripcionMensaje) {
    try{
      this.inicializarCuenta();
      Message message = Message.creator(
              new com.twilio.type.PhoneNumber("whatsapp:"+"+"+this.getNumeroTelefonicoPersonaAEnviar()),
              new com.twilio.type.PhoneNumber(numeroServicio),
              this.getAsunto()+" : " +descripcionMensaje)
          .create();

    }catch(InvalidRequestException e){
      throw new ExcepcionMensajeVacio("Error al enviar Mensaje, revise");
    }


  }
  /*
  public static void main(String[] args) {
  Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
            new com.twilio.type.PhoneNumber("whatsapp:+5491154201847"),
            new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
            "Prueba 3 de envio mensajes, cada mensaje cuesta $0.01 -> al equivalente a " +
                "5 pesos !")
        .create();

    System.out.println(message.getSid());
  }
  */
}
