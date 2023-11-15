package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.entity.Wallet;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.exception.NotEnoughBalanceException;
import com.example.phase4.repository.WalletRepository;
import com.example.phase4.service.OrderService;
import com.example.phase4.service.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl extends BaseServiceImpl<Wallet,Long> implements WalletService {
    private final WalletRepository repository;

    private final OrderService orderService;

    private final double specialistPortion = 0.7;

    public WalletServiceImpl(WalletRepository repository, OrderService orderService) {
        super(repository);
        this.repository = repository;
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public int deposit(long customerId, double amount) {
        return repository.deposit(customerId, amount);
    }

    @Override
    @Modifying
    @Transactional
    public int payWithWallet(long customerId, double amount, long specialistId,long orderId) {
        try {
            pay(customerId,amount);
            int affectedRows = deposit(specialistId, amount * specialistPortion);
            orderService.setOrderStatusToPayed(orderId);
            return affectedRows;
        }catch (DataIntegrityViolationException d){
            throw new NotEnoughBalanceException("wallet with userId %s doest not have enough balance"
                    .formatted(customerId));
        }
    }

    @Override
    @Transactional
    @Modifying
    public int payOnline(long specialistId, double amount,long orderId) {
            int affectedRows = deposit(specialistId, amount * specialistPortion);
            orderService.setOrderStatusToPayed(orderId);
            return affectedRows;

    }

    @Override
    public int pay(long userId, double amount) {
        return repository.pay(userId,amount);
    }

    @Override
    public Wallet findByUserId(Long userId) {
        return repository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("wallet with userId %d not found".formatted(userId)
        ));
    }
}
