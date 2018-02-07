package me.xunself.bpstatistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XunselF on 2018/2/6.
 */

public class Work {
    private int id;
    private Date wTime;
    private double wPrice;
    private Customer customer;
    private List<Box> orderBoxList = new ArrayList<>();

    public Work(){

    }

    public void setwPrice(double wPrice) {
        this.wPrice = wPrice;
    }

    public void setwTime(Date wTime) {
        this.wTime = wTime;
    }

    public void setOrderBoxList(List<Box> orderBoxList) {
        this.orderBoxList = orderBoxList;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Box> getOrderBoxList() {
        return orderBoxList;
    }

    public int getId() {
        return id;
    }

    public Date getwTime() {
        return wTime;
    }

    public double getwPrice() {
        return wPrice;
    }
}
