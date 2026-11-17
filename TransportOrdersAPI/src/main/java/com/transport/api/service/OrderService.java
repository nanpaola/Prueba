package com.transport.api.service;
import com.transport.api.dto.Requests.*;
import com.transport.api.entity.*;
import com.transport.api.exception.ApiException;
import com.transport.api.repository.OrderRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
@Service public class OrderService {
 private final OrderRepository repo;
 public OrderService(OrderRepository repo){this.repo=repo;}
 @Transactional public TransportOrder create(CreateOrder r){TransportOrder o=new TransportOrder();o.origin=r.origin();o.destination=r.destination();return repo.save(o);}
 @Transactional(readOnly=true) public TransportOrder get(UUID id){return repo.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Order not found"));}
 @Transactional public TransportOrder change(UUID id,OrderStatus next){TransportOrder o=get(id);boolean allowed=switch(o.status){case CREATED -> next==OrderStatus.IN_TRANSIT||next==OrderStatus.CANCELLED;case IN_TRANSIT -> next==OrderStatus.DELIVERED;case DELIVERED,CANCELLED -> false;};if(!allowed)throw new ApiException(HttpStatus.CONFLICT,"Invalid status transition: "+o.status+" -> "+next);o.status=next;return repo.save(o);}
 @Transactional(readOnly=true) public List<TransportOrder> filter(OrderStatus status,Instant start,Instant end,String origin,String destination){if(start!=null&&end!=null&&start.isAfter(end))throw new ApiException(HttpStatus.BAD_REQUEST,"start must be before end");Specification<TransportOrder> spec=(root,q,cb)->cb.conjunction();if(status!=null)spec=spec.and((root,q,cb)->cb.equal(root.get("status"),status));if(start!=null)spec=spec.and((root,q,cb)->cb.greaterThanOrEqualTo(root.get("createdAt"),start));if(end!=null)spec=spec.and((root,q,cb)->cb.lessThanOrEqualTo(root.get("createdAt"),end));if(origin!=null&&!origin.isBlank())spec=spec.and((root,q,cb)->cb.like(cb.lower(root.get("origin")),"%"+origin.toLowerCase(Locale.ROOT)+"%"));if(destination!=null&&!destination.isBlank())spec=spec.and((root,q,cb)->cb.like(cb.lower(root.get("destination")),"%"+destination.toLowerCase(Locale.ROOT)+"%"));return repo.findAll(spec);}
}
