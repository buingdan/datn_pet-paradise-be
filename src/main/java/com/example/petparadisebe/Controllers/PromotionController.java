package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Promotion;
import com.example.petparadisebe.Services.CategoryService;
import com.example.petparadisebe.Services.MapValidationErrorService;
import com.example.petparadisebe.Services.PromotionService;
import com.example.petparadisebe.dto.CategoryDto;
import com.example.petparadisebe.dto.PromotionDto;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/promotions")
public class PromotionController {
    @Autowired
    PromotionService promotionService;
    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    //POST http://localhost:8090/api/v1/promotions
    public ResponseEntity<?> createPromotion(@Valid @RequestBody PromotionDto dto,
                                           BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if(responseEntity != null){
            return responseEntity;
        }

        Promotion entity = promotionService.insertPromotion(dto);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDelete(false);
        dto.setCreateDate(LocalDateTime.now());
        dto.setDiscount(entity.getDiscount());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    //PUT http://localhost:8090//api/v1/promotions/1
    public ResponseEntity<?> updateCategory(@PathVariable("id")  Long id,
                                            @RequestBody PromotionDto dto){
        Promotion entity = new Promotion();
        BeanUtils.copyProperties(dto, entity);

        entity = promotionService.update(id, entity);

        dto.setId(entity.getId());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping()
    //GET http://localhost:8090//api/v1/promotions
    public  ResponseEntity<?> getPromotion(){
        var list = promotionService.findAll();
        var newlist = list.stream().map(item ->{
            PromotionDto dto = new PromotionDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @GetMapping("/page")
    //GET http://localhost:8090/api/v1/promotions/page?page=0&sort=id&size=2
    public  ResponseEntity<?> getCategories( @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC)
                                             Pageable pageable){
        var list = promotionService.findAll(pageable);
        var newlist = list.stream().map(item ->{
            PromotionDto dto = new PromotionDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    //GET http://localhost:8090/api/v1/promotions/1/get
    public  ResponseEntity<?> getCategories(@PathVariable Long id){
        var entity = promotionService.findById(id);
        PromotionDto dto = new PromotionDto();
        BeanUtils.copyProperties(entity, dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    //DELETE http://localhost:8090/api/v1/promotions/1
    public ResponseEntity<?> deleteCategories(@PathVariable("id") Long id
    ){
        promotionService.deleteById(id);
        return new ResponseEntity<>("Chương trình khuyến mại số " + id + " đã bị xóa", HttpStatus.OK);
    }

    @GetMapping("/get/find")
    //GET http://localhost:8090/api/v1/promotions/get/find?query&&sortData&&sortType&&currentPage=1&&limit=10
    public ResponseEntity<?> getProducts(@RequestParam(value = "query", defaultValue = "") String query,
                                         @RequestParam(value = "sortData", defaultValue = "id") String sortData,
                                         @RequestParam(value = "sortType", defaultValue = "asc") String sortType,
                                         @RequestParam(value = "currentPage", defaultValue = "1") Long currentPage,
                                         @RequestParam(value = "limit", defaultValue = "9") Long limit) {
        return new ResponseEntity<>(promotionService.getPromotions(query, currentPage, limit, sortData, sortType), HttpStatus.OK);
    }
}
