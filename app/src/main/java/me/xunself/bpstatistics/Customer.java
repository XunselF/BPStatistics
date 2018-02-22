package me.xunself.bpstatistics;

import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2018/2/5.
 */

public class Customer extends DataSupport implements Serializable,Comparable<Customer>{
    private int id;
    private String cName;
    private String cPinYin; //拼音
    private String cLetter; //首字母
    private String cContent;

    public Customer(){

    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public void setcLetter(String cLetter) {
        this.cLetter = cLetter;
    }

    public void setcPinYin(String cPinYin) {
        this.cPinYin = cPinYin;
    }

    public String getcLetter() {
        return cLetter;
    }

    public String getcPinYin() {
        return cPinYin;
    }

    public int getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public String getcContent() {
        return cContent;
    }

    public void deleteCustomer(){
        List<Work> works = DataSupport.where("cName = ?",cName).find(Work.class);
        if (works.size() != 0){
            //删除所有订单
            for (int i = 0; i < works.size(); i++){
                Work work = works.get(i);
                work.deleteCBox(work.getId());
            }
        }
        //删除该客户
        DataSupport.deleteAll(Customer.class,"cName = ?",cName);
    }

    /**
     * 查询是否包含工单
     * @return
     */
    public boolean isHaveWork(){
        List<Work> works = DataSupport.where("cName = ?",cName).find(Work.class);
        if (works.size() != 0){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Customer customer) {
        if (this.getcLetter().equals("@") || customer.getcLetter().equals("#")){
            return -1;
        }else if (this.getcLetter().equals("#") || customer.getcLetter().equals("@")){
            return 1;
        }else{
            return this.getcPinYin().compareTo(customer.getcPinYin());
        }
    }
}
