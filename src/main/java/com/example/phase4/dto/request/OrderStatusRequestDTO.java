package com.example.phase4.dto.request;

import com.example.phase4.entity.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record OrderStatusRequestDTO(@Enumerated(EnumType.STRING)
                                    OrderStatus orderStatus) {
}
