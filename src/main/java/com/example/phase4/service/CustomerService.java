package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.dto.response.OfferResponseDTO;
import com.example.phase4.entity.*;
import com.example.phase4.entity.enums.OrderStatus;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseService<Customer,Long> {

    void updatePassword(String email, String oldPassword, String newPassword);

    Optional<Customer> signIn(String email, String password);

    Optional<Customer> findByEmail(String email);

    List<Category> seeAllCategories();

    Optional<Category> chooseCategory(String name);

    List<SubCategory> seeAllSubCategoriesOfSpecificCategory(Category category);

    SubCategory chooseSubCategory(List<SubCategory> subCategories, int index);

    Order saveNewOrder(Order order);

    List<Offer> seeAllOffersForSpecificOrder(Order order);

    List<Offer> allOffersOrderedByPrice(List<Offer> offers);

    List<Offer> allOffersOrderedBySpecialistScore(List<Offer> offers);

    Offer chooseOffer(List<Offer> offers, int index);

//    Task saveNewTask(Task task);

    Rating giveRating(Order order,int rating,String comment);

    Order setStatusToArrived(Order order);

    Order setOrderStatusToDone(Order order, LocalTime finishTime);

    Optional<Offer> findAcceptedOffer (Order order);

    Customer saveRequest(Customer customer);

    int updatePasswordQuery(String email, String oldPassword, String newPassword);

    Order orderRequest(String categoryName,String subCategoryName,String email,Order order);

    List<OfferResponseDTO> findAllOffersByOrderById(long orderId);

    int setOfferAcceptedTrue(long offerId,long customerId);

    Order findOrderById(long orderId);

    Offer chooseOfferById(long offerId);

    Offer returnAcceptedOffer(long orderId);

    double getBalance(String email);

    List<Order> filteredOrders(String email,OrderStatus orderStatus);
}
