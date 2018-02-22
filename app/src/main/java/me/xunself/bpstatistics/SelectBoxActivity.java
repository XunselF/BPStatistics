package me.xunself.bpstatistics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectBoxActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 标题栏按钮
     */
    private Button mCancelButton;
    private TextView mItemSelectNumber;
    private Button mNoSelectButton;
    private Button mSelectButton;
    private RelativeLayout mSelectBoxLayout;
    private RelativeLayout nodataLayout;
    private TextView noDataText;

    private String mSelectPrice;

    private List<Box> boxList;

    private RecyclerView mSelectBoxRecyclerView;

    //适配器
    private BoxAdapter mBoxAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_box);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        mSelectPrice = getIntent().getStringExtra("extra_price");
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mItemSelectNumber = (TextView) findViewById(R.id.item_number);
        mNoSelectButton = (Button) findViewById(R.id.no_select_all);
        mSelectButton = (Button) findViewById(R.id.select_all);
        mSelectBoxLayout = (RelativeLayout) findViewById(R.id.select_box_layout);
        nodataLayout = (RelativeLayout) findViewById(R.id.nodata_layout);
        noDataText = (TextView) findViewById(R.id.nodata_textview);
        boxList = DataSupport.findAll(Box.class);
        if (boxList.size() == 0){
            nodataLayout.setVisibility(View.VISIBLE);
            noDataText.setText("当前没有任何纸箱数据，请回到首页添加纸箱信息");
        }else{
            nodataLayout.setVisibility(View.GONE);
        }
        mSelectBoxRecyclerView = (RecyclerView) findViewById(R.id.select_box_recyclerview);
        mBoxAdapter = new BoxAdapter();
        mSelectBoxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSelectBoxRecyclerView.setAdapter(mBoxAdapter);

        mCancelButton.setOnClickListener(this);
        mSelectButton.setOnClickListener(this);
        mNoSelectButton.setOnClickListener(this);
        mSelectBoxLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.cancel_button:
                finish();//返回
                break;
            case R.id.select_all:
                //选中全部
                mBoxAdapter.selectAll();
                break;
            case R.id.no_select_all:
                //全部取消
                mBoxAdapter.noSelectAll();
                break;
            case R.id.select_box_layout:
                List<BoxPrice> boxPrices = mBoxAdapter.selectBox();
                if (boxPrices.size() != 0){
                    Intent intent = new Intent(SelectBoxActivity.this,AddCustomerActivity.class);
                    intent.putExtra("extra_boxprice",(Serializable) boxPrices);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder>{

        SparseBooleanArray mSelectStatu = new SparseBooleanArray();




        class ViewHolder extends RecyclerView.ViewHolder{
            TextView boxName;
            CheckBox boxSelect;
            LinearLayout boxItemLayout;
            public ViewHolder(View itemView) {
                super(itemView);
                boxName = (TextView) itemView.findViewById(R.id.box_name);
                boxSelect = (CheckBox) itemView.findViewById(R.id.box_selected);
                boxItemLayout = (LinearLayout) itemView.findViewById(R.id.box_item_layout);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SelectBoxActivity.this).inflate(R.layout.select_box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Box box = boxList.get(position);

            //数据加载
            holder.boxName.setText(box.getbName());
            //选中项加载
            holder.boxSelect.setChecked(isItemChecked(position));
            //修改UI
            if (isItemChecked(position)){
                holder.boxItemLayout.setBackgroundResource(R.drawable.selected_layout);
            }else{
                //数据是否包含该价格的判断
                if (getBoxPrice(box) != null){
                    holder.boxItemLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
                }else{
                    holder.boxItemLayout.setBackgroundColor(getResources().getColor(R.color.gray_color));
                }
            }

            //获取选中的数据
            mItemSelectNumber.setText(getItemCheckedNumber() + "");
            //点击监听
            holder.boxItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getBoxPrice(box) != null){
                        //更改选择状态
                        setItemChecked(position,!isItemChecked(position));
                    }else{
                        Toast.makeText(SelectBoxActivity.this,"无法选择！该纸箱没有该价格",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return boxList.size();
        }

        /**
         * 设置选择的状态
         * @param position
         * @param isSelected
         */
        public void setItemChecked(int position,boolean isSelected){
            mSelectStatu.put(position,isSelected);
            //进行UI更新
            mBoxAdapter.notifyDataSetChanged();
        }

        /**
         * 显示数据
         * @param position
         */
        public boolean isItemChecked(int position){
            return mSelectStatu.get(position);
        }

        /**
         * 获取选中的数据量
         * @return
         */
        public int getItemCheckedNumber(){
            int checkNumber = 0;
            for (int i = 0; i < boxList.size() ; i++){
                if (mSelectStatu.get(i)){
                    checkNumber ++;
                }
            }
            return checkNumber;
        }

        /**
         * 全部选中
         */
        public void selectAll(){
            for (int i = 0; i < boxList.size(); i++){
                if (getBoxPrice(boxList.get(i)) != null){
                    mSelectStatu.put(i,true);
                }
            }
            mBoxAdapter.notifyDataSetChanged();
        }

        /**
         * 全部取消
         */
        public void noSelectAll(){
            mSelectStatu.clear();
            mBoxAdapter.notifyDataSetChanged();
        }

        /**
         * 确定选项
         */
        public List<BoxPrice> selectBox(){
            List<BoxPrice> selectPrices = new ArrayList<>();
            for (int i = 0; i < boxList.size(); i++){
                if (isItemChecked(i)){
                    BoxPrice boxPrice = getBoxPrice(boxList.get(i));
                    if (boxPrice != null){
                        selectPrices.add(boxPrice);
                    }
                }
            }
            return selectPrices;
        }

        public BoxPrice getBoxPrice(Box box){
            List<BoxPrice> prices = DataSupport.where("bName = ? and pName = ? and ifLatest = ?",
                    box.getbName(),mSelectPrice,BoxPrice.LATEST_PRICE + "").order("id desc").limit(1).find(BoxPrice.class);
            return prices.size() != 0? prices.get(0):null;
        }
    }



}
