package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XunselF on 2018/2/5.
 */

public class Order extends DataSupport implements Serializable {
    private int id;
    private Date oTime;
    private double oPrice;
    private Customer customer;
    private List<OrderBox> orderBoxList = new ArrayList<>();

    public Order(){

    }

    public void setoPrice(double oPrice) {
        this.oPrice = oPrice;
    }

    public void setoTime(Date oTime) {
        this.oTime = oTime;
    }

    public int getId() {
        return id;
    }

    public Date getoTime() {
        return oTime;
    }

    public double getoPrice() {
        return oPrice;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOrderBoxList(List<OrderBox> orderBoxList) {
        this.orderBoxList = orderBoxList;
    }

    public List<OrderBox> getOrderBoxList() {
        return orderBoxList;
    }

    public Customer getCustomer() {
        return customer;
    }
}
