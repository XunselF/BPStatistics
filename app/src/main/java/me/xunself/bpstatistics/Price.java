package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by XunselF on 2018/1/23.
 */

public class Price extends DataSupport implements Serializable{
    private int id;
    private String priceName;

    public int getId() {
        return id;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }
    public Price(){

    }

    public Price(String priceName){
        this.priceName = priceName;
    }

    public String getPriceName() {
        return priceName;
    }
}
