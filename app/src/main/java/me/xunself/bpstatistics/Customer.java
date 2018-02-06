package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2018/2/5.
 */

public class Customer extends DataSupport implements Serializable{
    private int id;
    private String cName;
    private String cContent;
    private List<Order> orderList = new ArrayList<>();

    public Customer(){

    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public int getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public String getcContent() {
        return cContent;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
