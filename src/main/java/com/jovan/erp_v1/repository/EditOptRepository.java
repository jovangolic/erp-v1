package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.EditOptType;
import com.jovan.erp_v1.model.EditOpt;

@Repository
public interface EditOptRepository extends JpaRepository<EditOpt, Long> {

    List<EditOpt> findByType(EditOptType type);
    EditOpt findByName(String name);
    EditOpt findByValue(String value);
}
