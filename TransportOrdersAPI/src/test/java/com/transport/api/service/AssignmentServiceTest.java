package com.transport.api.service;
import com.transport.api.dto.Requests.CreateAssignment;
import com.transport.api.entity.*;
import com.transport.api.exception.ApiException;
import com.transport.api.repository.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class AssignmentServiceTest {
 AssignmentRepository a=mock(AssignmentRepository.class);OrderRepository o=mock(OrderRepository.class);DriverRepository d=mock(DriverRepository.class);
 AssignmentService service=new AssignmentService(a,o,d,"uploads");
 @Test void inactiveDriverCannotBeAssigned(){UUID oid=UUID.randomUUID(),did=UUID.randomUUID();TransportOrder order=new TransportOrder();order.id=oid;Driver driver=new Driver();driver.id=did;driver.active=false;when(o.findById(oid)).thenReturn(Optional.of(order));when(d.findById(did)).thenReturn(Optional.of(driver));assertEquals(409,assertThrows(ApiException.class,()->service.create(new CreateAssignment(oid,did))).status.value());}
 @Test void validAssignment(){UUID oid=UUID.randomUUID(),did=UUID.randomUUID();TransportOrder order=new TransportOrder();order.id=oid;Driver driver=new Driver();driver.id=did;driver.active=true;when(o.findById(oid)).thenReturn(Optional.of(order));when(d.findById(did)).thenReturn(Optional.of(driver));when(a.save(any())).thenAnswer(i->i.getArgument(0));assertEquals(driver,service.create(new CreateAssignment(oid,did)).driver);}
}
