package com.example.phase4.repository;

import com.example.phase4.entity.Order;
import com.example.phase4.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByOrder(Order order);
}
