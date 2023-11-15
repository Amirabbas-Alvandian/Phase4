package com.example.phase4.controller;

import com.example.phase4.dto.UserOrdersDTO;
import com.example.phase4.dto.request.FilteredUserRequestDTO;
import com.example.phase4.dto.request.FilteredOrdersRequestDTO;
import com.example.phase4.dto.request.FilteredUsers2;
import com.example.phase4.dto.response.*;
import com.example.phase4.entity.Specialist;
import com.example.phase4.entity.SubCategory;
import com.example.phase4.mapper.OrderMapper;
import com.example.phase4.mapper.SpecialistMapper;
import com.example.phase4.mapper.SubCategoryMapper;
import com.example.phase4.mapper.UserMapper;
import com.example.phase4.service.AdminService;
import com.example.phase4.service.CategoryService;
import com.example.phase4.service.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController extends usefulMethods{

    private final AdminService adminService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    public AdminController(AdminService adminService, SubCategoryService service, CategoryService categoryService,
                           OrderMapper orderMapper,
                           UserMapper userMapper) {
        super(service, categoryService);
        this.adminService = adminService;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    @PutMapping("/addSpecialistSubCategory/{subCategoryId}")
    public ResponseEntity<SpecialistResponseDTO> addSpecialistSubCategory(@PathVariable(name = "subCategoryId")long subCategoryId,
                                                                          @RequestParam(name = "id") long SpecialistId){

        Specialist specialist = adminService.addSubCategoryForSpecialistWithId(SpecialistId, subCategoryId);

        SpecialistResponseDTO responseDTO = SpecialistMapper.INSTANCE.modelToDTO(specialist);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @PutMapping("/deleteSpecialistSubCategory")
    public ResponseEntity<SpecialistResponseDTO> deleteSpecialistSubCategory(@RequestParam(name = "categoryName")String categoryName,
                                                                          @RequestParam(name = "subCategoryName")String subCategoryName,
                                                                             @RequestParam(name = "specialistEmail") String specialistEmail){

        /*Specialist specialist =*/ adminService.deleteSubCategoryForSpecialist(specialistEmail,subCategoryName,categoryName);

        /*SpecialistResponseDTO responseDTO = SpecialistMapper.INSTANCE.modelToDTO(specialist);*/

        return new ResponseEntity<>(/*responseDTO,*/ HttpStatus.OK);
    }

    @PutMapping("/updateSubCategoryPrice/{categoryName}/{subcategoryName}")
    public ResponseEntity<SubCategoryResponseDTO> updateSubCategoryPrice(@PathVariable("subcategoryName") String subcategoryName,
                                                              @PathVariable("categoryName") String categoryName,
                                                              @RequestParam double newPrice){
        SubCategory subCategory = adminService.changeSubCategoryPrice(subcategoryName,newPrice,categoryName);

        SubCategoryResponseDTO responseDTO = SubCategoryMapper.INSTANCE.modelToDTO(subCategory);

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/updateSubCategoryDescription/{categoryName}/{subcategoryName}")
    public ResponseEntity<SubCategoryResponseDTO> updateSubCategoryDescription(@PathVariable("subcategoryName") String subcategoryName,
                                                                    @PathVariable("categoryName") String categoryName,
                                                                    @RequestBody String newDescription){

        SubCategory subCategory = adminService.changeSubCategoryDescription(subcategoryName,newDescription,categoryName);

        SubCategoryResponseDTO responseDTO = SubCategoryMapper.INSTANCE.modelToDTO(subCategory);

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/filterSearch")
    public ResponseEntity<List<FilteredUserResponseDTO>> filteredUsers (@Valid @RequestBody FilteredUserRequestDTO requestDTO){

        List<FilteredUserResponseDTO> responseDTOList = adminService.filteredUser(requestDTO).stream()
                .map(UserMapper.INSTANCE::modelToDto).toList();

        return new ResponseEntity<>(responseDTOList,HttpStatus.OK);
    }

    @PutMapping("/verifySpecialist")
    public ResponseEntity<List<FilteredUserResponseDTO>> verifySpecialist (@RequestParam String email){

        adminService.setSpecialistStatusToVerified(email);


        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/finished-orders")
    public ResponseEntity<List<UserOrdersDTO>> finishedOrders(@RequestParam String userEmail){

        List<UserOrdersDTO> responseDTP = adminService.userOrders(userEmail);

        return new ResponseEntity<>(responseDTP,HttpStatus.OK);
    }

    @PutMapping("/filtered-orders")
    public ResponseEntity<List<OrderResponseDTO>> filteredOrders(@RequestBody FilteredOrdersRequestDTO requestDTO){

        List<OrderResponseDTO> responseDTO = adminService.filteredOrders(requestDTO)
                .stream().map(orderMapper::modelToDTO).toList();

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/filtered-users")
    public ResponseEntity<List<FilteredUserResponseDTO>> filteredUsers (@RequestBody FilteredUsers2 requestDTO){

        List<FilteredUserResponseDTO> responseDTO = adminService.filteredUsers2(requestDTO)
                .stream().map(userMapper::modelToDto).toList();

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
}
