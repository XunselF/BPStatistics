package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by XunselF on 2018/2/5.
 */

public class OrderBox extends DataSupport implements Serializable {

    private int id;
    private String bName;
    private String bPrice;
    private String bNumber;
    private Order order;

    public void setbName(String bName) {
        this.bName = bName;
    }

    public void setbNumber(String bNumber) {
        this.bNumber = bNumber;
    }

    public void setbPrice(String bPrice) {
        this.bPrice = bPrice;
    }

    public int getId() {
        return id;
    }

    public String getbName() {
        return bName;
    }

    public String getbNumber() {
        return bNumber;
    }

    public String getbPrice() {
        return bPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
