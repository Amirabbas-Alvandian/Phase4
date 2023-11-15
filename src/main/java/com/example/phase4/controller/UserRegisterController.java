package com.example.phase4.controller;

import com.example.phase4.dto.request.AdminRequestDTO;
import com.example.phase4.dto.request.CustomerRequestDTO;
import com.example.phase4.dto.request.SpecialistRequestDTO;
import com.example.phase4.dto.response.AdminResponseDTO;
import com.example.phase4.dto.response.CustomerResponseDTO;
import com.example.phase4.dto.response.FilteredUserResponseDTO;
import com.example.phase4.dto.response.SpecialistRegisterResponseDTO;
import com.example.phase4.dto.verifiedUserDTO;
import com.example.phase4.entity.*;
import com.example.phase4.entity.enums.Role;
import com.example.phase4.mapper.AdminMapper;
import com.example.phase4.mapper.CustomerMapper;
import com.example.phase4.mapper.SpecialistMapper;
import com.example.phase4.mapper.UserMapper;
import com.example.phase4.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class UserRegisterController {

    private final CustomerService customerService;

    private final SpecialistService specialistService;

    private final AdminService adminService;

    private final ConfirmationTokenService confirmationTokenService;

    private final UserService userService;

    private final JavaMailSender javaMailSender;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    public UserRegisterController(CustomerService customerService, SpecialistService specialistService, AdminService adminService, ConfirmationTokenService confirmationTokenService, UserService userService, JavaMailSender javaMailSender,
                                  AdminMapper adminMapper,
                                  UserMapper userMapper) {
        this.customerService = customerService;
        this.specialistService = specialistService;
        this.adminService = adminService;
        this.confirmationTokenService = confirmationTokenService;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerResponseDTO> register(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        Customer customer = CustomerMapper.INSTANCE.DToToModel(customerRequestDTO);
        Customer savedCustomer = customerService.saveRequest(customer);

        ConfirmationToken confirmationToken = new ConfirmationToken(savedCustomer);

        confirmationTokenService.saveOrUpdate(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(savedCustomer.getEmail());
        mailMessage.setSubject("Account Verification!");
        mailMessage.setFrom("Alvandiyan77@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/register/confirm-user?token="+confirmationToken.getConfirmationToken());

        javaMailSender.send(mailMessage);
        CustomerResponseDTO responseDTO = CustomerMapper.INSTANCE.modelToDTO(savedCustomer);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<AdminResponseDTO> register(@Valid @RequestBody AdminRequestDTO requestDTO){
        Admin admin = AdminMapper.INSTANCE.DToToModel(requestDTO);

        Admin savedAdmin = adminService.saveRequest(admin);

        ConfirmationToken confirmationToken = new ConfirmationToken(savedAdmin);

        confirmationTokenService.saveOrUpdate(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(savedAdmin.getEmail());
        mailMessage.setSubject("Account Verification!");
        mailMessage.setFrom("Alvandiyan77@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/register/confirm-user?token="+confirmationToken.getConfirmationToken());

        javaMailSender.send(mailMessage);


        return new ResponseEntity<>(adminMapper.modelToDTO(savedAdmin), HttpStatus.CREATED);

    }


    @PostMapping(value = "/specialist",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SpecialistRegisterResponseDTO> register(@Valid @ModelAttribute SpecialistRequestDTO requestDTO
    ) {

        Specialist specialist = SpecialistMapper.INSTANCE.DToToModel(requestDTO);


        Specialist savedSpecialist = specialistService.saveRequest(specialist);


        ConfirmationToken confirmationToken = new ConfirmationToken(savedSpecialist);

        confirmationTokenService.saveOrUpdate(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(savedSpecialist.getEmail());
        mailMessage.setSubject("Account Verification!");
        mailMessage.setFrom("Alvandiyan77@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/register/confirm-user?token="+confirmationToken.getConfirmationToken());

        javaMailSender.send(mailMessage);
        SpecialistRegisterResponseDTO registerResponseDTO = SpecialistMapper.INSTANCE.registeredModelToDTO(savedSpecialist);

        return new ResponseEntity<>(registerResponseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/confirm-user")
    public ResponseEntity<String> conformUser(@RequestParam("token")String confirmationToken){

        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);

        System.out.println(token);
        User user = token.getUser();

        int affectedRows = userService.enableUser(user);

        if (user.getRole() == Role.ROLE_SPECIALIST)
            specialistService.setStatusToAwaitingVerification(user.getEmail());

        System.out.println(affectedRows);

        return new ResponseEntity<>("email %s verified".formatted(user.getEmail()),HttpStatus.OK );
    }


    @GetMapping("/temp")
    public ResponseEntity<FilteredUserResponseDTO> temp(){
        User byEmail = userService.findByEmail("specialistRegister8@gmail.com");
        System.out.println(byEmail);
        return new ResponseEntity<>(userMapper.modelToDto(byEmail),HttpStatus.OK);
    }
}
