package me.xunself.bpstatistics;

import android.util.SparseArray;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XunselF on 2018/2/6.
 */

public class Work extends DataSupport implements Serializable{
    private int id;
    private Date wTime;
    private double wPrice;
    private String cName;
    private Customer customer;
    private List<BoxPrice> orderBoxList = new ArrayList<>();

    public Work(){

    }

    public void setwPrice(double wPrice) {
        this.wPrice = wPrice;
    }

    public void setwTime(Date wTime) {
        this.wTime = wTime;
    }

    public void deleteCBox(int workId){
        DataSupport.delete(Work.class,workId);
        DataSupport.deleteAll(CBox.class,"workId = ?",workId + "");
    }

    public void setOrderBoxList(int workId, SparseArray<String> boxNumbers, List<BoxPrice> orderBoxList) {
        for (int i = 0; i < orderBoxList.size(); i++){
            try{
                CBox cBox = new CBox();
                //对应的订单号
                cBox.setWorkId(workId);
                //纸箱名
                cBox.setbName(orderBoxList.get(i).getbName());
                //纸箱单价
                cBox.setbPrice(orderBoxList.get(i).getbPrice());
                //保存数量
                cBox.setbNumber(Integer.valueOf(boxNumbers.get(i)));

                cBox.save();    //保存
            }catch(NumberFormatException e){
                e.printStackTrace();
            }

        }
    }

    public List<BoxPrice> getOrderBoxList() {
        return orderBoxList;
    }

    public int getId() {
        return id;
    }

    public Date getwTime() {
        return wTime;
    }

    public double getwPrice() {
        return wPrice;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcName() {
        return cName;
    }
}
