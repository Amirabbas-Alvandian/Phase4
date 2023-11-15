package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.dto.UserOrdersDTO;
import com.example.phase4.dto.request.AdminRequestDTO;
import com.example.phase4.dto.request.FilteredUserRequestDTO;
import com.example.phase4.dto.request.FilteredOrdersRequestDTO;
import com.example.phase4.dto.request.FilteredUsers2;
import com.example.phase4.entity.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface AdminService extends BaseService<Admin,Long> {


    Specialist addSpecialist(Specialist specialist, Path path);

    Customer addCustomer(Customer customer);

    Category addCategory(Category category);

    SubCategory addSubCategory(SubCategory subCategory);

    void addSubCategoryForSpecialist(String specialistEmail, String subCategoryName, String categoryName);
    Specialist addSubCategoryForSpecialistWithId(long specialistId, long subCategoryId);

    void deleteSubCategoryForSpecialist(String specialistEmail, String subCategoryName, String categoryName);
    Specialist deleteSubCategoryForSpecialistWithId(long specialistId, long subCategoryId);

    List<Category> allCategories();

    Optional<Category> findCategoryByName(String name);

    List<SubCategory> allSubCategories();

    SubCategory findSubCategoryByName(String subCategoryName,String categoryName);

    void setSpecialistStatusToVerified(String email);

    SubCategory changeSubCategoryDescription(String subCategoryName,String newDescription,String categoryName);
    SubCategory changeSubCategoryPrice(String subCategoryName,double newPrice,String categoryName);

    Optional<Specialist> findSpecialistByEmail(String email);

    List<User> filteredUser(FilteredUserRequestDTO requestDTO);

    List<UserOrdersDTO> userOrders(String email);

    User findUserByEmail(String email);

    List<Order> filteredOrders(FilteredOrdersRequestDTO requestDTO);

    List<User> filteredUsers2(FilteredUsers2 requestDTO);

    Admin saveRequest (Admin admin);
}
