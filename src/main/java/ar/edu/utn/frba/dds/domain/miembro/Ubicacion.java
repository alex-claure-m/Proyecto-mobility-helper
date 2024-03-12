package ar.edu.utn.frba.dds.domain.miembro;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

@Getter
@Setter
public class Ubicacion {

  private String direccion;

  private double lat;
  @SerializedName("long")
  private double longitud;


  public Ubicacion(double lat, double longitud) {
    this.lat = lat;
    this.longitud = longitud;
  }

  // esta funcion es para calcular la distancia global de 2 latitudes y 2 longitudes
  // que en realidad deberia ser una interfaz ...
  // lo que hare aca es pasarle lat y long de Localizacion y comparo con la Ubicacion
  // devolvera una distancia en km
  // con esto yo debo comparar con una distancia setteada para que se active el hecho para notificar
  public static double distance(double startLat, double startLong, double endLat, double endLong) {
    double toRad = 0.0174532925;
    double dLat = (endLat - startLat) * toRad;
    double dLon = (endLong - startLong) * toRad;
    double a = sin(dLat / 2) * sin(dLat / 2) + cos(startLat * toRad) * cos(endLat * toRad) * sin(dLon / 2) * sin(dLon / 2);
    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
    return 6371 * c;
  }
}
