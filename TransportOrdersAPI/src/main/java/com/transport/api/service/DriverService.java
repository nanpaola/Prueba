package com.transport.api.service;
import com.transport.api.dto.Requests.CreateDriver;
import com.transport.api.entity.Driver;
import com.transport.api.exception.ApiException;
import com.transport.api.repository.DriverRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service public class DriverService {
 private final DriverRepository repo;
 public DriverService(DriverRepository repo){this.repo=repo;}
 @Transactional public Driver create(CreateDriver r){if(repo.existsByLicenseNumber(r.licenseNumber()))throw new ApiException(HttpStatus.CONFLICT,"License already registered");Driver d=new Driver();d.name=r.name();d.licenseNumber=r.licenseNumber();d.active=r.active();return repo.save(d);}
 @Transactional(readOnly=true) public List<Driver> active(){return repo.findByActiveTrue();}
}
