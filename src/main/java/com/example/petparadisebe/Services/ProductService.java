package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Repositories.ProductRepository;
import com.example.petparadisebe.dto.ProductDto;
import com.example.petparadisebe.exception.ProductException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private  FileStorageService fileStorageService;

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
            throw new ProductException("Sản phẩm số " + id + " không tồn tại");
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
            throw new ProductException("Tên manufacturer không tồn tại ");
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
}
