package org.yearup.models;

import java.math.BigDecimal;

public class Order {
    private int orderId;
    private int userId;
    private String address;
    private String city;
    private String state;
    private String zip;
    private BigDecimal shipAmt;

    public Order() {
    }

    public Order(int orderId, int userId , String address, String city, String state, String zip, BigDecimal shipAmt) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shipAmt = shipAmt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public BigDecimal getShipAmt() {
        return shipAmt;
    }

    public void setShipAmt(BigDecimal shipAmt) {
        this.shipAmt = shipAmt;
    }
}
