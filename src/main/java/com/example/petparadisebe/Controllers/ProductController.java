package com.example.petparadisebe.Controller;

import java.util.List;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private  ProductService productService;
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return productService.getProductsByCategory(category);
    }

}

