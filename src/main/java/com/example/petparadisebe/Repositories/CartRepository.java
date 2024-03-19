package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Cart;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserAndProduct(User user, Product product);
    List<Cart> findByUser(User user);
}
