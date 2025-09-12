package com.jovan.erp_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.jovan.erp_v1.enumeration.HelpCategory;
import com.jovan.erp_v1.model.Help;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {

    List<Help> findByCategory(HelpCategory category);

    List<Help> findByIsVisibleTrue();
    
    @Query("SELECT DISTINCT h.category FROM Help h")
    List<Help> findAllCategories();
    
    List<Help> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT h FROM Help h WHERE LOWER(h.content) LIKE LOWER(CONCAT('%',:content, '%'))")
    List<Help> findByContentContainingIgnoreCase(@Param("content") String content);
    @Query("SELECT h FROM Help h WHERE LOWER(h.title) LIKE LOWER(CONCAT('%',:title ,'%')) AND LOWER(h.content) LIKE LOWER(CONCAT('%',:content, '%'))")
    List<Help> findByTitleContainingIgnoreCaseAndContentContainingIgnoreCase(@Param("title") String title,@Param("content") String content);
    
    Boolean existsByTitle(String title);
   
}
