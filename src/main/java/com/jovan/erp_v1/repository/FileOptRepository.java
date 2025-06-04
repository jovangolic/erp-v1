package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.FileAction;
import com.jovan.erp_v1.enumeration.FileExtension;
import com.jovan.erp_v1.model.FileOpt;

@Repository
public interface FileOptRepository extends JpaRepository<FileOpt, Long> {

    List<FileOpt> findByExtension(FileExtension extension);

    List<FileOpt> findByAvailableActions(FileAction action);
}
