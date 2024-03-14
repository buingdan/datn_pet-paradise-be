package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Repositories.RoleRepository;
import com.example.petparadisebe.Repositories.UserRepository;
import com.example.petparadisebe.dto.ContentDto;
import com.example.petparadisebe.dto.ProductDto;
import com.example.petparadisebe.dto.UserDto;
import com.example.petparadisebe.exception.ProductException;
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
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser (User user){
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return  userRepository.save(user);
    }

    public Role saveRole (Role role){
        return roleRepository.save(role);
    }

    public void addToUser (String username, String rolename){
        User user = userRepository.findByEmail(username).get();
        Role role = roleRepository.findByName(rolename);
        user.getRoles().add(role);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUser(Long id, UserDto dto){
        var found = userRepository.findById(id);

        if(found.isEmpty()){
            throw new UsernameNotFoundException("Tên sản phẩm không tồn tại ");
        }

//        User entity = new User();
        User existingUser= found.get();
//        BeanUtils.copyProperties(dto, entity);
        BeanUtils.copyProperties(dto, existingUser, "createDate");


        return userRepository.save(existingUser);
    }

    public List<User> findAllWithRoles() {
        return userRepository.findUsernamesAndRoleNames();
    }
    public ContentDto getUsers(String query, Long currentPage, Long limit, String sortData, String sortType) {
        try {
            ContentDto contentDto = new ContentDto();
            currentPage -= 1;
            Pageable pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
            var list = userRepository.searchUser(query, pageable);
            var newlist = list.getContent().stream().map(item ->{
                UserDto dto = new UserDto();
                BeanUtils.copyProperties(item, dto);
                return dto;
            }).collect(Collectors.toList());
            Integer totalProduct = Math.toIntExact(list.getTotalElements());
            Integer totalPageJob = Math.toIntExact(list.getTotalPages());
            if (currentPage.intValue() > totalPageJob) {
                currentPage = totalPageJob.longValue();
                pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
                list = userRepository.searchUser(query, pageable);
                newlist = list.getContent().stream().map(item ->{
                    UserDto dto = new UserDto();
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
