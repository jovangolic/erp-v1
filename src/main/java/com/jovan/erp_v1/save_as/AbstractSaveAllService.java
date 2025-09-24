package com.jovan.erp_v1.save_as;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *Apstraktna/bazna klasa sablona za cuvanje - save_all
 *Parametri apstraktne klase su: entitet i response-objekat
 */
public abstract class AbstractSaveAllService<T, R> {

    protected abstract JpaRepository<T, Long> getRepository();

    protected abstract Function<T, R> toResponse();

    @Transactional
    public List<R> saveAll(List<T> entities) {
        List<T> saved = getRepository().saveAll(entities);
        return saved.stream().map(toResponse()).collect(Collectors.toList());
    }
}
