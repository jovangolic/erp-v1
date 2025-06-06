package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.DriverErrorException;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.repository.DriversRepository;
import com.jovan.erp_v1.request.DriverRequest;
import com.jovan.erp_v1.response.DriverResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService implements IDriverService {

    private final DriversRepository driversRepository;

    @Transactional
    @Override
    public DriverResponse create(DriverRequest request) {
        Driver driver = new Driver();
        driver.setName(request.name());
        driver.setPhone(request.phone());
        return new DriverResponse(driver);
    }

    @Transactional
    @Override
    public DriverResponse update(Long id, DriverRequest request) {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(() -> new DriverErrorException("Driver not found wtih id " + id));
        driver.setName(request.name());
        driver.setPhone(request.phone());
        return new DriverResponse(driver);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!driversRepository.existsById(id)) {
            throw new DriverErrorException("Driver not found wtih id " + id);
        }
        driversRepository.deleteById(id);
    }

    @Override
    public DriverResponse findOneById(Long id) {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(() -> new DriverErrorException("Driver not found wtih id " + id));
        return new DriverResponse(driver);
    }

    @Override
    public List<DriverResponse> findAllDrivers() {
        return driversRepository.findAll().stream()
                .map(DriverResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverResponse> findByName(String name) {
        return driversRepository.findByName(name).stream()
                .map(DriverResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse findByPhone(String phone) {
        Driver driver = driversRepository.findByPhone(phone)
                .orElseThrow(() -> new DriverErrorException("Driver with phone not found" + phone));
        return new DriverResponse(driver);
    }

}
