package ar.edu.utn.frba.dds.domain.services.georef;

import ar.edu.utn.frba.dds.domain.services.georef.entities.ListadoDepartamentos;
import ar.edu.utn.frba.dds.domain.services.georef.entities.ListadoMunicipios;
import ar.edu.utn.frba.dds.domain.services.georef.entities.ListadoProvincias;
import ar.edu.utn.frba.dds.domain.services.georef.entities.Municipio;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;


public class ServicioGeoref {
  private static ServicioGeoref instance = null; // sera el uso de patron builder o singleton
  private static final String urlAPI = "https://apis.datos.gob.ar/georef/api/";
  // EL FINAL hara q se comportara como una Constante y no como un atributo

  private Retrofit retrofit;

  private ServicioGeoref() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlAPI)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  //constructor de singleton
  public static ServicioGeoref getInstance() {
    if (instance == null) {
      instance = new ServicioGeoref();
    }
    return instance;
  }

  /* ********************** PROVINCIA **************************** */
  //metodo para que haga uso de la interfaz GeorefService
  public ListadoProvincias listadoDeProvincias() throws IOException {
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoProvincias> requestProvinciasArg = georefservice.provincias();
    //aca ejecuto el geoRef para que me traiga la query que le estoy pasando
    Response<ListadoProvincias> responseProvinciasArgs = requestProvinciasArg.execute();
    return responseProvinciasArgs.body();
  }


// OK ES EL UNICO QUE CAMBIARE DEBIDO A QUE NO SE SI ESTA BIEN APLICARLO DE ESTA FORMA
  // O ES MEJOR DEJARLO COMO INT y reconvertirlo con INTERGER.PARSEINT(xxxx);
  public ListadoProvincias listadoProvinciaPorId(String id) throws ApiGeoRefException {
    try{
      int id_provincia = Integer.parseInt(id);
      GeorefService georefservice = this.retrofit.create(GeorefService.class);
      Call<ListadoProvincias> requestProvinciasArg = georefservice.provincias(id_provincia);
      //aca ejecuto el geoRef para que me traiga la query que le estoy pasando
      Response<ListadoProvincias> responseProvinciasArgs = requestProvinciasArg.execute();
      return responseProvinciasArgs.body();
    }catch(NumberFormatException e) {
        throw new ApiGeoRefException("El ID debe ser Numero Entero");
      }catch(IOException e){
        throw new ApiGeoRefException("No se pudo Conectar");
    }
  }


  /* ***************************** MUNICIPIO ****************************** */
  public ListadoMunicipios listadoDeMunicipios() throws IOException {
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoMunicipios> requestMunicipiosArg = georefservice.municipios();
    //aca ejecuto el geoRef para que me traiga la query que le estoy pasando
    Response<ListadoMunicipios> responseMunicipiosArgs = requestMunicipiosArg.execute();
    return responseMunicipiosArgs.body();

  }
  public ListadoMunicipios listadoDeMunicipiosPorNombre(String nombreMunicipio) throws IOException {
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoMunicipios> requestMunicipiosArg = georefservice.municipios(nombreMunicipio);
    //aca ejecuto el geoRef para que me traiga la query que le estoy pasando
    Response<ListadoMunicipios> responseMunicipiosArgs = requestMunicipiosArg.execute();
    //System.out.println(responseMunicipiosArgs);
    return responseMunicipiosArgs.body();


  }
  public ListadoMunicipios listadoDeMunicipioPorIdConProvincia(String id_municipio) throws IOException {
    int idMunicipio = Integer.parseInt(id_municipio);
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoMunicipios> requestMunicipiosArg = georefservice.municipios(idMunicipio);
    Response<ListadoMunicipios> responseMunicipiosArgs = requestMunicipiosArg.execute();
    //logica para traerme las provincias que estan asociadas al municipio
    ListadoMunicipios listadoMunicipios = responseMunicipiosArgs.body();
    List<Municipio> municipios = listadoMunicipios.getMunicipios();
    for (Municipio unMunicipio : municipios) {
      ListadoProvincias provincia = this.obtenerProvinciaPorMunicipio(unMunicipio.getProvincia().getId());
      //estaba aca la clave!!!! , cuando hago un ObtenerProvinciaPorMunicipio
      // me devuelve toda la lista que matchee
      //pero yo quiero el primero!
      unMunicipio.setProvincia(provincia.getProvincias().get(0));
    }
    return listadoMunicipios;
  }

  public ListadoProvincias obtenerProvinciaPorMunicipio(int id) throws IOException {
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoProvincias> requestProvinciasArg = georefservice.provinciasSueltas(id);
    Response<ListadoProvincias> responseProvinciasArg = requestProvinciasArg.execute();
    ListadoProvincias provincia = responseProvinciasArg.body();
    return provincia;

  }

  public ListadoMunicipios listadoMunicipioAndProvincia(String nombreMunicipio,String nombreProvincia) throws IOException {
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoMunicipios> requestMunicipiosArg = georefservice.municipios(nombreMunicipio,nombreProvincia);
    Response<ListadoMunicipios> responseMunicipiosArgs = requestMunicipiosArg.execute();
    System.out.println(responseMunicipiosArgs);
    ListadoMunicipios listadoMunicipios = responseMunicipiosArgs.body();
    List<Municipio> municipios = listadoMunicipios.getMunicipios();
    System.out.println(municipios);
    if(listadoMunicipios != null ) {
      for (Municipio unMunicipio : municipios) {
        ListadoProvincias provincia = this.obtenerNombreProvinciaPorMunicipio(unMunicipio.getProvincia().getNombre());
        if(provincia != null && !provincia.getProvincias().isEmpty() && provincia.getProvincias().get(0).getNombre() == nombreProvincia) {
          unMunicipio.setProvincia(provincia.getProvincias().get(0));
        }

      }
    }
    return listadoMunicipios;
  }

  public ListadoProvincias obtenerNombreProvinciaPorMunicipio(String nombreProvincia) throws IOException {
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoProvincias> requestProvinciasArg = georefservice.provincias(nombreProvincia);
    Response<ListadoProvincias> responseProvinciasArg = requestProvinciasArg.execute();
    ListadoProvincias provincia = responseProvinciasArg.body();
    return provincia;

  }


  /* ***************************** DEPARTAMENTOS ************************ */
  public ListadoDepartamentos listadoDeDepartamentos() throws IOException {
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoDepartamentos> requestDepartamentosArg = georefservice.departamentos();
    //aca ejecuto el geoRef para que me traiga la query que le estoy pasando
    Response<ListadoDepartamentos> responseDeoartanebtisArgs = requestDepartamentosArg.execute();
    return responseDeoartanebtisArgs.body();
  }

  // lo que debo hacer es que esta funcion es que me devuelva el departamento por el ID que le paso
  // y no solo eso sino que me traiga el municipio y la provincia asociada!
  // esto es copypasteado de la funcion de arriba, pero debo modificarlo para que me traigan los 2
  public ListadoDepartamentos listadoDeDepartamentosPorId(String id) throws IOException {
    int idDepartamento = Integer.parseInt(id);
    GeorefService georefservice = this.retrofit.create(GeorefService.class);
    Call<ListadoDepartamentos> requestDepartamentosArg = georefservice.departamentos(idDepartamento);
    //aca ejecuto el geoRef para que me traiga la query que le estoy pasando
    Response<ListadoDepartamentos> responseDeoartanebtisArgs = requestDepartamentosArg.execute();
    return responseDeoartanebtisArgs.body();
  }



}

