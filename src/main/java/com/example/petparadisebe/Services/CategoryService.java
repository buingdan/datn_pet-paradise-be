package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Repositories.CategoryRepository;
import com.example.petparadisebe.dto.CategoryDto;
import com.example.petparadisebe.exception.CategoryException;
import com.example.petparadisebe.exception.ProductException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category insertCategory(CategoryDto dto) {
        List<?> foundlist = categoryRepository.findByNameContainsIgnoreCase(dto.getName());

        if (foundlist.size() > 0) {
            throw new ProductException("Tên danh mục đã tồn tại ");
        }

        Category entity = new Category();

        BeanUtils.copyProperties(dto, entity);

        return categoryRepository.save(entity);
    }

    public Category update(Long id, Category entity) {
        Optional<Category> exited = categoryRepository.findById(id);

        if (exited.isEmpty()) {
            throw new CategoryException("Loại sản phẩm số " + id + " không tồn tại");
        }

        try {
            Category existedCategory = exited.get();
            existedCategory.setName(entity.getName());

            return categoryRepository.save(existedCategory);
        } catch (Exception ex) {
            throw new CategoryException("Loại sản phẩm cập nhật thất bại");
        }
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findById(Long id) {
        Optional<Category> found = categoryRepository.findById(id);

        if (found.isEmpty()) {
            throw new CategoryException("Loại sản phẩm số " + id + " không tồn tại");
        }
        return found.get();
    }

    public void deleteById(Long id) {
        Category existed = findById(id);
        categoryRepository.deleteById(id);
    }
}

