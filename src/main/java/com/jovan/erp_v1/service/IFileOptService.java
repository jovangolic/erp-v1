package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.FileAction;
import com.jovan.erp_v1.enumeration.FileExtension;
import com.jovan.erp_v1.request.FileOptRequest;
import com.jovan.erp_v1.response.FileOptResponse;

public interface IFileOptService {

    FileOptResponse create(FileOptRequest request);

    FileOptResponse update(Long id, FileOptRequest request);

    void delete(Long id);

    FileOptResponse getById(Long id);

    List<FileOptResponse> getAll();

    List<FileOptResponse> getByExtension(FileExtension extension);

    List<FileOptResponse> getByAction(FileAction action);
}
