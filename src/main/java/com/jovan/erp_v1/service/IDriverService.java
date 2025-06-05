package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;

public interface IDriverService {

    DriverResponse create(DriverRequest request);

    DriverResponse update(Long id, DriverRequest request);

    void delete(Long id);

    DriverResponse findOneById(Long id);

    List<DriverResponse> findAllDrivers();

    List<DriverResponse> findByName(String name);

    DriverResponse findByPhone(String phone);
}
