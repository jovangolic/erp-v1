package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.LogisticsProvider;

@Repository
public interface LogisticsProviderRepository extends JpaRepository<LogisticsProvider, Long> {

    List<LogisticsProvider> findByName(String name);

    List<LogisticsProvider> findByNameContainingIgnoreCase(String fragment);

    LogisticsProvider findByContactPhone(String contactPhone);

    LogisticsProvider findByEmail(String email);

    LogisticsProvider findByWebsite(String website);

    @Query("SELECT l FROM LogisticsProvider l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(l.website) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<LogisticsProvider> searchByNameOrWebsite(@Param("query") String query);
}
