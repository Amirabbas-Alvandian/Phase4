package com.example.phase4.repository;

import com.example.phase4.entity.Order;
import com.example.phase4.entity.SubCategory;
import com.example.phase4.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllBySubCategoryInAndOrderStatusIn(List<SubCategory> subCategories, List<OrderStatus> orderStatuses);

    @Modifying
    @Query("UPDATE Order o set o.orderStatus = 'PAYED' where o.id = :orderId")
    int setOrderStatusToPayed(long orderId);

    List<Order> findByCustomerId(long customerId);

    List<Order> findByCustomerIdAndOrderStatus(long customerId,OrderStatus orderStatus);
}
