package com.transport.api.entity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="transport_orders") public class TransportOrder {
 @Id @GeneratedValue(strategy=GenerationType.UUID) public UUID id;
 @Enumerated(EnumType.STRING) @Column(nullable=false) public OrderStatus status=OrderStatus.CREATED;
 @Column(nullable=false) public String origin;
 @Column(nullable=false) public String destination;
 @Column(nullable=false,updatable=false) public Instant createdAt;
 @Column(nullable=false) public Instant updatedAt;
 @PrePersist void created(){createdAt=Instant.now();updatedAt=createdAt;}
 @PreUpdate void updated(){updatedAt=Instant.now();}
}
