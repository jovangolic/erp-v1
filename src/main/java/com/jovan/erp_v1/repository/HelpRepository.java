package com.jovan.erp_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.jovan.erp_v1.enumeration.HelpCategory;
import com.jovan.erp_v1.model.Help;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {

    List<Help> findByCategory(HelpCategory category);

    List<Help> findByIsVisibleTrue();
}
