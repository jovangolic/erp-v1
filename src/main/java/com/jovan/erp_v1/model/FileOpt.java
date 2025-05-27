package com.jovan.erp_v1.model;

import java.util.Set;

import com.jovan.erp_v1.enumeration.FileAction;
import com.jovan.erp_v1.enumeration.FileExtension;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileOpt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FileExtension extension;

    @Column
    private String mimeType;

    @Column
    private Long maxSizeInBytes;

    @Column
    private boolean uploadEnabled = true;

    @Column
    private boolean previewEnabled = false;

    @Column
    @ElementCollection(targetClass = FileAction.class)
    @Enumerated(EnumType.STRING)
    private Set<FileAction> availableActions;

}
