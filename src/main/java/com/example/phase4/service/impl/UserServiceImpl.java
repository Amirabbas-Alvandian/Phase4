package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.User;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.exception.UserAlreadyEnabledException;
import com.example.phase4.repository.UserRepository;
import com.example.phase4.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User,Long> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("user with email %s not found"
                        .formatted(email))
        );
    }


    @Override
    @Transactional
    @Modifying
    public int enableUser(User user) {
        if (user.isEnabled())
            throw new UserAlreadyEnabledException("link to verify %s already used once".formatted(user.getEmail()));

        return userRepository.enableUser(user.getId());
    }

    @Override
    public List<Order> userOrders(long userId) {
        return null;
    }
}
