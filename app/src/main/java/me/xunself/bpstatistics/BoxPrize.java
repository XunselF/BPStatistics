package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by XunselF on 2018/1/23.
 */

public class BoxPrize extends DataSupport{
    private String bName;
    private String pName;
    private double bPrize;
    private Date bDate;

    public BoxPrize(String bName,String pName,double bPrize,Date bDate){
        this.bDate = bDate;
        this.bName = bName;
        this.pName = pName;
        this.bPrize = bPrize;
    }

    public String getbName() {
        return bName;
    }

    public Date getbDate() {
        return bDate;
    }

    public double getbPrize() {
        return bPrize;
    }

    public String getpName() {
        return pName;
    }
}
