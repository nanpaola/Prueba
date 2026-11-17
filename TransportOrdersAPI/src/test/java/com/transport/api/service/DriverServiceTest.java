package com.transport.api.service;
import com.transport.api.dto.Requests.CreateDriver;
import com.transport.api.exception.ApiException;
import com.transport.api.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class DriverServiceTest {
 DriverRepository repo=mock(DriverRepository.class);DriverService service=new DriverService(repo);
 @Test void duplicateLicenseIsRejected(){when(repo.existsByLicenseNumber("ABC123")).thenReturn(true);assertEquals(409,assertThrows(ApiException.class,()->service.create(new CreateDriver("Ana","ABC123",true))).status.value());}
}
