package ar.edu.utn.frba.dds.repositories.factories;

import ar.edu.utn.frba.dds.config.Config;
import ar.edu.utn.frba.dds.repositories.Repositorio;
import ar.edu.utn.frba.dds.repositories.daos.*;

import java.util.HashMap;

public class FactoryRepositorio {
    private static HashMap<String, Repositorio> repos;

    static {
        repos = new HashMap<>();
    }

    public static <T> Repositorio<T> get(Class<T> type){
        Repositorio<T> repo;
        if(repos.containsKey(type.getName())){
            repo = repos.get(type.getName());
        }
        else{
            if(Config.useDataBase){
                DAO<T> dao = new DAOHibernate<>(type);
                repo = new Repositorio<>(dao);
            }
            else{
                repo = null/*new Repositorio<>(new DAOMemoria<>(Data.getData(type)))*/;
            }
            repos.put(type.toString(), repo);
        }
        return repo;
    }
}
