package com.transport.api.dto;
import com.transport.api.entity.OrderStatus;
import jakarta.validation.constraints.*;
import java.util.UUID;
public final class Requests {
 private Requests(){}
 public record CreateOrder(@NotBlank String origin,@NotBlank String destination){}
 public record UpdateStatus(@NotNull OrderStatus status){}
 public record CreateDriver(@NotBlank String name,@NotBlank String licenseNumber,boolean active){}
 public record CreateAssignment(@NotNull UUID orderId,@NotNull UUID driverId){}
 public record Login(@NotBlank String username,@NotBlank String password){}
 public record Token(String token,String type){}
}
