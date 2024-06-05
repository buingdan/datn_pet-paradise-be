package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Order;
import com.example.petparadisebe.Entities.Promotion;
import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Repositories.OrderRepository;
import com.example.petparadisebe.Repositories.RoleRepository;
import com.example.petparadisebe.Repositories.UserRepository;
import com.example.petparadisebe.dto.ContentDto;
import com.example.petparadisebe.dto.OrderDto;
import com.example.petparadisebe.dto.PromotionDto;
import com.example.petparadisebe.dto.UserDto;
import com.example.petparadisebe.exception.ProductException;
import com.example.petparadisebe.exception.PromotionException;
import com.example.petparadisebe.exception.UsernameNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;


    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Long id, OrderDto dto){
        var found = orderRepository.findById(id);

        if(found.isEmpty()){
            throw new UsernameNotFoundException("Tên sản phẩm không tồn tại ");
        }
        Order existingOrder= found.get();
        BeanUtils.copyProperties(dto, existingOrder, "createDate");
        return orderRepository.save(existingOrder);
    }

    @Transactional
    public Order insertOrder(OrderDto dto) {
        Order entity = new Order();
        BeanUtils.copyProperties(dto, entity);
        return orderRepository.save(entity);
    }

    public ContentDto getOrders(String query, Long currentPage, Long limit, String sortData, String sortType) {
        try {
            ContentDto contentDto = new ContentDto();
            currentPage -= 1;
            Pageable pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
            var list = orderRepository.searchOrder(query, pageable);
            var newlist = list.getContent().stream().map(item ->{
                OrderDto dto = new OrderDto();
                BeanUtils.copyProperties(item, dto);
                return dto;
            }).collect(Collectors.toList());
            Integer totalProduct = Math.toIntExact(list.getTotalElements());
            Integer totalPageJob = Math.toIntExact(list.getTotalPages());
            if (currentPage.intValue() > totalPageJob) {
                currentPage = totalPageJob.longValue();
                pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
                list = orderRepository.searchOrder(query, pageable);
                newlist = list.getContent().stream().map(item ->{
                    OrderDto dto = new OrderDto();
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
            throw new ProductException("Có lỗi lấy danh sách yêu cầu ");
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
