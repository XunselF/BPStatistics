package me.xunself.bpstatistics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InquirePriceActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String mBName;

    private String mPName;

    List<BoxPrice> boxPriceList;

    private RecyclerView boxPriceRecyclerView;

    private ItemPriceAdapter itemPriceAdapter;

    //没有数据时所显示的布局
    private TextView nodataLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_price);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        //获取传过来的值
        mBName = getIntent().getStringExtra("extra_bName");
        mPName = getIntent().getStringExtra("extra_pName");
        //控件的初始化

        boxPriceRecyclerView = (RecyclerView) findViewById(R.id.box_price_recyclerview);
        nodataLayout = (TextView) findViewById(R.id.nodata_layout);
        itemPriceAdapter = new ItemPriceAdapter();
        toolbar = (Toolbar) findViewById(R.id.inquire_toolbar);
        //加载标题栏
        setSupportActionBar(toolbar);



        //为标题栏加载纸箱姓名与价格名的数据
        toolbar.setTitle(mBName);
        toolbar.setSubtitle(mPName + "类详情");

        //recyclerview布局设置与加载适配器
        boxPriceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        boxPriceRecyclerView.setAdapter(itemPriceAdapter);
        //获取数据
        getPriceList();

        //返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getPriceList(){
        boxPriceList = DataSupport.where("bName = ? and pName = ?",mBName,mPName).order("id desc").find(BoxPrice.class);
        //查询数据是否为空
        if (boxPriceList.size() == 0){
            //如果为空显示 没有数据时的提醒板块
            nodataLayout.setVisibility(View.VISIBLE);
        }else{
            nodataLayout.setVisibility(View.GONE);
        }
    }

    class ItemPriceAdapter extends RecyclerView.Adapter<ItemPriceAdapter.ViewHolder>{


        class ViewHolder extends RecyclerView.ViewHolder{
            CardView boxPriceItemLayout;
            ImageView ifLaterst;
            TextView priceTime;
            TextView itemPrice;
            public ViewHolder(View itemView) {
                super(itemView);
                boxPriceItemLayout = (CardView) itemView.findViewById(R.id.boxprice_item_layout);
                ifLaterst = (ImageView) itemView.findViewById(R.id.ifLatest);
                priceTime = (TextView) itemView.findViewById(R.id.price_time);
                itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            }
        }

        public ItemPriceAdapter(){
            getPriceList();
        }


        @Override
        public ItemPriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(InquirePriceActivity.this).inflate(R.layout.boxinfo_boxprice_item,parent,false);
            ItemPriceAdapter.ViewHolder holder = new ItemPriceAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ItemPriceAdapter.ViewHolder holder, int position) {
            final BoxPrice boxPrice = boxPriceList.get(position);
            if (boxPrice.getifLatest() == 1){
                holder.ifLaterst.setImageResource(R.drawable.ic_star_border_black_36dp);
            }else{
                holder.ifLaterst.setImageResource(0);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            holder.itemPrice.setText(boxPrice.getbPrice() + "");
            holder.priceTime.setText(sdf.format(boxPrice.getbDate()));

            //长按监听 显示菜单进行选择
            holder.boxPriceItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(InquirePriceActivity.this,holder.boxPriceItemLayout);
                    popupMenu.getMenuInflater().inflate(R.menu.boxprice_item_popupmenu,popupMenu.getMenu());
                    //菜单选项监听
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.reuse_boxprice:
                                    //重用该价格
                                    OperateDialog.reuseBoxPrice(boxPrice,InquirePriceActivity.this,itemPriceAdapter);
                                    break;
                                case R.id.delete_boxprice:
                                    //删除该价格
                                    OperateDialog.deleteBoxPrice(boxPrice,InquirePriceActivity.this,itemPriceAdapter);
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            getPriceList();
            return boxPriceList.size();
        }

    }
}
