package com.example.phase4.repository;

import com.example.phase4.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Modifying
    @Query("UPDATE Wallet w set w.balance = w.balance + :amount where w.user.id = :customerId")
    int deposit(long customerId,double amount);

    @Modifying
    @Query("UPDATE Wallet w set w.balance = w.balance - :amount where w.user.id = :customerId")
    int pay(long customerId,double amount);

    Optional<Wallet> findByUserId(Long userId);
}
