package com.example.phase4.repository;

import com.example.phase4.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    Optional<Specialist> findByEmailAndPassword(String email, String oldPassword);

    Optional<Specialist> findByEmail(String email);

    @Query("UPDATE Specialist s set s.status = 'AWAITING_FOR_APPROVAL' where s.email = :email")
    @Modifying
    int setStatusToAwaitingVerification(String email);
}
