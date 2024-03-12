package ar.edu.utn.frba.dds.repositories.daos;

import ar.edu.utn.frba.dds.domain.Persistence;

import java.util.List;

public interface DAO<T> {
    List<T> buscarTodos();
    T buscar(int id);
    void agregar(Persistence unObjeto);
    void modificar(Persistence unObjeto);
    void eliminar(Persistence unObjeto);
}
