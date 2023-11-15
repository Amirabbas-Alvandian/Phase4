package com.example.phase4.service.impl;

import com.example.phase4.entity.ConfirmationToken;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.repository.ConfirmationTokenRepository;
import com.example.phase4.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmationTokenServiceImpl extends BaseServiceImpl<ConfirmationToken,String> implements ConfirmationTokenService {
    private final ConfirmationTokenRepository repository;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional
    public ConfirmationToken findByConfirmationToken(String confirmationToken){
        return repository.findByConfirmationToken(confirmationToken).orElseThrow(
                () -> new EntityNotFoundException("Entity with %s not found".formatted(confirmationToken))
        );
    }
}
