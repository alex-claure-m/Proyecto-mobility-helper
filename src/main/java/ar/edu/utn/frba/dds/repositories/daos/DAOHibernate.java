package ar.edu.utn.frba.dds.repositories.daos;

import ar.edu.utn.frba.dds.db.EntityManagerHelper;
import ar.edu.utn.frba.dds.domain.Persistence;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class DAOHibernate<T> implements DAO<T> {
    private Class<T> type;

    public DAOHibernate(Class<T> type){
        this.type = type;
    }

    /*@Override
    public List<T> buscarTodos() {
        CriteriaBuilder builder = EntityManagerHelper.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> critera = builder.createQuery(this.type);
        critera.from(type);
        List<T> entities = EntityManagerHelper.getEntityManager().createQuery(critera).getResultList();
        return entities;
    }

    @Override
    public T buscar(int id) {
        return EntityManagerHelper.getEntityManager().find(type, id);
    }*/

    @Override
    public List<T> buscarTodos() {
        return EntityManagerHelper.getEntityManager()
            .createQuery("FROM " + this.type.getName() + " WHERE invalidado IS NULL").getResultList();
    }

    @Override
    public T buscar(int id) {
        return (T) EntityManagerHelper.getEntityManager()
            .createQuery("FROM " + this.type.getName() + " WHERE invalidado IS NULL AND id = :id")
            .setParameter("id", id).getSingleResult();
    }

    @Override
    public void agregar(Persistence unObjeto) {
        EntityManagerHelper.getEntityManager().getTransaction().begin();
        EntityManagerHelper.getEntityManager().persist(unObjeto);
        EntityManagerHelper.getEntityManager().getTransaction().commit();
    }

    @Override
    public void modificar(Persistence unObjeto) {
        EntityManagerHelper.getEntityManager().getTransaction().begin();
        EntityManagerHelper.getEntityManager().merge(unObjeto);
        EntityManagerHelper.getEntityManager().getTransaction().commit();
    }

    /*@Override
    public void eliminar(Persistence unObjeto) {
        EntityManagerHelper.getEntityManager().getTransaction().begin();
        EntityManagerHelper.getEntityManager().remove(unObjeto);
        EntityManagerHelper.getEntityManager().getTransaction().commit();
    }*/

    @Override
    public void eliminar(Persistence unObjeto) {
        unObjeto.setInvalidado(Boolean.TRUE);
        this.modificar(unObjeto);
    }
}
