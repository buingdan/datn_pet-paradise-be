package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Services.CategoryService;
import com.example.petparadisebe.Services.MapValidationErrorService;
import com.example.petparadisebe.Services.UserService;
import com.example.petparadisebe.dto.CategoryDto;
import com.example.petparadisebe.dto.ProductDto;
import com.example.petparadisebe.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @GetMapping()
    //GET http://localhost:8090/api/v1/users
    public  ResponseEntity<?> getUsers(){
        var list = userService.findAll();
        var newlist = list.stream().map(item ->{
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @GetMapping("/page")
    //GET http://localhost:8090/api/v1/users/page?page=0&sort=id&size=2
    public  ResponseEntity<?> getUsers( @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC)
                                             Pageable pageable){
        var list = userService.findAll(pageable);
        var newlist = list.stream().map(item ->{
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

}

