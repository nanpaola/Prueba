package com.transport.api.repository;
import com.transport.api.entity.TransportOrder;
import org.springframework.data.jpa.repository.*;
import java.util.UUID;
public interface OrderRepository extends JpaRepository<TransportOrder,UUID>, JpaSpecificationExecutor<TransportOrder> {}
