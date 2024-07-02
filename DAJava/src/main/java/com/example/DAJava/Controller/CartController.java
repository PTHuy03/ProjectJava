package com.example.DAJava.Controller;

import com.example.DAJava.Model.CartItem;
import com.example.DAJava.Repository.ProductRepository;
import com.example.DAJava.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cartItems")
public class CartController {

    @Autowired
    private CartService cartService;

    @ModelAttribute("cartItems")
    public List<CartItem> cartItems() {
        return cartService.getCartItems();
    }

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        return "/cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        boolean productExists = false;
        for (CartItem item : cartService.getCartItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            cartService.addToCart(productId, quantity);
        }

        return "redirect:/cart";
    }

    @GetMapping("/update")
    public String updateQuantity(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
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
