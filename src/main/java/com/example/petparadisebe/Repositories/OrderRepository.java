package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Order;
import com.example.petparadisebe.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select j from Order j where j.fullName like %:key%")
    Page<Order> searchOrder(@Param("key") String textSearch, Pageable pageable);

}
