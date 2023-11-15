package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.entity.Offer;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.enums.OrderStatus;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.repository.OfferRepository;
import com.example.phase4.service.OfferService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl extends BaseServiceImpl<Offer,Long> implements OfferService {

    private final OfferRepository repository;

    public OfferServiceImpl(OfferRepository repository) {
        super(repository);
        this.repository =repository;
    }

    @Override
    public List<Offer> findAllOffersByOrder(Order order) {
        return repository.findAllByOrder(order);
    }

    @Transactional
    @Override
    public Optional<Offer> findAcceptedOffer(Order order) {
        return repository.findOfferByOrderAndIsAcceptedTrue(order);
    }

    @Override
    @Transactional
    public List<Offer> findAllOffersByOrderById(long orderId) {
        return repository.findAllByOrderId(orderId);
    }

    @Override
    @Transactional
    @Modifying
    public int setAcceptedTrue(long offerId, long customerId) {
        return repository.setAccepted(offerId,customerId);
    }

    @Override
    public Optional<Offer> findAcceptedOfferById(long orderId) {
        return repository.findAcceptedOfferById(orderId);
    }

    @Override
    public List<Offer> findByIsAcceptedTrueAndSpecialistId(long specialistId) {
        List<Offer> offers = repository.findByIsAcceptedTrueAndSpecialistId(specialistId);
        if (offers.isEmpty())
            throw new EntityNotFoundException("no accepted offers for specialist with id %s".formatted(specialistId));
        return offers;
    }

    @Override
    public List<Order> filteredOrders(long specialistId, OrderStatus orderStatus) {
        return repository.filteredOrders(specialistId, orderStatus);
    }
}
