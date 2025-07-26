package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.DriverErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
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
        validateString(request.name(), request.phone());
        driver.setName(request.name());
        driver.setPhone(request.phone());
        return new DriverResponse(driver);
    }

    @Transactional
    @Override
    public DriverResponse update(Long id, DriverRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
        Driver driver = driversRepository.findById(id)
                .orElseThrow(() -> new DriverErrorException("Driver not found wtih id " + id));
        validateString(request.name(), request.phone());
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
    	List<Driver> items = driversRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Driver list is empty");
    	}
        return items.stream()
                .map(DriverResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverResponse> findByName(String name) {
    	validateString(name);
    	List<Driver> items = driversRepository.findByName(name);
    	if(items.isEmpty()) {
    		String msg = String.format("No Driver with name equal to %s is found", name);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(DriverResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse findByPhone(String phone) {
    	validateString(phone);
        Driver driver = driversRepository.findByPhone(phone)
                .orElseThrow(() -> new DriverErrorException("Driver with phone not found" + phone));
        return new DriverResponse(driver);
    }
    
 /**Metoda sa jednim obaveznim argumentom
  * Posto Java nema default-ne argumente kao recimo Python, morao sam da napravim dve odvojene String metode.
  *  */
    private void validateString(String str) {
        validateString(str, null);  // Pozivamo "glavnu" verziju metode
    }

    // Metoda sa dva argumenta (drugi je "opciono")
    private void validateString(String str, String s) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(
                s != null ? s : "Tekstualni karakter za vozačevo ime ili broj telefona ne sme biti prazan ili null"
            );
        }
    }
    
    

}
