package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.entity.Offer;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OfferService extends BaseService<Offer,Long> {
    List<Offer> findAllOffersByOrder(Order order);

    Optional<Offer> findAcceptedOffer(Order order);

    List<Offer> findAllOffersByOrderById(long orderId);

    int setAcceptedTrue(long offerId,long customerId);

    Optional<Offer> findAcceptedOfferById(long orderId);

    List<Offer> findByIsAcceptedTrueAndSpecialistId(long specialistId);

    List<Order> filteredOrders(long specialistId, OrderStatus orderStatus);

}
