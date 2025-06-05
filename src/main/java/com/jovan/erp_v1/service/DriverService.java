package com.jovan.erp_v1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.repository.DriversRepository;
import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService implements IDriverService {

    private final DriversRepository driversRepository;

    @Override
    public DriverResponse create(DriverRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DriverResponse update(Long id, DriverRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public DriverResponse findOneById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DriverResponse> findAllDrivers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DriverResponse> findByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DriverResponse findByPhone(String phone) {
        // TODO Auto-generated method stub
        return null;
    }

}
