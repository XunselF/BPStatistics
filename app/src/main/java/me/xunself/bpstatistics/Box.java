package me.xunself.bpstatistics;

import java.util.Date;

/**
 * Created by XunselF on 2018/1/22.
 */

public class Box {
    private String bName;
    private String bContent;
    private Date bTime;

    public Box(String bName,String bContent,Date bTime){
        this.bName = bName;
        this.bContent = bContent;
        this.bTime = bTime;
    }

    public Date getbTime() {
        return bTime;
    }

    public String getbContent() {
        return bContent;
    }

    public String getbName() {
        return bName;
    }
}
