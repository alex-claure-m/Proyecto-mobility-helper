package ar.edu.utn.frba.dds.domain.miembro;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.domain.incidentes.Incidente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/*ESTOY CAMBIANDO LOS TIPOS DE VALORES DEBIDO A QUE POSTGRESQL NO LO TOMA
* EN VEZ DE LONGTEXT -> TEXT
* EN VEZ DE DATETIME -> TIMESTAMP */
@Entity
@Table(name = "notificacion")
@Getter
@Setter
public class Notificacion extends Persistence {
	@Column(name = "texto", columnDefinition = "text")
	private String texto = "";
	@Column(name = "fueLeido")
	private Boolean fueLeido = Boolean.FALSE;
	@Column(name = "fueEnviado")
	private Boolean fueEnviado = Boolean.FALSE;
	@Column(name="fechaHora"/*, columnDefinition = "TIMESTAMP"*/)
	private LocalDateTime fechaHora = LocalDateTime.now();
	@ManyToOne
	private Miembro miembroReceptor; //
	@ManyToOne
	private Incidente incidenteReportado;

	public void notificarAlMiembro() {
		/*estas notificaciones se muestran siempre en el sistema,
		* después se verá si se envían o no en el momento*/
		this.miembroReceptor.getNotificacionesRecibidas().add(this);

		if(miembroReceptor.getFormaNotifi() == FormaNotificacion.CUANDO_SUCEDEN)
			this.enviarAlMiembro(); //enviar ahora
	}

	public Boolean puedeEnviarse() {
		if(this.fueEnviado)
			return Boolean.FALSE;

		if(incidenteReportado != null) { //solo para notificaciones de incidentes
			LocalTime now = LocalTime.now();
			LocalTime horarioNotificacion = miembroReceptor.getHorarioNotificacion();
			if(miembroReceptor.getFormaNotifi() == FormaNotificacion.SIN_APUROS) {
				if(now.isBefore(horarioNotificacion) || now.isAfter(horarioNotificacion.plusMinutes(2)))
					return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	public void enviarAlMiembro() { //este método debería estar acá o en un controller??
		//Nota: el seteo de 'fueEnviado' se hace desde afuera
		//TO-DO: implementar el envío en sí..
		if (miembroReceptor.getNotificarVia() == NotificarVia.ENVIAR_POR_EMAIL) {
			;
		} else if (miembroReceptor.getNotificarVia() == NotificarVia.ENVIAR_POR_TELEFONO) {
			if (miembroReceptor.getNroWhatsapp() != null) {
				;
			}
		}
	}

	public String fechaHoraMostrada() {
		return this.fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/uuuu' a las 'HH:mm:ss"));
	}
}
