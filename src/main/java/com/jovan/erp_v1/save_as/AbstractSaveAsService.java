package com.jovan.erp_v1.save_as;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.ValidationException;

/**
 *Apstraktna/bazna klasa sablona za cuvanje - save_as
 *Parametri apstraktne klase su: entitet i response-objekat
 */
public abstract class AbstractSaveAsService<T, R> implements SaveAsService<T, R> {

    protected abstract JpaRepository<T, Long> getRepository();
    //R -> DTP/Response objekat
    protected abstract R toResponse(T entity);
    //T -> tip podatka, jer je Java
    protected abstract T copyAndOverride(T source, Map<String, Object> overrides);

    @Override
    @Transactional
    /**
     *Metoda saveAs koja vraca DTO/Response objekat za dati save-as objekat
     */
    public R saveAs(Long sourceId, Map<String, Object> overrides) {
        T source = getRepository().findById(sourceId)
            .orElseThrow(() -> new ValidationException("Entity not found"));

        T newEntity = copyAndOverride(source, overrides);
        T saved = getRepository().save(newEntity);

        return toResponse(saved);
    }
}
