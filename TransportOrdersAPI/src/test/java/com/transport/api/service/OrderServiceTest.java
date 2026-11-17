package com.transport.api.service;
import com.transport.api.dto.Requests.CreateOrder;
import com.transport.api.entity.*;
import com.transport.api.exception.ApiException;
import com.transport.api.repository.OrderRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class OrderServiceTest {
 OrderRepository repo=mock(OrderRepository.class);OrderService service=new OrderService(repo);
 @Test void createStartsCreated(){when(repo.save(any())).thenAnswer(i->i.getArgument(0));TransportOrder order=service.create(new CreateOrder("Mexico City","Puebla"));assertEquals(OrderStatus.CREATED,order.status);assertEquals("Puebla",order.destination);}
 @Test void createdCanBecomeInTransit(){UUID id=UUID.randomUUID();TransportOrder o=new TransportOrder();o.id=id;when(repo.findById(id)).thenReturn(Optional.of(o));when(repo.save(any())).thenAnswer(i->i.getArgument(0));assertEquals(OrderStatus.IN_TRANSIT,service.change(id,OrderStatus.IN_TRANSIT).status);}
 @Test void deliveredCannotChange(){UUID id=UUID.randomUUID();TransportOrder o=new TransportOrder();o.status=OrderStatus.DELIVERED;when(repo.findById(id)).thenReturn(Optional.of(o));assertThrows(ApiException.class,()->service.change(id,OrderStatus.CREATED));}
 @Test void missingOrderIs404(){assertEquals(404,assertThrows(ApiException.class,()->service.get(UUID.randomUUID())).status.value());}
}
