package com.transport.api.repository;
import com.transport.api.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface AssignmentRepository extends JpaRepository<Assignment,UUID> {
 boolean existsByOrderId(java.util.UUID orderId);
}
