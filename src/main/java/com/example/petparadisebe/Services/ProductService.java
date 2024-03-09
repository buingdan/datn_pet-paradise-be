package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Repositories.ProductRepository;
import com.example.petparadisebe.dto.ContentDto;
import com.example.petparadisebe.dto.ProductDto;
import com.example.petparadisebe.exception.ProductException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Product insertProduct(ProductDto dto){
        List<?> foundlist = productRepository.findByNameContainsIgnoreCase(dto.getName());

        if(foundlist.size() > 0){
            throw new ProductException("Tên sản phẩm đã tồn tại ");
        }

        Product entity = new Product();

        BeanUtils.copyProperties(dto, entity);

        if(dto.getImgFile() != null){
            String filename = fileStorageService.storeLogoFile(dto.getImgFile());

            entity.setImage(filename);
            dto.setImgFile(null);
        }

        return productRepository.save(entity);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findByName(String name, Pageable pageable) {
        return productRepository.findByNameContainsIgnoreCase(name, pageable);
    }

    public Product findById(Long id) {
        Optional<Product> found = productRepository.findById(id);

        if(found.isEmpty()){
            throw new ProductException("Sản phẩm không tồn tại");
        }
        return found.get();
    }

    public void deleteById(Long id) {
        Product existed = findById(id);
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, ProductDto dto){
        var found = productRepository.findById(id);

        if(found.isEmpty()){
            throw new ProductException("Tên sản phẩm không tồn tại ");
        }

//        Product entity = new Product();
        Product existingProduct = found.get();
//        BeanUtils.copyProperties(dto, entity);
        BeanUtils.copyProperties(dto, existingProduct, "createDate");

//        if(dto.getImgFile() != null){
//            String filename = fileStorageService.storeLogoFile(dto.getImgFile());
//
//            entity.setImage(filename);
//            dto.setImgFile(null);
//        }

//        return productRepository.save(entity);
        if (dto.getImgFile() != null) {
            String filename = fileStorageService.storeLogoFile(dto.getImgFile());
            existingProduct.setImage(filename);
        }

        return productRepository.save(existingProduct);
    }
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryIdAndIsDeleteFalse(categoryId);
    }
    public ContentDto getProduct(String query, Long currentPage, Long limit, String sortData, String sortType) {
        try {
            ContentDto contentDto = new ContentDto();
            currentPage -= 1;
            Pageable pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
            var list = productRepository.searchProduct(query, pageable);
            var newlist = list.getContent().stream().map(item ->{
                ProductDto dto = new ProductDto();
                BeanUtils.copyProperties(item, dto);
                return dto;
            }).collect(Collectors.toList());
            Integer totalProduct = Math.toIntExact(list.getTotalElements());
            Integer totalPageJob = Math.toIntExact(list.getTotalPages());
            if (currentPage.intValue() > totalPageJob) {
                currentPage = totalPageJob.longValue();
                pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
                list = productRepository.searchProduct(query, pageable);
                newlist = list.getContent().stream().map(item ->{
                    ProductDto dto = new ProductDto();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).collect(Collectors.toList());
                totalProduct = Math.toIntExact(list.getTotalElements());
            }
            contentDto.setList(newlist);
            contentDto.setCurrentPage(currentPage.intValue() + 1);
            contentDto.setTotalRecord(totalProduct);
            return contentDto;
        } catch (Exception e) {
            throw new ProductException("Có lỗi lấy danh sách sản phẩm ");
        }
    }
    public List<Sort.Order> sortOrder(String sort, String sortDirection) {
        System.out.println(sortDirection);
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        if (sortDirection != null) {
            direction = Sort.Direction.fromString(sortDirection);
        } else {
            direction = Sort.Direction.DESC;
        }
        sorts.add(new Sort.Order(direction, sort));
        return sorts;
    }
}
