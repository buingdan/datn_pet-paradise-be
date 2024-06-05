package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Entities.Order;
import com.example.petparadisebe.Entities.Promotion;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Services.MapValidationErrorService;
import com.example.petparadisebe.Services.OrderService;
import com.example.petparadisebe.Services.UserService;
import com.example.petparadisebe.dto.OrderDto;
import com.example.petparadisebe.dto.PromotionDto;
import com.example.petparadisebe.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @GetMapping()
    //GET http://localhost:8090/api/v1/orders
    public  ResponseEntity<?> getUsers(){
        var list = orderService.findAll();
        var newlist = list.stream().map(item ->{
            OrderDto dto = new OrderDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @PostMapping()
    //POST http://localhost:8090/api/v1/orders
    public ResponseEntity<?> updateOrder(@Valid @RequestBody OrderDto dto,
                                           BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if(responseEntity != null){
            return  responseEntity;
        }

        Order entity = orderService.insertOrder(dto);

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setDelete(entity.isDelete());
        dto.setCreateDate(entity.getCreateDate());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUser(entity.getUser());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/get/find")
    //GET http://localhost:8090/api/v1/orders/get/find?query&&sortData&&sortType&&currentPage=1&&limit=10
    public ResponseEntity<?> getUsers(@RequestParam(value = "query", defaultValue = "") String query,
                                        @RequestParam(value = "sortData", defaultValue = "id") String sortData,
                                        @RequestParam(value = "sortType", defaultValue = "asc") String sortType,
                                        @RequestParam(value = "currentPage", defaultValue = "1") Long currentPage,
                                        @RequestParam(value = "limit", defaultValue = "9") Long limit) {
        return new ResponseEntity<>(orderService.getOrders(query, currentPage, limit, sortData, sortType), HttpStatus.OK);
    }
}

