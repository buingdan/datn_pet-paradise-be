package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Services.CategoryService;
import com.example.petparadisebe.Services.MapValidationErrorService;
import com.example.petparadisebe.dto.CategoryDto;
import com.example.petparadisebe.dto.ProductDto;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    MapValidationErrorService mapValidationErrorService;

    //nếu truyền vào là 1 object thì data tranfer object = request object ở đây lag dto
    @PostMapping()
    //POST http://localhost:8090//api/v1/categories
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute CategoryDto dto,
                                           BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if(responseEntity != null){
            return responseEntity;
        }

        Category entity = categoryService.insertCategory(dto);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDelete(false);
        dto.setCreateDate(LocalDateTime.now());
        return  new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    //PUT http://localhost:8090//api/v1/categories/1
    public ResponseEntity<?> updateCategory(@PathVariable("id")  Long id,
                                            @RequestBody CategoryDto dto){
        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);

        entity = categoryService.update(id, entity);

        dto.setId(entity.getId());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping()
    //GET http://localhost:8090//api/v1/categories
    public  ResponseEntity<?> getCategories(){
        var list = categoryService.findAll();
        var newlist = list.stream().map(item ->{
            CategoryDto dto = new CategoryDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @GetMapping("/page")
    //GET http://localhost:8090/api/v1/categories/page?page=0&sort=id&size=2
    public  ResponseEntity<?> getCategories( @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC)
                                          Pageable pageable){
        var list = categoryService.findAll(pageable);
        var newlist = list.stream().map(item ->{
            CategoryDto dto = new CategoryDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    //GET http://localhost:8090/api/v1/categories/1/get
    public  ResponseEntity<?> getCategories(@PathVariable Long id){
        var entity = categoryService.findById(id);
        CategoryDto dto = new CategoryDto();
        BeanUtils.copyProperties(entity, dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    //DELETE http://localhost:8090/api/v1/categories/1
    public ResponseEntity<?> deleteCategories(@PathVariable("id") Long id
    ){
        categoryService.deleteById(id);
        return new ResponseEntity<>("Loại sản phẩm số " + id + " đã bị xóa", HttpStatus.OK);
    }
}
