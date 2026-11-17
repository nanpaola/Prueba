package com.transport.api.service;
import com.transport.api.dto.Requests.CreateAssignment;
import com.transport.api.entity.*;
import com.transport.api.exception.ApiException;
import com.transport.api.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.*;
@Service public class AssignmentService {
 private final AssignmentRepository assignments;private final OrderRepository orders;private final DriverRepository drivers;private final Path storage;
 public AssignmentService(AssignmentRepository assignments,OrderRepository orders,DriverRepository drivers,@Value("${app.upload-dir:uploads}") String uploadDir){this.assignments=assignments;this.orders=orders;this.drivers=drivers;this.storage=Paths.get(uploadDir).toAbsolutePath().normalize();}
 @Transactional public Assignment create(CreateAssignment r){TransportOrder o=orders.findById(r.orderId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Order not found"));Driver d=drivers.findById(r.driverId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Driver not found"));if(!d.active)throw new ApiException(HttpStatus.CONFLICT,"Driver is inactive");if(o.status!=OrderStatus.CREATED)throw new ApiException(HttpStatus.CONFLICT,"Order must be CREATED");if(assignments.existsByOrderId(o.id))throw new ApiException(HttpStatus.CONFLICT,"Order already assigned");Assignment a=new Assignment();a.order=o;a.driver=d;return assignments.save(a);}
 @Transactional public Assignment upload(UUID id,MultipartFile file,boolean pdf){Assignment a=assignments.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Assignment not found"));if(file.isEmpty())throw new ApiException(HttpStatus.BAD_REQUEST,"Empty file");String ext;try(InputStream in=file.getInputStream()){byte[] header=in.readNBytes(8);boolean isPdf=header.length>=5&&header[0]=='%'&&header[1]=='P'&&header[2]=='D'&&header[3]=='F'&&header[4]=='-';boolean isPng=header.length>=8&&(header[0]&255)==137&&header[1]=='P'&&header[2]=='N'&&header[3]=='G'&&(header[4]&255)==13&&(header[5]&255)==10&&(header[6]&255)==26&&(header[7]&255)==10;boolean isJpeg=header.length>=3&&(header[0]&255)==255&&(header[1]&255)==216&&(header[2]&255)==255;if(pdf&&!isPdf)throw new ApiException(HttpStatus.BAD_REQUEST,"A valid PDF is required");if(!pdf&&!isPng&&!isJpeg)throw new ApiException(HttpStatus.BAD_REQUEST,"A valid PNG or JPEG is required");ext=pdf?"pdf":isPng?"png":"jpg";Files.createDirectories(storage);Path target=storage.resolve(UUID.randomUUID()+"."+ext);try(InputStream source=file.getInputStream()){Files.copy(source,target,StandardCopyOption.REPLACE_EXISTING);}if(pdf)a.pdfPath=target.toString();else a.imagePath=target.toString();return assignments.save(a);}catch(IOException e){throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"File could not be stored");}}
}
