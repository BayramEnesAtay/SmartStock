package com.inventory.model;

public class Order {

    private int orderId;
    private String productName;
    private int orderQuantity;
    private String status;

    public Order(int orderId, String productName,
                 int orderQuantity, String status) {
        this.orderId = orderId;
        this.productName = productName;
        this.orderQuantity = orderQuantity;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public String getProductName() { return productName; }
    public int getOrderQuantity() { return orderQuantity; }
    public String getStatus() { return status; }
}
