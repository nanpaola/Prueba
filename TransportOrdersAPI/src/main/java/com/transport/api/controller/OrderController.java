package com.transport.api.controller;
import com.transport.api.dto.Requests.*;
import com.transport.api.dto.Responses.OrderView;
import com.transport.api.entity.OrderStatus;
import com.transport.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
@RestController @RequestMapping("/api/orders") public class OrderController {
 private final OrderService service;public OrderController(OrderService s){service=s;}
 @PostMapping public ResponseEntity<OrderView> create(@Valid @RequestBody CreateOrder r){return ResponseEntity.status(HttpStatus.CREATED).body(OrderView.from(service.create(r)));}
 @GetMapping("/{id}") public OrderView get(@PathVariable UUID id){return OrderView.from(service.get(id));}
 @PatchMapping("/{id}/status") public OrderView change(@PathVariable UUID id,@Valid @RequestBody UpdateStatus r){return OrderView.from(service.change(id,r.status()));}
 @GetMapping public List<OrderView> list(@RequestParam(required=false) OrderStatus status,@RequestParam(required=false) Instant start,@RequestParam(required=false) Instant end,@RequestParam(required=false) String origin,@RequestParam(required=false) String destination){return service.filter(status,start,end,origin,destination).stream().map(OrderView::from).toList();}
}
