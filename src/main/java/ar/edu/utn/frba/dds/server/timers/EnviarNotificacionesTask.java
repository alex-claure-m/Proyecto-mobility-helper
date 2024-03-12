package ar.edu.utn.frba.dds.server.timers;

import ar.edu.utn.frba.dds.controllers.NotificacionesController;
import ar.edu.utn.frba.dds.domain.miembro.Notificacion;

import java.util.List;
import java.util.TimerTask;

public class EnviarNotificacionesTask extends TimerTask {
	@Override
	public void run() {
		//obtenemos todas las notificaciones
		List<Notificacion> notificaciones = NotificacionesController.getRepositorio().buscarTodos();

		//buscamos aquellas que puedan ser enviadas
		List<Notificacion> notificacionesAEnviar = notificaciones.stream()
				.filter(Notificacion::puedeEnviarse).toList();

		//enviarlas y marcarlas como enviadas
		notificacionesAEnviar.forEach(noti -> {
			noti.enviarAlMiembro();
			noti.setFueEnviado(Boolean.TRUE);
			NotificacionesController.getRepositorio().modificar(noti);
		});
	}
}
