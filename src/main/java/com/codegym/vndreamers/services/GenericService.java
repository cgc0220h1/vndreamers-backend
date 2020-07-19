package com.codegym.vndreamers.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericService<T> {
    List<T> findAllExist();

    Page<T> findAllExist(Pageable pageable);

    List<T> findAllDeleted();

    Page<T> findAllDeleted(Pageable pageable);

    T findExistById(int id);

    T findDeletedById(int id);

    T save(T model);

    T update(T model);

    boolean delete(int id);
}
