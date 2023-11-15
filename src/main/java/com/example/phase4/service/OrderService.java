package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.entity.Offer;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.SubCategory;
import com.example.phase4.entity.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService extends BaseService<Order,Long> {

    List<Order> findAllRelatedAvailableOrders(List<SubCategory> subCategories);

    Optional<Offer> findAcceptedOffer(Order order);

    int setOrderStatusToPayed(long orderId);

    List<Order> findByCustomerId(long customerId);

    Offer AcceptedOfferByOrder(Order order);

    List<Order> filteredOrders(long customerId,OrderStatus orderStatus);
}
