package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.domain.Persistence;
import ar.edu.utn.frba.dds.repositories.daos.DAO;

import java.util.List;

public class Repositorio<T> {
    protected DAO<T> dao;

    public Repositorio(DAO<T> dao) {
        this.dao = dao;
    }

    public void setDao(DAO<T> dao) {
        this.dao = dao;
    }

    public void agregar(Persistence unObjeto){
        this.dao.agregar(unObjeto);
    }

    public void modificar(Persistence unObjeto){
        this.dao.modificar(unObjeto);
    }

    public void eliminar(Persistence unObjeto){
        this.dao.eliminar(unObjeto);
    }

    public List<T> buscarTodos(){
        return this.dao.buscarTodos();
    }

    public T buscar(int id){
        return this.dao.buscar(id);
    }
}
