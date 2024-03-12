package ar.edu.utn.frba.dds.domain.Notificador.JavaMail;


//los hots de cada servicio de correos
// son enum por que a lo mejor ya no quiero usar gmail y quiero usar no se
// yahoo correos
public enum MailHost {
  GMAIL("smtp.gmail.com");
  private String host;

  MailHost(String host) {
    this.host = host;
  }

  public String getHost() {
    return host;
  }
}