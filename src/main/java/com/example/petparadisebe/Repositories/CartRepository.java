package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Cart;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserAndProduct(User user, Product product);
    List<Cart> findByUser(User user);
    //COALESCE được sử dụng để đảm bảo rằng nếu không có giỏ hàng nào chứa sản phẩm này, kết quả trả về sẽ là 0
    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM Cart c WHERE c.product.id = :productId")
    int getTotalQuantityOfProductInCarts(@Param("productId") Long productId);
}
