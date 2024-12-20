package org.example.dao;

import java.util.List;

public interface CRUDInterface<T> {

    void save(T item);

    void saveMany(List<T> items) ;

    void update(T item);

    void delete(T item);

    List<T> findAll();

    void deleteAll();

}
