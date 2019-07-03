package com.melardev.spring.apicrud.repositories;


import com.melardev.spring.apicrud.entities.Todo;
import com.melardev.spring.apicrud.utils.JpaUtilEntityManager;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class JPQLTodosRepository implements TodosRepository {

    public List<Todo> fetchAll() {
        String hql = "select new com.melardev.spring.apicrud.entities.Todo(t.id, t.title, t.completed, t.createdAt, t.updatedAt) from Todo t order by t.createdAt desc";
        return todoList(hql);
    }

    @Override
    public Optional<Todo> fetchById(Long id) {
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        em.getTransaction().begin();
        Todo todo = em.find(Todo.class, id);
        em.getTransaction().commit();
        return Optional.ofNullable(todo);
    }


    @Override
    public Optional<Todo> fetchAsProxy(Long todoId) {
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        em.getTransaction().begin();
        try {
            Todo reference = em.getReference(Todo.class, todoId);
            em.getTransaction().commit();
            return Optional.of(reference);
        } catch (Exception ex) { // EntityNotFoundException if was not found
            return Optional.empty();
        }
    }

    /*
    @Override
    public Optional<Todo> fetchAsProxy(Long todoId) {
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        em.getTransaction().begin();
        TypedQuery<Todo> query = em.createQuery("select new com.melardev.spring.apicrud.entities.Todo(t.id) from Todo t where t.id = ?1", Todo.class);
        query.setParameter(1, todoId);
        try {
            Todo todo = query.getSingleResult();
            em.getTransaction().commit();
            return Optional.of(todo);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
    */

    private List<Todo> todoList(String hql) {
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        TypedQuery<Todo> query = em.createQuery(hql, Todo.class);
        return query.getResultList();
    }

    @Override
    public List<Todo> findByHqlCompletedIs(boolean completed) {
        String hql = "select new com.melardev.spring.apicrud.entities.Todo(t.id, t.title, t.completed, t.createdAt, t.updatedAt) from Todo t where t.completed = :completed";
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        TypedQuery<Todo> query = em.createQuery(hql, Todo.class);
        query.setParameter("completed", completed);
        return query.getResultList();
    }

    List<Todo> findByHqlTitleLike(String word) {
        String hql = "select t from com.melardev.spring.apicrud.entities.Todo t where t.title like concat('%', :word, '%')";
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        TypedQuery<Todo> query = em.createQuery(hql, Todo.class);
        query.setParameter("word", word);
        return query.getResultList();
    }

    List<Todo> findByHqlTitleAndDescription(String title, String description) {
        String hql = "SELECT t FROM Todo t WHERE t.title = ?1 and t.description  = ?2";
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        TypedQuery<Todo> query = em.createQuery(hql, Todo.class);
        query.setParameter(1, title);
        query.setParameter(2, description);
        return query.getResultList();
    }


    @Override
    public long count() {
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        Long count = (Long) em.createQuery("select count(t.id) from Todo t").getSingleResult();
        return count;
    }

    @Override
    public Todo save(Todo todo) {
        if (todo.getId() != null) {
            EntityManager em = JpaUtilEntityManager.getEntityManager();
            em.getTransaction().begin();
            em.getTransaction().commit();
        } else {
            EntityManager em = JpaUtilEntityManager.getEntityManager();
            em.getTransaction().begin();
            em.persist(todo);
            em.getTransaction().commit();
        }
        return todo;
    }

    @Override
    public void delete(Todo todo) {
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        em.getTransaction().begin();
        if (!em.contains(todo))
            todo = em.merge(todo);
        em.remove(todo);
        em.getTransaction().commit();
        em.close();
    }

    public int deleteAll() {
        EntityManager em = JpaUtilEntityManager.getEntityManager();
        em.getTransaction().begin();
        int affectedRows = em.createQuery("delete from Todo t").executeUpdate();
        em.getTransaction().commit();
        return affectedRows;
    }

}