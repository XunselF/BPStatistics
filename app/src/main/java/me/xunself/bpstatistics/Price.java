package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

/**
 * Created by XunselF on 2018/1/23.
 */

public class Price extends DataSupport{
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
