package com.transport.api.controller;
import com.transport.api.dto.Requests.CreateAssignment;
import com.transport.api.dto.Responses.AssignmentView;
import com.transport.api.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
@RestController @RequestMapping("/api/assignments") public class AssignmentController {
 private final AssignmentService service;public AssignmentController(AssignmentService s){service=s;}
 @PostMapping public ResponseEntity<AssignmentView> create(@Valid @RequestBody CreateAssignment r){return ResponseEntity.status(HttpStatus.CREATED).body(AssignmentView.from(service.create(r)));}
 @PostMapping(value="/{id}/pdf",consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public AssignmentView pdf(@PathVariable UUID id,@RequestPart("file") MultipartFile file){return AssignmentView.from(service.upload(id,file,true));}
 @PostMapping(value="/{id}/image",consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public AssignmentView image(@PathVariable UUID id,@RequestPart("file") MultipartFile file){return AssignmentView.from(service.upload(id,file,false));}
}
