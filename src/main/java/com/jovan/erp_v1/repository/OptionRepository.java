package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Option;
import com.jovan.erp_v1.enumeration.OptionCategory;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByCategory(OptionCategory category);

    Option findByLabel(String label);

    Option findByValue(String value);
}
