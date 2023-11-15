package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.User;

import java.util.List;

public interface UserService extends BaseService<User,Long> {

    User findByEmail(String email);

    int enableUser(User user);

    List<Order> userOrders (long userId);
}
