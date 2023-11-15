package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.entity.Wallet;

public interface WalletService extends BaseService<Wallet,Long> {

    int deposit (long userId,double amount);
    int payWithWallet (long customerId,double amount,long specialistId,long orderId);
    int payOnline (long specialistId,double amount,long orderId);
    int pay (long userId,double amount);
    Wallet findByUserId(Long userId);
}
