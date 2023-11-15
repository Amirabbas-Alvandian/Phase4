package com.example.phase4.service.impl;


import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.dto.UserOrdersDTO;
import com.example.phase4.dto.request.FilteredUserRequestDTO;
import com.example.phase4.dto.request.FilteredOrdersRequestDTO;
import com.example.phase4.dto.request.FilteredUsers2;
import com.example.phase4.entity.*;
import com.example.phase4.entity.enums.OrderStatus;
import com.example.phase4.entity.enums.Role;
import com.example.phase4.entity.enums.SpecialistStatus;
import com.example.phase4.exception.DuplicateEntityException;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.exception.SpecialistSubCategoryNotFoundException;
import com.example.phase4.mapper.OfferMapper;
import com.example.phase4.mapper.OrderMapper;
import com.example.phase4.mapper.UserOrdersMapper;
import com.example.phase4.repository.AdminRepository;
import com.example.phase4.repository.AdminRepositoryImpl;
import com.example.phase4.service.*;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin,Long> implements AdminService {
    private final AdminRepository repository;
    private final SpecialistService specialistService;
    private final CustomerService customerService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final UserService userService;
    private final OrderService orderService;
    private final OfferService offerService;

    private final AdminRepositoryImpl repositoryImpl;
    private final OrderMapper orderMapper;
    private final OfferMapper offerMapper;

    private final BCryptPasswordEncoder passwordEncoder;


    public AdminServiceImpl(AdminRepository repository, SpecialistService specialistService, CustomerService customerService, CategoryService categoryService, SubCategoryService subCategoryService, UserService userService, OrderService orderService, OfferService offerService, AdminRepositoryImpl repositoryImpl,
                            OrderMapper orderMapper,
                            OfferMapper offerMapper, BCryptPasswordEncoder passwordEncoder) {
        super(repository);
        this.specialistService = specialistService;
        this.customerService = customerService;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.repository = repository;
        this.userService = userService;
        this.orderService = orderService;
        this.offerService = offerService;
        this.repositoryImpl = repositoryImpl;
        this.orderMapper = orderMapper;
        this.offerMapper = offerMapper;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    @Transactional
    public Specialist addSpecialist(Specialist specialist, Path path) {
        try {

            return specialistService.saveWithValidation(specialist, path);

        } catch (RollbackException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public Customer addCustomer(Customer customer) {

        try {

            return customerService.saveOrUpdate(customer);

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("email already exists");
        return null;
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        try {
            return categoryService.saveOrUpdate(category);
        } catch (PersistenceException p) {
            System.out.println(p.getMessage());
        }
        throw new DuplicateEntityException("category already exists");
    }

    @Override
    @Transactional
    public SubCategory addSubCategory(SubCategory subCategory) {
        try {
            return subCategoryService.saveOrUpdate(subCategory);
        } catch (PersistenceException p) {
            System.out.println(p.getMessage());
        }
        throw new DuplicateEntityException("subCategory already exists");

    }

    @Override
    @Transactional
    @Modifying
    public void addSubCategoryForSpecialist(String specialistEmail, String subCategoryName,String categoryName) {
        try {
            Optional<Specialist> specialist = specialistService.findByEmail(specialistEmail);
            if (specialist.isEmpty())
                throw new EntityNotFoundException("specialist not found");
            Optional<Category> category = categoryService.findByName(categoryName);
            if (category.isEmpty())
                throw new EntityNotFoundException("category not found");
            Optional<SubCategory> subCategory = subCategoryService.findByNameAndCategory(subCategoryName,category.get());
            if (subCategory.isEmpty())
                throw new EntityNotFoundException("subCategory not found");

            specialist.get().getSubCategoryList().add(subCategory.get());
            System.out.println("success");
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
        }
    }

    @Override
    @Modifying
    @Transactional
    public Specialist addSubCategoryForSpecialistWithId(long specialistId, long subCategoryId) {

            Optional<SubCategory> subCategory = subCategoryService.findById(subCategoryId);

            Optional<Specialist> specialist = specialistService.findById(specialistId);

            specialist.orElseThrow(
                    () -> new EntityNotFoundException(String.format("specialist with" +
                            " id %s does not exist",specialist)
            )).getSubCategoryList().add(subCategory.orElseThrow(
                    () -> new EntityNotFoundException(String.format("subCategory with" +
                            " id %s does not exist",subCategoryId)
            )));

            return specialist.get();
    }

    @Override
    @Transactional
    @Modifying
    public void deleteSubCategoryForSpecialist(String specialistEmail, String subCategoryName,String categoryName) {
        try {
            Optional<Specialist> specialist = specialistService.findByEmail(specialistEmail);
            if (specialist.isEmpty())
                throw new EntityNotFoundException("specialist not found");
            Optional<Category> category = categoryService.findByName(categoryName);
            if (category.isEmpty())
                throw new EntityNotFoundException("category not found");
            Optional<SubCategory> subCategory = subCategoryService.findByNameAndCategory(subCategoryName,category.get());
            if (subCategory.isEmpty())
                throw new EntityNotFoundException("subCategory not found");
            if (!specialist.get().getSubCategoryList().contains(subCategory.get())) {
                throw new SpecialistSubCategoryNotFoundException("no such category");
            }
            specialist.get().getSubCategoryList().remove(subCategory.get());
            System.out.println("success");
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
        }
    }

    @Override
    @Transactional
    @Modifying
    public Specialist deleteSubCategoryForSpecialistWithId(long specialistId, long subCategoryId) {
        Optional<SubCategory> subCategory = subCategoryService.findById(subCategoryId);

        Optional<Specialist> specialist = specialistService.findById(specialistId);

        specialist.orElseThrow(
                () -> new EntityNotFoundException(String.format("specialist with" +
                        " id %s does not exist",specialist)
                )).getSubCategoryList().remove(subCategory.orElseThrow(
                () -> new EntityNotFoundException(String.format("subCategory with" +
                        " id %s does not exist",subCategoryId)
                )));

        if (!specialist.get().getSubCategoryList().contains(subCategory.get()))
            throw new SpecialistSubCategoryNotFoundException(String.format("specialist with id %s does not have " +
                    "subCategory with id %s",specialistId,subCategoryId));

        return specialist.get();
    }

    @Override
    public List<Category> allCategories() {
        try {
            return categoryService.findAll();
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
            return null;
        }

    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        try {
            return categoryService.findByName(name);
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public List<SubCategory> allSubCategories() {
        try {
            return subCategoryService.findAll();
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
            return null;
        }

    }

    @Override
    public SubCategory findSubCategoryByName(String subCategoryName,String categoryName) {
        try {
            Optional<Category> category = categoryService.findByName(categoryName);
            if (category.isEmpty())
                throw new EntityNotFoundException("category not found");

            Optional<SubCategory> subCategory = subCategoryService.findByNameAndCategory(subCategoryName,category.get());
            if (subCategory.isEmpty())
                throw new EntityNotFoundException("subCategory not found");
            return subCategory.get();
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
            return null;
        }

    }

    @Override
    @Transactional
    @Modifying
    public void setSpecialistStatusToVerified(String email) {
            Optional<Specialist> specialist = findSpecialistByEmail(email);
            specialist.orElseThrow(
                    () ->new EntityNotFoundException("specialist not found"))
                    .setStatus(SpecialistStatus.VERIFIED);
    }

    @Override
    @Transactional
    @Modifying
    public SubCategory changeSubCategoryDescription(String subCategoryName, String newDescription,String categoryName) {
        try {
            Optional<Category> category = categoryService.findByName(categoryName);
            if (category.isEmpty())
                throw new EntityNotFoundException("category not found");

            Optional<SubCategory> subCategory = subCategoryService.findByNameAndCategory(subCategoryName,category.get());
            subCategory.orElseThrow(()-> new EntityNotFoundException("subCategory not found") ).setDescription(newDescription);
            return subCategory.get();
        }catch (PersistenceException p){
            System.out.println(p.getMessage());
            return null;
        }

    }

    @Override
    @Transactional
    @Modifying
    public SubCategory changeSubCategoryPrice(String subCategoryName, double newPrice,String categoryName) {
        try {

            Optional<Category> category = categoryService.findByName(categoryName);
            if (category.isEmpty())
                throw new EntityNotFoundException("category not found");

            Optional<SubCategory> subCategory = subCategoryService.findByNameAndCategory(subCategoryName,category.get());
            subCategory.orElseThrow(()-> new EntityNotFoundException("subCategory not found")).setBasePrice(newPrice);
            return subCategory.get();
        }catch (PersistenceException p){
            System.out.println(p.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public Optional<Specialist> findSpecialistByEmail(String email) {
        return specialistService.findByEmail(email);
    }

    @Override
    public List<User> filteredUser(FilteredUserRequestDTO requestDTO) {
        return repositoryImpl.filteredUser(requestDTO);
    }

    @Override
    public List<UserOrdersDTO> userOrders(String email) {
        User user = findUserByEmail(email);

        if (user.getRole() == Role.ROLE_CUSTOMER)
            return orderService.findByCustomerId(user.getId())
                    .stream()
                    .filter(order -> order.getOrderStatus() == OrderStatus.FINISHED)
                    .map(orderService::AcceptedOfferByOrder)
                    .map(UserOrdersMapper.INSTANCE::offerModelToDTO)
                    .toList();


        return offerService.findByIsAcceptedTrueAndSpecialistId(user.getId())
                .stream()
                .map(UserOrdersMapper.INSTANCE::offerModelToDTO)
                .toList();

    }

    @Override
    public User findUserByEmail(String email) {
        return userService.findByEmail(email);
    }

    @Override
    public List<Order> filteredOrders(FilteredOrdersRequestDTO requestDTO) {
        return repositoryImpl.filteredOrders(requestDTO);
    }

    @Override
    public List<User> filteredUsers2(FilteredUsers2 requestDTO) {
        return repositoryImpl.filteredUsers2(requestDTO);
    }

    @Override
    public Admin saveRequest(Admin admin) {
        admin.setRole(Role.ROLE_ADMIN);
        admin.setWallet(new Wallet(admin));
        admin.setSignUpDate(LocalDate.now());
        admin.setPassword(passwordEncoder
                .encode(admin.getPassword()));
        return saveOrUpdate(admin);
    }
}
