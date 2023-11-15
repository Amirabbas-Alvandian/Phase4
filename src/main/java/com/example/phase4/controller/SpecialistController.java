package com.example.phase4.controller;

import com.example.phase4.dto.request.OfferRequestDTO;
import com.example.phase4.dto.request.OrderStatusRequestDTO;
import com.example.phase4.dto.request.SpecialistRequestDTO;
import com.example.phase4.dto.response.*;
import com.example.phase4.entity.*;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.mapper.OfferMapper;
import com.example.phase4.mapper.OrderMapper;
import com.example.phase4.mapper.SpecialistMapper;
import com.example.phase4.service.SpecialistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/specialist")
public class SpecialistController {

    private final SpecialistService specialistService;
    private final SpecialistMapper specialistMapper;
    private final OrderMapper orderMapper;

    public SpecialistController(SpecialistService specialistService,
                                SpecialistMapper specialistMapper,
                                OrderMapper orderMapper) {
        this.specialistService = specialistService;
        this.specialistMapper = specialistMapper;
        this.orderMapper = orderMapper;
    }


    @PostMapping(value = "/register",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SpecialistRegisterResponseDTO> register(@Valid @ModelAttribute SpecialistRequestDTO requestDTO
                                                                    ) {

        Specialist specialist = SpecialistMapper.INSTANCE.DToToModel(requestDTO);


        Specialist savedSpecialist = specialistService.saveRequest(specialist);

        SpecialistRegisterResponseDTO registerResponseDTO = SpecialistMapper.INSTANCE.registeredModelToDTO(savedSpecialist);

        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/RelatedOrders")
    public ResponseEntity<List<OrderResponseDTO>> allRelatedOrders(@RequestParam("specialistId")long id){

        List<OrderResponseDTO> orders = specialistService.requestRelatedOrders(id);

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PostMapping("/giveOffer")
    public ResponseEntity<OfferResponseDTO> giveOffer(/*@PathVariable("orderId") long orderId,
                                                      @RequestParam("specialistId") long specialistId,*/
                                                      @RequestBody OfferRequestDTO requestDTO){

        Offer offer = specialistService.requestOffer(requestDTO);

        OfferResponseDTO responseDTO = OfferMapper.INSTANCE.modelToDTO(offer);

        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/signIn")
    public ResponseEntity<SpecialistResponseDTO> signIn(@RequestParam("email") String email,
                                                        @RequestParam("password") String password){
        SpecialistResponseDTO responseDTO = specialistService.signIn(email, password)
                .map(specialistMapper::modelToDTO).orElseThrow(
                        () -> new EntityNotFoundException(String.format(
                                "specialist with email %s not found",email
                        ))
                );

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/seeRating")
    public ResponseEntity<Integer> seeRating(@RequestParam("orderId")long orderId){
        int score = specialistService.seeRating(orderId);
        return new ResponseEntity<>(score,HttpStatus.OK);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<CustomerResponseDTO> updatePassword(@RequestParam(name = "email") String email,
                                                              @RequestParam(name = "oldPassword") String oldPassword,
                                                              @RequestParam(name = "newPassword") String newPassword){
        specialistService.setNewPassword(email,oldPassword,newPassword);
        Optional<Specialist> specialist = specialistService.signIn(email,newPassword);
        SpecialistResponseDTO responseDTO = SpecialistMapper.INSTANCE.modelToDTO(
                specialist.orElseThrow(()-> new EntityNotFoundException(
                        String.format("user with email %s not found",email))));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(email);
        double balance = specialistService.getBalance(email);

        return new ResponseEntity<>(balance,HttpStatus.OK);
    }

    @PutMapping("/filtered-orders")
    public ResponseEntity<List<OrderResponseDTO>> filteredOrders(@RequestBody OrderStatusRequestDTO requestDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<OrderResponseDTO> responseDTO = specialistService.filteredOrders(email, requestDTO.orderStatus())
                .stream().map(orderMapper::modelToDTO).toList();

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
}
