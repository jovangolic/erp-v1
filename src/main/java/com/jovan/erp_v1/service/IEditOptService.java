package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.EditOptType;
import com.jovan.erp_v1.request.EditOptRequest;
import com.jovan.erp_v1.response.EditOptResponse;

public interface IEditOptService {

    EditOptResponse create(EditOptRequest request);
    EditOptResponse update(Long id, EditOptRequest request);
    void delete(Long id);
    EditOptResponse getById(Long id);
    List<EditOptResponse> getAll();
    List<EditOptResponse> getByType(EditOptType type);
    
    EditOptResponse findByName(String name);
    EditOptResponse findByValue(String value);
}
