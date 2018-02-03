package me.xunself.bpstatistics;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * Created by XunselF on 2018/2/3.
 */

public class OperateDialog {
    /**
     * 关闭dialog
     */
    public static void closeDialog(DialogInterface dialog){
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 不关闭dialog
     */
    public static void noCloseDialog(DialogInterface dialog){
        //不关闭对话框
        try{
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 重新使用该价格的操作
     * @param boxPrice
     */
    public static void reuseBoxPrice(final BoxPrice boxPrice, final Context context, final RecyclerView.Adapter boxPriceAdapter){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("重用");
        dialog.setMessage("是否重新使用该价格？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //获取数据
                String bName = boxPrice.getbName();
                String pName = boxPrice.getpName();
                //判断是否为当前最新价格
                if (boxPrice.getifLatest() != BoxPrice.LATEST_PRICE){
                    //修改之前的最新价格
                    BoxPrice.updateIfLatest(bName,pName);
                    //添加新的价格
                    BoxPrice newPrice = new BoxPrice(boxPrice.getbName(),boxPrice.getpName(),boxPrice.getbPrice(),new Date(),BoxPrice.LATEST_PRICE);
                    if (newPrice.save()){
                        //数据刷新
                        boxPriceAdapter.notifyDataSetChanged();
                        Toast.makeText(context,"保存成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "异常错误！请重新进行操作", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //如果是，返回 提示
                    Toast.makeText(context,"设置失败！该价格已为当前最新价格",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
    public static void deleteBoxPrice(final BoxPrice boxPrice, final Context context, final RecyclerView.Adapter boxPriceAdapter){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("删除");
        dialog.setMessage("是否删除该价格？");
        dialog.setCancelable(true);dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除该价格
                DataSupport.delete(BoxPrice.class,boxPrice.getId());
                //如果需要删除的价格是最新的价格 需要修改上一个价格
                if (boxPrice.getifLatest() == BoxPrice.LATEST_PRICE){
                    List<BoxPrice> boxPriceList = DataSupport.where("bName = ? and pName = ?",
                            boxPrice.getbName(),boxPrice.getpName()).order("id desc").find(BoxPrice.class);
                    //判断是否为空
                    if (boxPriceList.size() != 0){
                        //取最新的一个价格 修改
                        BoxPrice latestPrice = boxPriceList.get(0);
                        BoxPrice updatePrice = new BoxPrice();
                        updatePrice.setIfLatest(BoxPrice.LATEST_PRICE);
                        updatePrice.update(latestPrice.getId());
                    }
                }
                boxPriceAdapter.notifyDataSetChanged();
                //提示
                Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("取消",null);
        dialog.show();
    }

}
