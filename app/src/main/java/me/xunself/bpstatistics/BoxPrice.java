package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by XunselF on 2018/1/23.
 */

public class BoxPrice extends DataSupport implements Serializable{
    private int id;
    private String bName;
    private String pName;
    private double bPrice;
    private Date bDate;
    private int ifLatest;

    //之前的价格参数
    public static final int BEFORE_PRICE = 0;
    //最新的价格参数
    public static final int LATEST_PRICE = 1;

    public BoxPrice(String bName,String pName,double bPrize,Date bDate,int ifLatest){
        this.bDate = bDate;
        this.bName = bName;
        this.pName = pName;
        this.bPrice = bPrize;
        this.ifLatest = ifLatest;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public BoxPrice(){

    }

    public void setIfLatest(int ifLatest) {
        this.ifLatest = ifLatest;
    }

    public int getId() {
        return id;
    }

    public void setbName(String bName) {
        this.bName = bName;
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

    public static void updateIfLatest(String bName,String pName){
        if (DataSupport.where("bName = ? and pName = ? and ifLatest = ?",
                bName,pName,BoxPrice.LATEST_PRICE + "").find(BoxPrice.class).size() != 0){
            //对原有数据进行修改
            BoxPrice updatePrice = new BoxPrice();
            updatePrice.setToDefault("ifLatest");
            //对最新的价格进行修改
            updatePrice.updateAll("bName = ? and pName = ? and ifLatest = ?",bName,pName,BoxPrice.LATEST_PRICE + "");
        }
    }
}
