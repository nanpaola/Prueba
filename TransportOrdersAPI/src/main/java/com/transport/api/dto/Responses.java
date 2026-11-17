package com.transport.api.dto;
import com.transport.api.entity.*;
import java.time.Instant;
import java.util.UUID;
public final class Responses {
 private Responses(){}
 public record OrderView(UUID id,OrderStatus status,String origin,String destination,Instant createdAt,Instant updatedAt){public static OrderView from(TransportOrder o){return new OrderView(o.id,o.status,o.origin,o.destination,o.createdAt,o.updatedAt);}}
 public record DriverView(UUID id,String name,String licenseNumber,boolean active){public static DriverView from(Driver d){return new DriverView(d.id,d.name,d.licenseNumber,d.active);}}
 public record AssignmentView(UUID id,UUID orderId,UUID driverId,boolean hasPdf,boolean hasImage){public static AssignmentView from(Assignment a){return new AssignmentView(a.id,a.order.id,a.driver.id,a.pdfPath!=null,a.imagePath!=null);}}
}
