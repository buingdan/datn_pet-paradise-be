package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Services.MapValidationErrorService;
import com.example.petparadisebe.Services.UserService;
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

import java.util.List;
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
    @PutMapping("/{id}")
    //PUT http://localhost:8090/api/v1/users/5
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @ModelAttribute UserDto dto,
                                           BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if(responseEntity != null){
            return  responseEntity;
        }

        User entity = userService.updateUser(id, dto);

        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setDelete(entity.isDelete());
        dto.setCreateDate(entity.getCreateDate());
        dto.setPhoneNumber(entity.getPhoneNumber());

        return  new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<User>> getUsersRoles() {
        List<User> list = userService.findAllWithRoles();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/get/find")
    //GET http://localhost:8090/api/v1/users/get/find?query&&sortData&&sortType&&currentPage=1&&limit=10
    public ResponseEntity<?> getUsers(@RequestParam(value = "query", defaultValue = "") String query,
                                        @RequestParam(value = "sortData", defaultValue = "id") String sortData,
                                        @RequestParam(value = "sortType", defaultValue = "asc") String sortType,
                                        @RequestParam(value = "currentPage", defaultValue = "1") Long currentPage,
                                        @RequestParam(value = "limit", defaultValue = "9") Long limit) {
        return new ResponseEntity<>(userService.getUsers(query, currentPage, limit, sortData, sortType), HttpStatus.OK);
    }
}

