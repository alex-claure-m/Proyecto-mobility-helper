package ar.edu.utn.frba.dds.domain.services.georef;

import ar.edu.utn.frba.dds.domain.services.georef.entities.ListadoDepartamentos;
import ar.edu.utn.frba.dds.domain.services.georef.entities.ListadoMunicipios;
import ar.edu.utn.frba.dds.domain.services.georef.entities.ListadoProvincias;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeorefService {
  // aca es donde voy a las posibles llamadas con ruta RELATIVAS (NO ABSOLUTAS) que voy a hacer
  // https://apis.datos.gob.ar/georef/api/

  // DATAZO
  /*
  * EL USO DE QUERY Y PATH DEPENDE DE COMO ESTE DISEÑADA LA API
  * si es PATH, solo reemplaza en la URL xxxxx / {algo para reemplazar}/ xxxxx
  * si es QUERY, se sobre entiende que en la url ira nombre/nombre=?id...  -- se maneja por CLAVE-VALOR
  *   entonces en la query no tengo que extender la ANOTACION @GET del https*/

  //DATAZO2
  /*
  * lo que le paso en la QUERY/PATH, tiene que COINCIDIR con lo que espera en la API
  * no le puedo mandar id_municipio , si justamente la API no espera eso
  * la API , en este caso espera ../?ID=xxxx, entonces yo debo decirle QUERT(ID) int id --
  * */

  @GET("provincias")
  Call<ListadoProvincias> provincias();

// para el uso de provincia-por-id
  @GET("provincias")
  Call<ListadoProvincias> provincias(@Query("id") int id);

  @GET("provincias")
  Call<ListadoProvincias> provinciasSueltas(@Query("id") int id);


  @GET("provincias")
  Call<ListadoProvincias> provincias(@Query("nombre") String nombre);

  @GET("municipios")
  Call<ListadoMunicipios> municipios();

  @GET("municipios")
  Call<ListadoMunicipios> municipios(@Query("nombre") String nombre, @Query("provincia") String provincia);

  @GET("municipios")
  Call<ListadoMunicipios> municipios(@Query("id") int id);

  @GET("municipios")
  Call<ListadoMunicipios> municipios(@Query("nombre") String nombre);

  /*
  @GET("municipios")
  Call<ListadoMunicipios> municipios(@Query("provincia") int id);
  */

  @GET("departamentos")
  Call<ListadoDepartamentos> departamentos();

  @GET("departamentos")
  Call<ListadoDepartamentos> departamentos(@Query("id") int id);



}
