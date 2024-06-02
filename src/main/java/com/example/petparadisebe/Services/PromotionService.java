package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Promotion;
import com.example.petparadisebe.Repositories.CategoryRepository;
import com.example.petparadisebe.Repositories.PromotionRepository;
import com.example.petparadisebe.dto.CategoryDto;
import com.example.petparadisebe.dto.ContentDto;
import com.example.petparadisebe.dto.ProductDto;
import com.example.petparadisebe.dto.PromotionDto;
import com.example.petparadisebe.exception.CategoryException;
import com.example.petparadisebe.exception.ProductException;
import com.example.petparadisebe.exception.PromotionException;
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
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    public Promotion insertPromotion(PromotionDto dto) {
        List<?> foundlist = promotionRepository.findByNameContainsIgnoreCase(dto.getName());

        if (foundlist.size() > 0) {
            throw new PromotionException("Tên chương trình khuyến mại đã tồn tại ");
        }

        Promotion entity = new Promotion();

        BeanUtils.copyProperties(dto, entity);

        return promotionRepository.save(entity);
    }

    public Promotion update(Long id, Promotion entity) {
        Optional<Promotion> exited = promotionRepository.findById(id);

        if (exited.isEmpty()) {
            throw new CategoryException("Chương trình khuyến mại số " + id + " không tồn tại");
        }

        try {
            Promotion existedPromotion = exited.get();
            existedPromotion.setName(entity.getName());
            existedPromotion.setDiscount(entity.getDiscount());
            existedPromotion.setStartDate(entity.getStartDate());
            existedPromotion.setEndDate(entity.getEndDate());

            return promotionRepository.save(existedPromotion);
        } catch (Exception ex) {
            throw new CategoryException("Chương trình khuyến mại cập nhật thất bại");
        }
    }

    public List<Promotion> findAll() {
        return promotionRepository.findAll();
    }

    public Page<Promotion> findAll(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    public Promotion findById(Long id) {
        Optional<Promotion> found = promotionRepository.findById(id);

        if (found.isEmpty()) {
            throw new CategoryException("Chương trình khuyến mại số " + id + " không tồn tại");
        }
        return found.get();
    }

    public void deleteById(Long id) {
        Promotion existed = findById(id);
        promotionRepository.deleteById(id);
    }

    public ContentDto getPromotions(String query, Long currentPage, Long limit, String sortData, String sortType) {
        try {
            ContentDto contentDto = new ContentDto();
            currentPage -= 1;
            Pageable pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
            var list = promotionRepository.searchPromotion(query, pageable);
            var newlist = list.getContent().stream().map(item ->{
                PromotionDto dto = new PromotionDto();
                BeanUtils.copyProperties(item, dto);
                return dto;
            }).collect(Collectors.toList());
            Integer totalPromotion = Math.toIntExact(list.getTotalElements());
            Integer totalPagePromotion= Math.toIntExact(list.getTotalPages());
            if (currentPage.intValue() > totalPagePromotion) {
                currentPage = totalPromotion.longValue();
                pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
                list = promotionRepository.searchPromotion(query, pageable);
                newlist = list.getContent().stream().map(item ->{
                    PromotionDto dto = new PromotionDto();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).collect(Collectors.toList());
                totalPromotion = Math.toIntExact(list.getTotalElements());
            }
            contentDto.setList(newlist);
            contentDto.setCurrentPage(currentPage.intValue() + 1);
            contentDto.setTotalRecord(totalPromotion);
            return contentDto;
        } catch (Exception e) {
            throw new ProductException("Có lỗi lấy danh sách khuyễn mãi ");
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

