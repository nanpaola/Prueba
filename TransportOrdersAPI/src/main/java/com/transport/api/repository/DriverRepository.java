package com.transport.api.repository;
import com.transport.api.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface DriverRepository extends JpaRepository<Driver,UUID> {
 java.util.List<Driver> findByActiveTrue();
 boolean existsByLicenseNumber(String licenseNumber);
}
