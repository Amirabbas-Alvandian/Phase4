package com.example.phase4.controller;

import com.example.phase4.dto.CardDto;
import com.example.phase4.dto.CustomerFinishTimeDTO;
import com.example.phase4.dto.request.CustomerRequestDTO;
import com.example.phase4.dto.request.OrderRequestDTO;
import com.example.phase4.dto.request.OrderStatusRequestDTO;
import com.example.phase4.dto.request.RatingRequestDTO;
import com.example.phase4.dto.response.*;
import com.example.phase4.entity.*;
import com.example.phase4.entity.enums.OrderStatus;
import com.example.phase4.exception.AlreadyPayedException;
import com.example.phase4.exception.CaptchaRevokedException;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.exception.InvalidCaptchaTextException;
import com.example.phase4.mapper.*;
import com.example.phase4.service.CategoryService;
import com.example.phase4.service.CustomerService;
import com.example.phase4.service.SubCategoryService;
import com.example.phase4.service.WalletService;
import com.wf.captcha.SpecCaptcha;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController extends usefulMethods{

    private final CustomerService customerService;
    private final OrderMapper orderMapper;
    private final WalletService walletService;
    private final OfferMapper offerMapper;

    private final AtomicInteger counter = new AtomicInteger();
    private final Map<Integer,String> captchaMap = new ConcurrentHashMap<>();

    public CustomerController(SubCategoryService service, CategoryService categoryService, CustomerService customerService,
                              OrderMapper orderMapper, WalletService walletService,
                              OfferMapper offerMapper) {
        super(service, categoryService);
        this.customerService = customerService;
        this.orderMapper = orderMapper;
        this.walletService = walletService;
        this.offerMapper = offerMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> register(@RequestBody CustomerRequestDTO customerRequestDTO){
        Customer customer = CustomerMapper.INSTANCE.DToToModel(customerRequestDTO);
        Customer savedCustomer = customerService.saveRequest(customer);
        CustomerResponseDTO responseDTO = CustomerMapper.INSTANCE.modelToDTO(savedCustomer);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @PutMapping("/updatePassword")
    public ResponseEntity<CustomerResponseDTO> updatePassword(@RequestParam(name = "email") String email,
                                                      @RequestParam(name = "oldPassword") String oldPassword,
                                                      @RequestParam(name = "newPassword") String newPassword){
        customerService.updatePassword(email,oldPassword,newPassword);
        Optional<Customer> customer = customerService.signIn(email,newPassword);
        CustomerResponseDTO responseDTO = CustomerMapper.INSTANCE.modelToDTO(
                customer.orElseThrow(()-> new EntityNotFoundException(
                String.format("user with email %s not found",email))));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/updatePasswordQuery")
    public ResponseEntity<Integer> updatePasswordQuery(@RequestParam(name = "email") String email,
                                                       @RequestParam(name = "oldPassword") String oldPassword,
                                                       @RequestParam(name = "newPassword") String newPassword){


        int affectedRows = customerService.updatePasswordQuery(email, oldPassword, newPassword);
        return new ResponseEntity<>(affectedRows,HttpStatus.OK);
    }

    @GetMapping("/signIn")
    public ResponseEntity<CustomerResponseDTO> SignIn(@RequestParam(name = "email") String email,
                                                              @RequestParam(name = "password") String Password){

        Optional<Customer> customer = customerService.signIn(email,Password);

        CustomerResponseDTO responseDTO = CustomerMapper.INSTANCE.modelToDTO(
                customer.orElseThrow(()-> new EntityNotFoundException(
                        String.format("user with email %s or entered password not found" +
                                "(wrong email or password)",email))));
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponseDTO> orderRegister(@RequestParam(name = "category") String categoryName,
                                                             @RequestParam(name = "subCategory") String subCategoryName,

                                                             @RequestBody OrderRequestDTO requestDTO){

        Order order = OrderMapper.INSTANCE.DToToModel(requestDTO);


        Order savedOrder = customerService.orderRequest(categoryName,subCategoryName, requestDTO.customerEmail(), order);
        OrderResponseDTO responseDTO = OrderMapper.INSTANCE.modelToDTO(savedOrder);
        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);

/*        Customer customer = findCustomerByEmail(requestDTO.customerEmail());

        Category category = findCategoryByName(categoryName);

        SubCategory subCategory = findSubCategoryByName(subCategoryName,category);

        order.setOrderStatus(OrderStatus.AWAITING_FOR_SPECIALIST_OFFER);
        order.setSubCategory(subCategory);
        order.setCustomer(customer);*/

    }

    @GetMapping("/allCategories")
    public ResponseEntity<List<CategoryResponseDTO>> allCategories(){
        List<Category> categories = customerService.seeAllCategories();
        if (categories.isEmpty())
            throw new EntityNotFoundException("no category Found");

        List<CategoryResponseDTO> responseDTOList = categories.stream()
                .map(CategoryMapper.INSTANCE::modelToDTO).toList();



        /*= new ArrayList<>();
        for (Category c : categories ){
            responseDTOList.add(CategoryMapper.INSTANCE.modelToDTO(c));
        }*/

        return new ResponseEntity<>(responseDTOList,HttpStatus.OK);
    }

    @GetMapping("/{category}/allSubCategories")
    public ResponseEntity<List<SubCategoryResponseDTO>> allCategorySubCategories(@PathVariable("category")String categoryName){
        Category category = findCategoryByName(categoryName);

        List<SubCategoryResponseDTO> responseDTOList = customerService
                .seeAllSubCategoriesOfSpecificCategory(category).stream()
                .map(SubCategoryMapper.INSTANCE::modelToDTO).toList();


        /*        if (subCategories.isEmpty())
            throw new EntityNotFoundException("no category Found");

        List<SubCategoryResponseDTO> responseDTOList = new ArrayList<>();
        for (SubCategory s : subCategories ){
            responseDTOList.add(SubCategoryMapper.INSTANCE.modelToDTO(s));
        }*/

        return new ResponseEntity<>(responseDTOList,HttpStatus.OK);
    }


    @GetMapping("/allOffers/{orderId}")
    public ResponseEntity<List<OfferResponseDTO>> allOffersForOrder(@PathVariable("orderId")long orderId)
    {
        List<OfferResponseDTO> offers = customerService.findAllOffersByOrderById(orderId);

        return new ResponseEntity<>(offers,HttpStatus.OK);
    }

    @PutMapping("/chooseOffer")
    public ResponseEntity<OfferResponseDTO> chooseOffer(@RequestParam("offerId") long offerId){

        return new ResponseEntity<>(offerMapper.modelToDTO(customerService.chooseOfferById(offerId))
                ,HttpStatus.OK);
    }


    @GetMapping("/allOffers/orderedByPrice/{orderId}")
    public ResponseEntity<List<OfferResponseDTO>> offersOrderedByPrice(@PathVariable("orderId")long orderId){

        List<OfferResponseDTO> offers = customerService.findAllOffersByOrderById(orderId).stream()
                .sorted(Comparator.comparing(OfferResponseDTO::specialistPrice)).toList();

        return new ResponseEntity<>(offers,HttpStatus.OK);

    }


    @GetMapping("/allOffers/orderedBySpecialistScore/{orderId}")
    public ResponseEntity<List<OfferResponseDTO>> offersOrderedBySpecialistScore(@PathVariable("orderId")long orderId){

        List<OfferResponseDTO> offers = customerService.findAllOffersByOrderById(orderId).stream()
                .sorted(Comparator.comparing(OfferResponseDTO::specialistScore)).toList();

        return new ResponseEntity<>(offers,HttpStatus.OK);
    }

    @PutMapping("/setStatusArrived")
    public ResponseEntity<OrderResponseDTO> setOrderStatusToArrived(@RequestParam("orderId") long orderId){

        OrderResponseDTO responseDTO = orderMapper.modelToDTO(customerService.setStatusToArrived(customerService.
                findOrderById(orderId)));

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/setStatusDone")
    public ResponseEntity<OrderResponseDTO> setOrderStatusToDone(@RequestParam("orderId") long orderId,
                                                                 @RequestBody CustomerFinishTimeDTO finishTime){

        OrderResponseDTO responseDTO = orderMapper.modelToDTO(customerService.setOrderStatusToDone(customerService.
                findOrderById(orderId),finishTime.finishTime()));

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/rating")
    public ResponseEntity<RatingResponseDTO> giveRating(@Valid @RequestBody RatingRequestDTO requestDTO){

        Rating savedRating = customerService.giveRating(customerService.findOrderById(requestDTO.orderId()),
                requestDTO.score(),requestDTO.comment());

        RatingResponseDTO responseDTO = RatingMapper.INSTANCE.modelToDTO(savedRating);

        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }


    @PutMapping("/onlinePayment")
    @CrossOrigin("*")
    public ResponseEntity<Integer> onlinePayment(@Valid
                                                            @RequestBody CardDto cardDto){
        //System.out.println(cardDto);

       // System.out.println(captchaMap);
        //System.out.println(cardDto.captcha());

        String captchaText = captchaMap.remove(cardDto.captchaId());


        if (captchaText == null){
            System.out.println("captcha revoked plz reload captcha");
            throw new CaptchaRevokedException("captcha revoked plz reload captcha");
        }

        if (!captchaText.equalsIgnoreCase(cardDto.captcha())){
            System.out.println("wrong captcha text");
            throw new InvalidCaptchaTextException("wrong captcha text");
        }


        Offer offer = customerService.returnAcceptedOffer(cardDto.orderId());
        int affectedRows = walletService.payOnline(offer.getSpecialist().getId(),
                offer.getSpecialistPrice(),offer.getOrder().getId());

        return new ResponseEntity<>(affectedRows,HttpStatus.OK);
    }


    @PutMapping("/walletPayment")
    public ResponseEntity<Integer> walletPayment(@RequestParam("orderId") long orderId,
                                                            @RequestParam("customerId") long customerId){
        Offer offer = customerService.returnAcceptedOffer(orderId);
        if (offer.getOrder().getOrderStatus() == OrderStatus.PAYED)
            throw new AlreadyPayedException("order with id %s already payed".formatted(orderId));
        int affectedRows = walletService.payWithWallet(offer.getOrder().getCustomer().getId(),
                offer.getSpecialistPrice(),
                offer.getSpecialist().getId(),offer.getOrder().getId());


        return new ResponseEntity<>(affectedRows,HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        double balance = customerService.getBalance(email);

        return new ResponseEntity<>(balance,HttpStatus.OK);
    }

    @PutMapping("/filtered-orders")
    public ResponseEntity<List<OrderResponseDTO>> filteredOrders(@RequestBody OrderStatusRequestDTO requestDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<OrderResponseDTO> responseDTO = customerService.filteredOrders(email, requestDTO.orderStatus())
                .stream().map(orderMapper::modelToDTO).toList();

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }


    @GetMapping("/captcha")
    @CrossOrigin("*")
    public Captcha generatedCaptcha(){
        SpecCaptcha captcha = new SpecCaptcha(130,48);
        int id = counter.incrementAndGet();
        captchaMap.put(id,captcha.text());
        System.out.println(captchaMap);
        return new Captcha(id, captcha.toBase64());
    }

    public Customer findCustomerByEmail(String email){
        Optional<Customer> optionalCustomer = customerService.findByEmail(email);

        return optionalCustomer.orElseThrow(() -> new EntityNotFoundException(
                String.format("user with email %s not found",email)));
    }

    public record Captcha(Integer id, String base64){

    }
}
