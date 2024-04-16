package com.example.petparadisebe.Services;


import com.example.petparadisebe.Entities.Cart;
import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Repositories.CartRepository;
import com.example.petparadisebe.Repositories.ProductRepository;
import com.example.petparadisebe.Repositories.UserRepository;
import com.example.petparadisebe.exception.CartNotFoundException;
import com.example.petparadisebe.exception.CategoryException;
import com.example.petparadisebe.exception.UsernameNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Cart addToCart(User user, Product product, int quantity) {
        if (user == null || product == null || quantity <= 0) {
            throw new IllegalArgumentException("Dữ liệu đầu vào không hợp lệ");
        }
        Cart existingCart = cartRepository.findByUserAndProduct(user, product);
        double price = productRepository.getPriceById(product.getId());
        int availableQuantity = productRepository.getQuantityById(product.getId());
        if (availableQuantity == 0) {
            throw new IllegalArgumentException("Sản phẩm không còn trong kho");
        }
        int totalRequestedQuantity = (existingCart != null ? cartRepository.getTotalQuantityOfProductInCarts(product.getId()) : 0) + quantity;
        if (totalRequestedQuantity > availableQuantity) {
            throw new IllegalArgumentException("Số lượng sản phẩm muốn thêm vượt quá số lượng có sẵn trong kho");
        }
        if (existingCart != null) {
            int newQuantity = existingCart.getQuantity() + quantity;
            double newTotalPrice = price * newQuantity;
            existingCart.setQuantity(newQuantity);
            existingCart.setTotalPrice(newTotalPrice);
            return cartRepository.save(existingCart);
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setTotalPrice(price * quantity);
        cart.setOrderStatus("Chưa thanh toán");
        return cartRepository.save(cart);
    }


    public void removeFromCart(Long cartId) {
        Optional<Cart> cartExist = cartRepository.findById(cartId);
        if (cartExist.isPresent()) {
            cartRepository.deleteById(cartId);
        } else {
            throw new CartNotFoundException("Không tìm thấy giỏ hàng");
        }
    }

    public List<Cart> getCartItemsByUser(User user) {
        Optional<User> userExist = userRepository.findById(user.getId());
        if (userExist.isPresent()) {
            return cartRepository.findByUser(user);
        } else {
            throw new UsernameNotFoundException("Không tìm thấy người dùng hoặc giỏ hàng của người dùng");
        }
    }

    public Cart updateCartItemQuantity(Long cartId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng không hợp lệ");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Không tìm thấy giỏ hàng"));

        Product product = cart.getProduct();
        int availableQuantity = product.getQuantityInStock();

        if (availableQuantity < quantity) {
            throw new IllegalArgumentException("Số lượng sản phẩm muốn thêm vượt quá số lượng có sẵn trong kho");
        }

        double price = product.getPrice();
        cart.setQuantity(quantity);
        cart.setTotalPrice(price * quantity);

        return cartRepository.save(cart);
    }

}
