package com.example.phase4.repository;

import com.example.phase4.entity.Offer;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findAllByOrder(Order order);

    Optional<Offer> findOfferByOrderAndIsAcceptedTrue(Order order);


    @Query("select o from Offer o where o.order.id =:orderId and o.isAccepted = true")
    Optional<Offer> findAcceptedOfferById(long orderId);



    List<Offer> findAllByOrderId(long orderId);

    @Modifying
    @Query("update Offer o set o.isAccepted = true, o.order.orderStatus = 'AWAITING_CUSTOMER_CHOICE' where o.id = :offerId and o.order.customer.id = :customerId")
    int setAccepted(long offerId,long customerId);

    List<Offer> findByIsAcceptedTrueAndSpecialistId(long specialistId);


    @Query("SELECT o2 from Offer o1 join o1.order o2 where o1.specialist.id =:specialistId and o1.isAccepted = true and o2.orderStatus =:orderStatus")
    List<Order> filteredOrders(long specialistId, OrderStatus orderStatus);
}
