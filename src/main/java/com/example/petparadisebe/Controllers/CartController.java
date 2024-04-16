package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Entities.Cart;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Services.CartService;
import com.example.petparadisebe.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Object> addToCart(@RequestBody CartDto cartDto) {
        if (cartDto.getUser() == null || cartDto.getUser().getId() == null ||
                cartDto.getProduct() == null || cartDto.getProduct().getId() == null ||
                cartDto.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Dữ liệu đầu vào không hợp lệ");
        }
        User user = new User();
        user.setId(cartDto.getUser().getId());
        Product product = new Product();
        product.setId(cartDto.getProduct().getId());
        try {
            Cart cart = cartService.addToCart(user, product, cartDto.getQuantity());
            return ResponseEntity.ok(cart);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Số lượng sản phẩm muốn thêm vượt quá số lượng có sẵn trong kho");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi hệ thống xảy ra");
        }
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<Object> removeFromCart(@PathVariable("cartId") Long cartId) {
        try {
            cartService.removeFromCart(cartId);
            return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy giỏ hàng");
        }
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<Object> getCartItemsByUser(@PathVariable("userId") Long userId) {
        User user = new User();
        user.setId(userId);
        try {
            List<Cart> cartItems = cartService.getCartItemsByUser(user);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng hoặc giỏ hàng của người dùng");
        }
    }
    @PutMapping("/updateQuantity/{cartId}")
    public ResponseEntity<Object> updateCartItemQuantity(@PathVariable("cartId") Long cartId, @RequestParam("quantity") int quantity) {
        try {
            Cart updatedCart = cartService.updateCartItemQuantity(cartId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dữ liệu đầu vào không hợp lệ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi hệ thống xảy ra");
        }
    }

}
