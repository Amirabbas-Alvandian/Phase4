package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.entity.Offer;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.SubCategory;
import com.example.phase4.entity.enums.OrderStatus;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.repository.OrderRepository;
import com.example.phase4.service.OfferService;
import com.example.phase4.service.OrderService;
import com.example.phase4.service.SubCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order,Long> implements OrderService {

    private final OrderRepository repository;

    private final SubCategoryService subCategoryService;

    private final OfferService offerService;
    private final List<OrderStatus> availableOrdersForOffers = new ArrayList<>(List.of
            (OrderStatus.AWAITING_FOR_SPECIALIST_OFFER,
            OrderStatus.AWAITING_CUSTOMER_CHOICE));
    public OrderServiceImpl(OrderRepository repository,
                            SubCategoryService subCategoryService, OfferService offerService) {
        super(repository);
        this.repository = repository;
        this.subCategoryService = subCategoryService;
        this.offerService = offerService;
    }

    @Override
    public List<Order> findAllRelatedAvailableOrders(List<SubCategory> subCategories) {
        return repository.findAllBySubCategoryInAndOrderStatusIn(subCategories,availableOrdersForOffers);
    }

    @Override
    public Optional<Offer> findAcceptedOffer(Order order) {
        return offerService.findAcceptedOffer(order);
    }

    @Override
    public int setOrderStatusToPayed(long orderId) {
        return repository.setOrderStatusToPayed(orderId);
    }

    @Override
    public List<Order> findByCustomerId(long customerId) {
        List<Order> orders = repository.findByCustomerId(customerId);
        if (orders.isEmpty())
            throw new EntityNotFoundException("no orders for customer with id %s found".formatted(customerId));
        return orders;
    }

    @Override
    public Offer AcceptedOfferByOrder(Order order) {
        return findAcceptedOffer(order).orElseThrow(
                () -> new EntityNotFoundException(
                        "order with id %d not found".formatted(order.getId())
                )
        );
    }

    @Override
    public List<Order> filteredOrders(long customerId,OrderStatus orderStatus) {
        return repository.findByCustomerIdAndOrderStatus(customerId, orderStatus);
    }


}
