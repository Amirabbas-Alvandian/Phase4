package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.dto.request.OfferRequestDTO;
import com.example.phase4.dto.response.OrderResponseDTO;
import com.example.phase4.entity.*;
import com.example.phase4.entity.enums.OrderStatus;
import com.example.phase4.entity.enums.Role;
import com.example.phase4.entity.enums.SpecialistStatus;
import com.example.phase4.exception.*;
import com.example.phase4.mapper.OrderMapper;
import com.example.phase4.repository.SpecialistRepository;
import com.example.phase4.service.OfferService;
import com.example.phase4.service.OrderService;
import com.example.phase4.service.SpecialistService;
import com.example.phase4.service.WalletService;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialistServiceImpl extends BaseServiceImpl<Specialist,Long> implements SpecialistService {

    private final SpecialistRepository repository;
    private final OrderService orderService;
    private final OfferService offerService;
    private final OrderMapper orderMapper;

    private final WalletService walletService;

    private final BCryptPasswordEncoder passwordEncoder;

    public SpecialistServiceImpl(SpecialistRepository repository,
                                 OrderService orderService,
                                 OfferService offerService,
                                 OrderMapper orderMapper, WalletService walletService, BCryptPasswordEncoder passwordEncoder) {
        super(repository);
        this.repository = repository;
        this.orderService = orderService;
        this.offerService = offerService;
        this.orderMapper = orderMapper;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Order> seeAllRelatedOrders(Specialist specialist) {
        return orderService.findAllRelatedAvailableOrders(specialist.getSubCategoryList());
    }

    @Override
    public Order pickOrder(List<Order> orders, int index) {
        try {
            return orders.get(index);

        } catch (IndexOutOfBoundsException i) {
            System.out.println(i.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public Offer giveOffer(Offer offer) {
        if (offer.getStartDate().isBefore(LocalDate.now()))
            throw new BadDateException("past date");

        if (offer.getOrder().getSubCategory().getBasePrice() > offer.getSpecialistPrice())
            throw new IllegalPriceException("specialist price lower than BasePrice");

        if (offer.getOrder().getCustomerPrice() > offer.getSpecialistPrice())
            throw new IllegalPriceException("specialist price lower than Customer price");

        if (offer.getStartDate().isBefore(offer.getOrder().getStartDate()))
            throw new BadDateException("Specialist date before Customer date");

        if (offer.getStartTime().isBefore(LocalTime.now()))
            throw new BadTimeException("Past start time");

        if (offer.getFinishTime().isBefore(offer.getStartTime()))
            throw new BadTimeException("finish time should be after start time");

        try {
            Offer checkExistence = offerService.saveOrUpdate(offer);
            if (checkExistence == null)
                throw new EntityNotSavedException("offer not saved");
            Order order = offer.getOrder();
            order.setOrderStatus(OrderStatus.AWAITING_CUSTOMER_CHOICE);
            orderService.saveOrUpdate(order);
            return offer;
        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    @Override
    public Boolean checkImageSize(Path path) {
        try {
            return Files.readAllBytes(path).length / 1024 <= 300;
        } catch (IOException i) {
            System.out.println(i.getMessage());
            return null;
        }
    }

    @Override
    public Specialist saveWithValidation(Specialist specialist, Path path) {
        if (specialist.getImage() != null) {
            Specialist temp = saveOrUpdate(specialist);
            if (temp != null){
                writeImageAfterSuccessfulSave(specialist.getImage(), path);
                return temp;
            }
        }

        return null;
    }

    @Override
    public boolean checkImageFormat(Path path) {
        String endOfPath = String.valueOf(path.getFileName());
        return endOfPath.endsWith(".jpg");
    }

    @Override
    public byte[] imageValidation(Path path) {
        try {
            if (!Files.exists(path))
                throw new WrongPathException("file doesnt exist");
            if (!checkImageFormat(path))
                throw new BadFormatException("only jpg format");
            if (!checkImageSize(path))
                throw new InvalidImageSizeException("size should be less than 300kb");

            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Path writeImageAfterSuccessfulSave(byte[] writeImage, Path path) {
        try {
            return Files.write(path, writeImage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Path readImageFromDateBase(Specialist specialist, Path readpath) {
        try {
            return Files.write(readpath, specialist.getImage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional
    public Optional<Specialist> signIn(String email, String oldPassword) {
        Optional<Specialist> specialist = repository.findByEmailAndPassword(email, oldPassword);

        Path path = Path.of(String.format("C:\\Users\\Alvandian\\IdeaProjects\\Phase3\\src" +
                "\\main\\resources\\image\\read\\%s.jpg",specialist.orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "specialist with email %s not found",email
                ))
        ).getId()));
        writeImageAfterSuccessfulSave(specialist.get().getImage(),path);

        return specialist;
    }

    @Override
    @Transactional
    @Modifying
    public void setNewPassword(String email, String oldPassword, String newPassword) {
        Optional<Specialist> specialist = signIn(email, oldPassword);
        specialist.ifPresent(s -> s.setPassword(newPassword));
    }

    @Override
    @Transactional
    public Optional<Specialist> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional
    public Specialist saveRequest(Specialist specialist) {
        specialist.setStatus(SpecialistStatus.NEW);
        specialist.setRole(Role.ROLE_SPECIALIST);
        specialist.setSignUpDate(LocalDate.now());
        specialist.setWallet(new Wallet(specialist));
        specialist.setPassword(passwordEncoder.encode(specialist.getPassword()));
        specialist.setSubCategoryList(new ArrayList<>());
        Specialist result =  saveOrUpdate(specialist);

        Path path = Path.of(String.format("C:\\Users\\Alvandian\\" +
                "IdeaProjects\\Phase3\\src\\main\\resources\\" +
                "image\\%s.jpg",specialist.getId()));
        writeImageAfterSuccessfulSave(specialist.getImage(),path);


        return result;
    }

    @Override
    public List<OrderResponseDTO> requestRelatedOrders(long specialistId) {
        Optional<Specialist> specialist = findById(specialistId);

        if (specialist.isEmpty())
            throw new EntityNotFoundException(String.format(
                    "specialist with id %s not found",specialist));

        if(specialist.get().getScore() < 0)
            throw new NegativeSpecialistScoreException(String.format("specialist with id %s " +
                            "has negative score (%s) therefore cant give any offer",specialist.get().getId()
                    ,specialist.get().getScore()));

        if (specialist.get().getStatus() != SpecialistStatus.VERIFIED)
            throw new SpecialistNotVerifiedException("specialist with id %s is not verified yet".formatted(
                    specialist.get().getId()
            ));

        List<Order> orders = seeAllRelatedOrders(specialist.orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "specialist with id %s not found",specialistId
                ))
        ));

        if (orders.isEmpty())
            throw new SpecialistSubCategoryNotFoundException("no subCategories");

        /*List<OrderResponseDTO> responseDTOList = new ArrayList<>();
        for (Order o : orders){
            responseDTOList.add(OrderMapper.INSTANCE.modelToDTO(o));
        }*/

        //return responseDTOList;
        return orders.stream().map(orderMapper::modelToDTO).toList();

    }

    @Override
    public Offer requestOffer(OfferRequestDTO requestDTO) {

        Optional<Order> order = orderService.findById(requestDTO.orderId());

        Optional<Specialist> specialist = findById(requestDTO.specialistId());

        SubCategory subCategory = order.orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "order with id %s not found",requestDTO.orderId()
                ))
        ).getSubCategory();


        if (specialist.isEmpty())
            throw new EntityNotFoundException(String.format(
                    "specialist with id %s not found",specialist));


        if(!specialist.get().getSubCategoryList().contains(subCategory))
            throw new UnRelatedOrderException("unrelated subCategory");

        if(specialist.get().getScore() < 0)
            throw new NegativeSpecialistScoreException(String.format("specialist with id %s " +
                    "has negative score (%s) therefore cant give any offer",specialist.get().getId()
                    ,specialist.get().getScore()));


        if (specialist.get().getStatus() != SpecialistStatus.VERIFIED)
            throw new SpecialistNotVerifiedException("specialist with id %s is not verified yet".formatted(
                    specialist.get().getId()
            ));


        return giveOffer( new Offer(specialist.get(),order.get(),requestDTO.SpecialistPrice(),
                requestDTO.startDate(),requestDTO.startTime(),requestDTO.finishTime()));

    }

    @Override
    public int seeRating(long orderId) {
        return orderService.findById(orderId).orElseThrow(
                        ()-> new EntityNotFoundException(
                                "order with id %s not found".formatted(orderId)
                        )
                )
                .getRating().getScore();
    }

    @Override
    @Transactional
    public double getBalance(String email) {
        long specialistId = findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("user with email %s not found".formatted(email))
        ).getId();
        return walletService.findByUserId(specialistId).getBalance();
    }

    @Override
    @Transactional
    public List<Order> filteredOrders(String email, OrderStatus orderStatus) {

        return offerService.filteredOrders(findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("user with email %s not found".formatted(email))
        ).getId(), orderStatus);
    }

    @Override
    @Transactional
    public int setStatusToAwaitingVerification(String email) {
        return repository.setStatusToAwaitingVerification(email);
    }
}
