package com.transport.api.controller;
import com.transport.api.dto.Requests.CreateDriver;
import com.transport.api.dto.Responses.DriverView;
import com.transport.api.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/drivers") public class DriverController {
 private final DriverService service;public DriverController(DriverService s){service=s;}
 @PostMapping public ResponseEntity<DriverView> create(@Valid @RequestBody CreateDriver r){return ResponseEntity.status(HttpStatus.CREATED).body(DriverView.from(service.create(r)));}
 @GetMapping("/active") public List<DriverView> active(){return service.active().stream().map(DriverView::from).toList();}
}
