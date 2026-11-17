package com.transport.api.entity;
import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="assignments") public class Assignment {
 @Id @GeneratedValue(strategy=GenerationType.UUID) public UUID id;
 @OneToOne(optional=false) @JoinColumn(name="order_id",nullable=false,unique=true) public TransportOrder order;
 @ManyToOne(optional=false) @JoinColumn(name="driver_id",nullable=false) public Driver driver;
 public String pdfPath;
 public String imagePath;
}
