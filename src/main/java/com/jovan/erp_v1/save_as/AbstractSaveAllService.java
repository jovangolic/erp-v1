package com.jovan.erp_v1.save_as;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

/**
 *Apstraktna/bazna klasa sablona za cuvanje - save_all
 *Parametri apstraktne klase su: entitet i response-objekat
 */
@Slf4j
public abstract class AbstractSaveAllService<T, R> {

    protected abstract JpaRepository<T, Long> getRepository();

    protected abstract Function<T, R> toResponse();
    
    /**
     * Opcioni hook — koji se moze pregaziti u konkretnim servisima
     * ako zelim da evidentiram dodatne podatke (npr. korisnika, vreme, broj stavki)
     */
    protected void beforeSaveAll(List<T> entities) {
    	log.info("Preparing to save {} entities of type {}", 
                entities.size(), entities.isEmpty() ? "Unknown" : entities.get(0).getClass().getSimpleName());
    }
    
    /**
     * Opcioni hook — poziva se nakon sto su entiteti sacuvani.
     */
    protected void afterSaveAll(List<T> savedEntities) {
        log.info("Successfully saved {} entities of type {}", 
                savedEntities.size(), 
                savedEntities.isEmpty() ? "Unknown" : savedEntities.get(0).getClass().getSimpleName());
    }

    @Transactional
    public List<R> saveAll(List<T> entities) {
    	if (entities == null || entities.isEmpty()) {
            log.warn("Attempted to save empty or null entity list");
            throw new ValidationException("Entity list must not be empty.");
        }
        beforeSaveAll(entities);
        long start = System.currentTimeMillis();
        List<T> saved = getRepository().saveAll(entities);
        long end = System.currentTimeMillis();
        long duration = end - start;
        log.info("Batch save completed: {} records, duration: {} ms", saved.size(), duration);
        afterSaveAll(saved);
        return saved.stream().map(toResponse()).collect(Collectors.toList());
    }
}
