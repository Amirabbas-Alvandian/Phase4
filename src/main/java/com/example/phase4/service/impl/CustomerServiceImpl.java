package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.dto.response.OfferResponseDTO;
import com.example.phase4.entity.*;
import com.example.phase4.entity.enums.OrderStatus;
import com.example.phase4.entity.enums.Role;
import com.example.phase4.exception.*;
import com.example.phase4.mapper.OfferMapper;
import com.example.phase4.repository.CustomerRepository;
import com.example.phase4.service.*;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    private final CustomerRepository repository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final OrderService orderService;
    private final OfferService offerService;
    private final TaskService taskService;
    private final RatingService ratingService;

    private final WalletService walletService;

    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository repository,
                               CategoryService categoryService,
                               SubCategoryService subCategoryService,
                               OrderService orderService,
                               OfferService offerService,
                               TaskService taskService,
                               RatingService ratingService,
                               WalletService walletService, BCryptPasswordEncoder passwordEncoder) {
        super(repository);
        this.repository = repository;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.orderService = orderService;
        this.offerService = offerService;
        this.taskService = taskService;
        this.ratingService = ratingService;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    @Modifying
    public void updatePassword(String email, String oldPassword, String newPassword) {

        Optional<Customer> customer = signIn(email, oldPassword);

        customer.orElseThrow(()-> new EntityNotFoundException(
                String.format("user with email %s and entered password not found" +
                        "(wrong email or password)",email)
        )).setPassword(newPassword);

    }

    @Override
    public Optional<Customer> signIn(String email, String password) {
        return repository.findCustomerByEmailAndPassword(email, password);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return repository.findCustomerByEmail(email);
    }


    @Override
    public List<Category> seeAllCategories() {
        try {
            return categoryService.findAll();
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Category> chooseCategory(String name) {
        try {
            return categoryService.findByName(name);
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public List<SubCategory> seeAllSubCategoriesOfSpecificCategory(Category category) {
        try {
            return subCategoryService.findAllByCategory(category);
        } catch (NoResultException n) {
            System.out.println(n.getMessage());
            return null;
        }
    }

    @Override
    public SubCategory chooseSubCategory(List<SubCategory> subCategories, int index) {
        try {
            return subCategories.get(index);

        } catch (IndexOutOfBoundsException i) {
            System.out.println(i.getMessage());
            return null;
        }
    }


    @Override
    @Transactional
    public Order saveNewOrder(Order order) {
        if (order.getStartDate().isBefore(LocalDate.now()))
            throw new BadDateException("past Date");
        if (order.getCustomerPrice() < order.getSubCategory().getBasePrice())
            throw new IllegalPriceException("customer price lower than baseprice");

        return orderService.saveOrUpdate(order);
    }

    @Override
    @Transactional
    public List<Offer> seeAllOffersForSpecificOrder(Order order) {
        try {
            return offerService.findAllOffersByOrder(order);
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Offer> allOffersOrderedByPrice(List<Offer> offers) {
        if (offers.isEmpty())
            throw new NullPointerException();
        return offers.stream().sorted(Comparator.comparing(Offer::getSpecialistPrice)).toList();
    }

    @Override
    public List<Offer> allOffersOrderedBySpecialistScore(List<Offer> offers) {
        if (offers.isEmpty())
            throw new NullPointerException();
        return offers.stream().sorted(Comparator.comparing(o -> o.getSpecialist().getScore())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Modifying
    public Offer chooseOffer(List<Offer> offers, int index) {
        try {
            Optional<Offer> offer = offerService.findById(offers.get(index).getId());
            offer.orElseThrow(EntityNotFoundException::new).setIsAccepted(true);
            offer.get().getOrder().setOrderStatus(OrderStatus.AWAITING_FOR_SPECIALIST_ARRIVAL);
            return offer.get();
        } catch (IndexOutOfBoundsException i) {
            System.out.println(i.getMessage());
            return null;
        }
    }

 /*   @Override
    @Transactional
    @Modifying
    public Task saveNewTask(Task task) {
        try {
            task.getOrder().setOrderStatus(OrderStatus.AWAITING_FOR_SPECIALIST_ARRIVAL);
            return taskService.saveOrUpdate(task);
        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }*/

    @Override
    @Transactional
    @Modifying
    public Rating giveRating(Order order, int score, String comment) {

        if (order.getOrderStatus() == OrderStatus.PAYED
                || order.getOrderStatus() == OrderStatus.FINISHED)
            System.out.println("okay");
        else
            throw new BadDateException("the task is not finished yet");

        try {
            if (order.getId() == null)
                throw new EntityNotSavedException("order doesnt have id(not saved)");

            //Optional<Order> orderTemp = orderService.findById(order.getId());

            Optional<Offer> acceptedOffer = offerService.findAcceptedOffer(order);
            acceptedOffer.orElseThrow(()-> new EntityNotFoundException("no accepted offer found"))
                    .getSpecialist().setScore(acceptedOffer.get().getSpecialist().getScore() + score);

            Rating rating = new Rating(score, comment, order);
            rating = ratingService.saveOrUpdate(rating);
            order.setRating(rating);
            order = orderService.saveOrUpdate(order);
            if (order == null)
                throw new EntityNotSavedException("rating didnt save successfully");

            Offer offer = offerService.saveOrUpdate(acceptedOffer.get());
            if ( offer== null)
                throw new EntityNotSavedException("offer didnt save successfully");


            return rating;
        } catch (IllegalStateException | NullPointerException i) {
            System.out.println(i.getMessage());
            return null;
        }
    }

    @Override
    @Modifying
    @Transactional
    public Order setStatusToArrived(Order order) {
        if (order == null)
            throw new NullPointerException();
        if (order.getId() == null)
            throw new EntityNotSavedException("order doesnt have id(not saved)");

        Optional<Offer> offer = findAcceptedOffer(order);
        LocalDate startDate = offer.orElseThrow(() -> new EntityNotFoundException("no accepted offer"))
                .getStartDate();

        if (LocalDate.now().isBefore(startDate))
            throw new BadDateException("Start date has not arrived yet");
        order.setOrderStatus(OrderStatus.STARTED);
        return orderService.saveOrUpdate(order);
    }

    @Override
    @Transactional
    @Modifying
    public Order setOrderStatusToDone(Order order,LocalTime finishTime) {
        if (order == null)
            throw new NullPointerException();
        if (order.getId() == null)
            throw new EntityNotSavedException("order doesnt have id(not saved)");

        if (order.getOrderStatus() == OrderStatus.FINISHED)
            throw new AlreadySetFinishedException("order with id %s is already set finished".formatted(order.getId()));

        Optional<Offer> offer = findAcceptedOffer(order);
        LocalDate startDate = offer.orElseThrow(() -> new EntityNotFoundException("no accepted offer"))
                .getStartDate();

        if (LocalDate.now().isBefore(startDate))
            throw new BadDateException("Start date has not arrived yet");
        order.setOrderStatus(OrderStatus.FINISHED);

        if (finishTime.getHour() < offer.get().getStartTime().getHour())
            throw new BadTimeException("Start Time has not arrived yet");

        if (finishTime.getHour() > offer.get().getFinishTime().getHour())
            offer.get().getSpecialist().setScore(
                    offer.get().getSpecialist().getScore() - (finishTime.getHour() - offer.get().getFinishTime().getHour()));
        return orderService.saveOrUpdate(order);

/*        if (order.getId() != null){
            order.setOrderStatus(OrderStatus.FINISHED);
            return orderService.saveOrUpdate(order);
        }
        throw new AttemptingToUpdateNonPersistentObject("this order is not in database");*/
    }

    @Override
    public Optional<Offer> findAcceptedOffer(Order order) {
        return offerService.findAcceptedOffer(order);
    }

    @Override
    public Customer saveRequest(Customer customer) {
        customer.setRole(Role.ROLE_CUSTOMER);
        customer.setWallet(new Wallet(customer));
        customer.setSignUpDate(LocalDate.now());
        customer.setPassword(passwordEncoder
                .encode(customer.getPassword()));
        return saveOrUpdate(customer);
    }

    @Override
    @Transactional
    @Modifying
    public int updatePasswordQuery(String email, String oldPassword, String newPassword) {
        return repository.updatePasswordQuery(email, oldPassword, newPassword);
    }

    @Override
    public Order orderRequest(String categoryName, String subCategoryName, String email, Order order) {

        Optional<Customer> customer = findByEmail(email);

        customer.orElseThrow(()-> new EntityNotFoundException(
                String.format("user with email %s or entered password not found" +
                        "(wrong email or password)",email)));

        if (!customer.get().isEnabled())
            throw new CustomerNotVerifiedException("customer with email %s not verified".formatted(email));

        Optional<Category> category = categoryService.findByName(categoryName);

        Optional<SubCategory> subCategory = subCategoryService.findByNameAndCategory(subCategoryName,
                category.orElseThrow(()-> new EntityNotFoundException(
                        String.format("user with email %s or entered password not found" +
                                "(wrong email or password)",email))));

        order.setOrderStatus(OrderStatus.AWAITING_FOR_SPECIALIST_OFFER);
        order.setSubCategory(subCategory.orElseThrow(()
                -> new EntityNotFoundException(String.format("subcategory %s " +
                "does not exist",subCategoryName))));

        order.setCustomer(customer.get());

        return saveNewOrder(order);
    }

    @Override
    @Transactional
    public List<OfferResponseDTO> findAllOffersByOrderById(long orderId) {
        return offerService.findAllOffersByOrderById(orderId).stream()
                .map(OfferMapper.INSTANCE::modelToDTO).toList();
    }

    @Override
    @Transactional
    @Modifying
    public int setOfferAcceptedTrue(long offerId, long customerId) {
        return offerService.setAcceptedTrue(offerId,customerId);
    }

    @Override
    public Order findOrderById(long orderId) {
        return orderService.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException(String.format("order with id %s not found",orderId))
        );
    }

    @Override
    @Transactional
    @Modifying
    public Offer chooseOfferById(long offerId) {
        try {
            Optional<Offer> offer = offerService.findById(offerId);

            offer.orElseThrow(() -> new EntityNotFoundException(
                    String.format("offer with id %s not found",offerId)))
                    .setIsAccepted(true);

            offer.get().getOrder().setOrderStatus(OrderStatus.AWAITING_FOR_SPECIALIST_ARRIVAL);
            return offer.get();
        } catch (IndexOutOfBoundsException i) {
            System.out.println(i.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public Offer returnAcceptedOffer(long orderId) {
        return offerService.findAcceptedOfferById(orderId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("accepted offer with order id %s not found",orderId)));
    }

    @Override
    public double getBalance(String email) {
        long customerId = findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("user with email %s not found".formatted(email))
        ).getId();
        return walletService.findByUserId(customerId).getBalance();
    }

    @Override
    public List<Order> filteredOrders(String email,OrderStatus orderStatus) {
        return orderService.filteredOrders(findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("user with email %s not found".formatted(email))
        ).getId(),orderStatus );
    }


}
