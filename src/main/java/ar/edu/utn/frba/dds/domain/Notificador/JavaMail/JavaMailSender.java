package ar.edu.utn.frba.dds.domain.Notificador.JavaMail;



//import ar.edu.utn.frba.dds.Notificador.ExcepcionesMail.*;
import ar.edu.utn.frba.dds.domain.Notificador.ExcepcionesMail.*;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class JavaMailSender {
  private Properties properties = new Properties();
  private MailHost operation;
  private Session session;
  private Transport transport;

  //constructor - username, pass, mailhost Gmail, en este caso
  public JavaMailSender(String username, String password, MailHost operation) {
    this.operation = operation;
    this.initProperties(username);
    this.session = Session.getDefaultInstance(this.properties);
    this.initTransport(password);
  }


  private void initProperties(String username) {
    this.properties.put("mail.smtp.host", this.operation.getHost());
    this.properties.setProperty("mail.smtp.starttls.enable", "true");
    this.properties.setProperty("mail.smtp.port", "587");
    this.properties.setProperty("mail.smtp.user", username);
    this.properties.setProperty("mail.smtp.auth", "true");
  }

  //inicializo la sesion , con mi user and pass!
  private void initTransport(String password) {
    try {
      this.transport = this.session.getTransport("smtp");
      try {
        this.transport.connect(this.properties.getProperty("mail.smtp.user"), password);
      } catch (MessagingException e) {
        throw new NoSeConectoAlUsuarioException("Error al realizar login. " + e.getMessage());
      }
    } catch (NoSuchProviderException e) {
      throw new NoExisteElProtocoloException("El protocolo requerido no existe.");
    }
  }

// para construir el mensaje a enviar
  public MimeMessage construirMensajeSoloTexto(String toEmail, String subject, String texto) {
    MimeMessage message = new MimeMessage(this.session);
    try {
      message.setFrom(properties.getProperty("mail.smtp.user"));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
      message.setSubject(subject);
      message.setText(texto);
    } catch (MessagingException e) {
      throw new NoSeCreoMailException("Error en construcción del mail solo texto. "
          + e.getMessage());
    }
    return message;
  }

  // para enviar mensajes
  public void enviarMail(MimeMessage message) {
    try {
      this.transport.sendMessage(message, message.getAllRecipients());
    } catch (MessagingException e) {
      throw new NoSeEnvioMailException("No se pudo enviar el mensaje desde" +
          " -> " + session.getProperty("mail.smtp.user"));
    }
  }

  public void cerrarSesion() {
    try {
      this.transport.close();
    } catch (MessagingException e) {
      throw new NoSePudoCerrarLaSesiónException("Error al intentar cerrar la sesión.");
    }
  }
}
