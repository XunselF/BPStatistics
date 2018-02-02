package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by XunselF on 2018/1/23.
 */

public class BoxPrice extends DataSupport{
    private String bName;
    private String pName;
    private double bPrice;
    private Date bDate;
    private int ifLatest;

    public BoxPrice(String bName,String pName,double bPrize,Date bDate,int ifLatest){
        this.bDate = bDate;
        this.bName = bName;
        this.pName = pName;
        this.bPrice = bPrize;
        this.ifLatest = ifLatest;
    }

    public String getbName() {
        return bName;
    }

    public Date getbDate() {
        return bDate;
    }

    public double getbPrice() {
        return bPrice;
    }

    public String getpName() {
        return pName;
    }

    public int getifLatest(){
        return ifLatest;
    }
}
