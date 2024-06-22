package com.example.DAJava.Service;

import com.example.DAJava.Model.CartItem;
import com.example.DAJava.Model.Product;
import com.example.DAJava.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found:" + productId));
        cartItems.add(new CartItem(product, quantity));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void updatetoQuanlity(Long productId, int quantity) {
        CartItem existItem = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        // Tìm sản phẩm trong giỏ hàng dựa trên productId
        if(existItem != null) {
            var totalPrice = existItem.getProduct().getPrice() * quantity;
            existItem.setQuantity(quantity);
            existItem.setTotalPrice(totalPrice);
        }
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart() {
        cartItems.clear();
    }
}