package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XunselF on 2018/2/10.
 */

public class CBox extends DataSupport implements Serializable {
    private int id;
    //对应的订单号
    private int workId;
    //纸箱名字
    private String bName;
    //纸箱价格
    private double bPrice;
    //纸箱数量
    private int bNumber;

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public void setbPrice(double bPrice) {
        this.bPrice = bPrice;
    }

    public void setbNumber(int bNumber) {
        this.bNumber = bNumber;
    }

    public int getId() {
        return id;
    }

    public String getbName() {
        return bName;
    }

    public double getbPrice() {
        return bPrice;
    }

    public int getbNumber() {
        return bNumber;
    }

    public int getWorkId() {
        return workId;
    }


}
