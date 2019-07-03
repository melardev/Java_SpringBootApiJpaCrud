package com.melardev.spring.apicrud.repositories;

import com.melardev.spring.apicrud.entities.Todo;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    List<T> fetchAll();

    List<Todo> findByHqlCompletedIs(boolean completed);

    Optional<T> fetchById(ID id);

    long count();

    T save(T entity);

    int deleteAll();

    void delete(T entity);

    Optional<T> fetchAsProxy(ID id);
}
