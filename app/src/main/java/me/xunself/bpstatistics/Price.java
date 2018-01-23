package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

/**
 * Created by XunselF on 2018/1/23.
 */

public class Price extends DataSupport{
    private String priceName;
    public Price(String priceName){
        this.priceName = priceName;
    }

    public String getPriceName() {
        return priceName;
    }
}
