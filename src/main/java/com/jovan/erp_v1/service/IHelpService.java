package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.HelpCategory;
import com.jovan.erp_v1.request.HelpRequest;
import com.jovan.erp_v1.response.HelpResponse;

public interface IHelpService {

    HelpResponse create(HelpRequest request);

    HelpResponse update(Long id, HelpRequest request);

    void delete(Long id);

    HelpResponse getById(Long id);

    List<HelpResponse> getAll();

    List<HelpResponse> getVisible();

    List<HelpResponse> getByCategory(HelpCategory category);
}
