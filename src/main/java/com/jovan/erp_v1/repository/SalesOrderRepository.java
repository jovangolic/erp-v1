package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.model.Invoice;
import com.jovan.erp_v1.model.SalesOrder;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    List<SalesOrder> findByBuyer(Buyer buyer);

    Optional<SalesOrder> findByInvoice(Invoice invoice);

    List<SalesOrder> findByInvoice_Id(Long invoiceId);

    List<SalesOrder> findByStatus(OrderStatus status);

    List<SalesOrder> findByBuyer_Id(Long buyerId);

    @Query("SELECT s FROM SalesOrder s WHERE s.orderDate BETWEEN :start AND :end")
    List<SalesOrder> findByOrderDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM SalesOrder s WHERE YEAR(s.orderDate) = :year")
    int countByOrderDateYear(@Param("year") int year);

    Optional<SalesOrder> findById(Long id);

    Optional<SalesOrder> findByOrderNumber(String orderNumber);

    Integer countByStatus(OrderStatus status);

    @Query("SELECT s.status, COUNT(s) FROM SalesOrder s GROUP BY s.status")
    List<Object[]> countOrdersByStatus();

}
