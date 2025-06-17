package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.OptionCategory;
import com.jovan.erp_v1.request.OptionRequest;
import com.jovan.erp_v1.response.OptionResponse;

public interface IOptionService {

    OptionResponse create(OptionRequest request);

    OptionResponse update(Long id, OptionRequest request);

    void delete(Long id);

    OptionResponse getOne(Long id);

    List<OptionResponse> getAll();

    List<OptionResponse> getByCategory(OptionCategory category);
}
