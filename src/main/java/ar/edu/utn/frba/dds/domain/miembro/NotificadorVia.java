package ar.edu.utn.frba.dds.domain.miembro;

public enum NotificadorVia {
  EMAIL("email"),
  WHATSAPP("whatsapp");

  private final String estadoFinal;

   NotificadorVia(String estadoFinal){
    this.estadoFinal = estadoFinal;
  }
  public String getEstado(){
     return estadoFinal;
  }
}
