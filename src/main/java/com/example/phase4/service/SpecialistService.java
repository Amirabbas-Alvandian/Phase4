package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.dto.request.OfferRequestDTO;
import com.example.phase4.dto.response.OrderResponseDTO;
import com.example.phase4.entity.Offer;
import com.example.phase4.entity.Order;
import com.example.phase4.entity.Specialist;
import com.example.phase4.entity.enums.OrderStatus;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface SpecialistService extends BaseService<Specialist,Long> {
    List<Order> seeAllRelatedOrders(Specialist specialist);

    Order pickOrder(List<Order> orders, int index);

    Offer giveOffer(Offer offer);

    Boolean checkImageSize(Path path);

    Specialist saveWithValidation(Specialist specialist, Path path);

    boolean checkImageFormat(Path path);

    byte[] imageValidation(Path path);

    Path writeImageAfterSuccessfulSave(byte[] writeImage, Path path);

    Path readImageFromDateBase(Specialist specialist, Path path);

    Optional<Specialist> signIn(String email, String oldPassword);

    void setNewPassword(String email, String oldPassword, String newPassword);

    Optional<Specialist> findByEmail(String email);

    Specialist saveRequest(Specialist specialist);

    List<OrderResponseDTO> requestRelatedOrders(long specialistId);

    Offer requestOffer(OfferRequestDTO requestDTO);

    int seeRating(long orderId);

    double getBalance(String email);

    List<Order> filteredOrders(String email, OrderStatus orderStatus);

    int setStatusToAwaitingVerification(String email);
}
