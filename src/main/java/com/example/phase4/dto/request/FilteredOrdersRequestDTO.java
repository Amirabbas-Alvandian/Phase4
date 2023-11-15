package com.example.phase4.dto.request;

import com.example.phase4.entity.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record FilteredOrdersRequestDTO(@Enumerated(EnumType.STRING)
                                       OrderStatus orderStatus,
                                       @Positive
                                       Long categoryId,
                                       @Positive
                                       Long subCategoryId,
                                       LocalDate startDate,
                                       int offset) {
}
