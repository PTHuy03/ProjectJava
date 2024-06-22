package com.example.DAJava.Service;

import com.example.DAJava.Model.CartItem;
import com.example.DAJava.Model.Order;
import com.example.DAJava.Model.OrderDetail;
import com.example.DAJava.Repository.OrderDetailRepository;
import com.example.DAJava.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService
    @Transactional
    public Order createOrder(String customerName,String address, String phone, String note, String paymentMethod, List<CartItem> cartItems) {
        double totalprice = 0.0;
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhone(phone);
        order.setNote(note);
        order.setPaymentmethod(paymentMethod);
        for(CartItem cartItem : cartItems) {
            totalprice = totalprice + cartItem.getTotalPrice();
        }
        order.setTotalPrice(totalprice);
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        // Optionally clear the cart after order placement
        cartService.clearCart();
        return order;
    }
}