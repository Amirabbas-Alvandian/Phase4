package com.example.phase4.repository;

import com.example.phase4.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByEmailAndPassword(String email, String password);

    Optional<Customer> findCustomerByEmail(String email);

    @Modifying
    @Query("update Customer c set c.password = :newPassword where c.email = :email and c.password = :oldPassword")
    int updatePasswordQuery(String email, String oldPassword,String newPassword);
}
