package com.transport.api.entity;
import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="drivers",uniqueConstraints=@UniqueConstraint(columnNames="license_number")) public class Driver {
 @Id @GeneratedValue(strategy=GenerationType.UUID) public UUID id;
 @Column(nullable=false) public String name;
 @Column(name="license_number",nullable=false) public String licenseNumber;
 @Column(nullable=false) public boolean active;
}
