package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by XunselF on 2018/1/22.
 */

public class Box extends DataSupport implements Serializable{
    private int id;
    private String bLetter;
    private String bPinyin;
    private String bName;
    private String bContent;
    private Date bTime;

    public Box(String bName,String bContent,Date bTime){
        this.bName = bName;
        this.bContent = bContent;
        this.bTime = bTime;
    }
    public Box(){

    }

    public int getId() {
        return id;
    }

    public void setbPinyin(String bPinyin) {
        this.bPinyin = bPinyin;
    }

    public String getbPinyin() {
        return bPinyin;
    }

    public void setbLetter(String bLetter) {
        this.bLetter = bLetter;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public void setbContent(String bContent) {
        this.bContent = bContent;
    }

    public String getbLetter() {
        return bLetter;
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
