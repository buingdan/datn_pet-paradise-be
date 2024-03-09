package com.example.petparadisebe.Controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Services.FileStorageService;
import com.example.petparadisebe.Services.MapValidationErrorService;
import com.example.petparadisebe.Services.ProductService;
import com.example.petparadisebe.dto.ProductDto;
import com.example.petparadisebe.exception.FileStorageException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    // POST http://localhost:8090/api/v1/products
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductDto dto,
                                                BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if(responseEntity != null){
            return  responseEntity;
        }

        Product entity = productService.insertProduct(dto);

        dto.setId(entity.getId());
        dto.setImage(entity.getImage());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDelete(false);
//        dto.setCreateDate(LocalDateTime.now());
        dto.setQuantityInStock(entity.getQuantityInStock());
        dto.setDiscount(entity.getDiscount());
//        dto.setVoteAverage(entity.getVoteAverage());

        if (entity.getCategory() != null) {
            dto.getCategory().setId(entity.getCategory().getId());
        }

        return  new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/logo/{filename:.+}")
    //GET http://localhost:8090/api/v1/products/logo/fb0518fb-752e-415d-862e-bce446fbbbcf.jpg
    public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadLogoFileAsResource(filename);

        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception ex){
            throw new FileStorageException("Không thể xác định loại tệp!");
        }

        if(contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping
    //GET http://localhost:8090/api/v1/products
    public  ResponseEntity<?> getProduct(){
        var list = productService.findAll();
        var newlist = list.stream().map(item ->{
            ProductDto dto = new ProductDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @GetMapping("/find")
    //GET http://localhost:8090/api/v1/product/find?query&&size=5
    public  ResponseEntity<?> getProduct(@RequestParam("query") String query,
                                              @PageableDefault(size = 2, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        var list = productService.findByName(query, pageable);
        var newlist = list.getContent().stream().map(item ->{
            ProductDto dto = new ProductDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());

        var newPage = new PageImpl<ProductDto>(newlist, list.getPageable(), list.getTotalPages());
        return new ResponseEntity<>(newPage, HttpStatus.OK);
    }
    @GetMapping("/get/find")
    //GET http://localhost:8090/api/v1/products/get/find?query&&sortData&&sortType&&currentPage=1&&limit=10
    public ResponseEntity<?> getProduct(@RequestParam(value = "query", defaultValue = "") String query,
                                    @RequestParam(value = "sortData", defaultValue = "id") String sortData,
                                    @RequestParam(value = "sortType", defaultValue = "asc") String sortType,
                                    @RequestParam(value = "currentPage", defaultValue = "1") Long currentPage,
                                    @RequestParam(value = "limit", defaultValue = "9") Long limit) {
        return new ResponseEntity<>(productService.getProduct(query, currentPage, limit, sortData, sortType), HttpStatus.OK);
    }

    @GetMapping("/page")
    //GET http://localhost:8090/api/v1/products/page?page=0&sort=id&size=2
    public  ResponseEntity<?> getProduct( @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC)
                                               Pageable pageable){
        var list = productService.findAll(pageable);
        var newlist = list.stream().map(item ->{
            ProductDto dto = new ProductDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newlist, HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    //GET http://localhost:8090/api/v1/products/1/get
    public  ResponseEntity<?> getProduct(@PathVariable Long id){
        var entity = productService.findById(id);
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(entity, dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    //DELETE http://localhost:8090/api/v1/products/1
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id
    ){
        productService.deleteById(id);
        return new ResponseEntity<>("Xóa sản phẩm thành công!", HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    //PUT http://localhost:8090/api/v1/products/1
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @ModelAttribute ProductDto dto,
                                                BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if(responseEntity != null){
            return  responseEntity;
        }

        Product entity = productService.updateProduct(id, dto);

        dto.setId(entity.getId());
        dto.setImage(entity.getImage());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDelete(entity.isDelete());
        dto.setCreateDate(entity.getCreateDate());
        dto.setQuantityInStock(entity.getQuantityInStock());
        dto.setDiscount(entity.getDiscount());
        dto.setCategory(entity.getCategory());
//        dto.setVoteAverage(entity.getVoteAverage());

        return  new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getCatProducts(@PathVariable Long categoryId) {
        List<Product> Products = productService.getProductsByCategoryId(categoryId);
        return new ResponseEntity<>(Products, HttpStatus.OK);
    }
}


