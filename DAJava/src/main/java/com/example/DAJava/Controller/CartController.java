package com.example.DAJava.Controller;

import com.example.DAJava.Model.CartItem;
import com.example.DAJava.Repository.ProductRepository;
import com.example.DAJava.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        return "/cart/cart";
    }
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
        boolean productExists = false;
        for (CartItem item : cartService.getCartItems()) {
            if (item.getProduct().getId().equals(productId)) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng
                item.setQuantity(item.getQuantity() + quantity);
                productExists = true;
                break;
            }
        }

        // Nếu sản phẩm chưa tồn tại, thêm một mục mới vào giỏ hàng
        if (!productExists) {
            cartService.addToCart(productId, quantity);
        }

        return "redirect:/cart";
    }

    @GetMapping("/update")
    public String updateQuantity(Long productId, int quantity) {
        // Gọi phương thức updateQuantity của CartService để cập nhật số lượng
        cartService.updatetoQuanlity(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}