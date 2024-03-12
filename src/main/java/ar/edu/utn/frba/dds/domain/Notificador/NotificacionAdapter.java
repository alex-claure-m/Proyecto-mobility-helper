package ar.edu.utn.frba.dds.domain.Notificador;

public interface NotificacionAdapter {
  //para cada adapter
  public void notificar(String destinatario,String asunto, String texto);
}
