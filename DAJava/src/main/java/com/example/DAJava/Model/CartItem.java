package com.example.DAJava.Model;

public class CartItem {
    private Product product;
    private int quantity;
    private double totalPrice;
    // Constructors
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
    }
    // Getters and Setters
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
        updateTotalPrice();
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private void updateTotalPrice() {
        if (product != null) {
            this.totalPrice = product.getPrice() * quantity;
        }
    }
}