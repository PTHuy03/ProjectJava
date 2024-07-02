package com.example.DAJava.Service;

import com.example.DAJava.Model.CartItem;
import com.example.DAJava.Model.Product;
import com.example.DAJava.Model.User;
import com.example.DAJava.Repository.CartItemRepository;
import com.example.DAJava.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public void addToCart(Long productId, int quantity) {
        User currentUser = userService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found:" + productId));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(currentUser, product)
                .orElse(new CartItem(product, quantity, currentUser));
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());

        cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItems() {
        User currentUser = userService.getCurrentUser();
        return cartItemRepository.findByUser(currentUser);
    }

    public void updateQuantity(Long productId, int quantity) {
        User currentUser = userService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found:" + productId));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(currentUser, product)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getProduct().getPrice() * quantity);

        cartItemRepository.save(cartItem);
    }

    public void removeFromCart(Long productId) {
        User currentUser = userService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found:" + productId));

        cartItemRepository.deleteByUserAndProduct(currentUser, product);
    }

    public void clearCart() {
        User currentUser = userService.getCurrentUser();
        cartItemRepository.deleteByUser(currentUser);
    }
}

