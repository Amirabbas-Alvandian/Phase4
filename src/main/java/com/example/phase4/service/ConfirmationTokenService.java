package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.entity.ConfirmationToken;

public interface ConfirmationTokenService extends BaseService<ConfirmationToken,String> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
