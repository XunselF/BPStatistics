package me.xunself.bpstatistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BoxInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView textBContent;

    private TextView textBTime;

    private RecyclerView priceRecyclerView;

    private PriceAdapter priceAdapter;

    private int mValue = 3;





    private Box mBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_info);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aboutbox_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_update:
                showAlertDialog();
                break;
            case R.id.action_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("是否删除该纸箱信息？");
                dialog.setTitle("删除");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataSupport.delete(Box.class,mBox.getId());
                        DataSupport.deleteAll(BoxPrice.class,"bName = ?",mBox.getbName());
                        Toast.makeText(BoxInfoActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                dialog.setNegativeButton("取消",null);
                dialog.show();
                break;
        }
        return true;
    }
    private void showAlertDialog(){
        View view = (LinearLayout) getLayoutInflater().inflate(R.layout.updatebox_dialog,null);
        final EditText inputBName = (EditText) view.findViewById(R.id.input_boxName);
        final EditText inputBContent = (EditText) view.findViewById(R.id.input_boxContent);

        inputBName.setText(mBox.getbName());
        inputBContent.setText(mBox.getbContent());
        inputBName.setSelection(mBox.getbName().length());
        inputBContent.setSelection(mBox.getbContent().length());

        new AlertDialog.Builder(this).setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        List<Box> boxList = DataSupport.findAll(Box.class);
                        String bName = inputBName.getText().toString().trim();
                        String bContent = inputBContent.getText().toString().trim();
                        if (bName.equals("")){
                            ManagementFragment.noCloseDialog(dialogInterface);
                            Toast.makeText(BoxInfoActivity.this,"请填写纸箱型号！",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (Box box : boxList){
                            if (box.getbName().equals(bName) && !box.getbName().equals(mBox.getbName())){
                                ManagementFragment.noCloseDialog(dialogInterface);
                                Toast.makeText(BoxInfoActivity.this,"您填写的纸箱已存在！",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Box updateBox = new Box();
                        updateBox.setbName(bName);
                        updateBox.setbContent(bContent);
                        updateBox.updateAll("bName = ?",mBox.getbName());

                        BoxPrice boxPrice = new BoxPrice();
                        boxPrice.setbName(bName);

                        boxPrice.updateAll("bName = ?",mBox.getbName());
                        Toast.makeText(BoxInfoActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        ManagementFragment.closeDialog(dialogInterface);

                        mBox.setbName(bName);
                        mBox.setbContent(bContent);

                        toolbar.setTitle(bName);
                        if (bContent.equals("")){
                            textBContent.setText("为空");
                        }else{
                            textBContent.setText(bContent);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ManagementFragment.noCloseDialog(dialogInterface);
            }
        }).show();
    }

    /**
     * 初始化
     */
    private void initView(){
        mBox = (Box)getIntent().getSerializableExtra("extra_box");
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        textBContent = (TextView) findViewById(R.id.box_content);
        textBTime = (TextView) findViewById(R.id.box_createtime);

        priceRecyclerView = (RecyclerView) findViewById(R.id.price_recyclerview);
        priceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        priceAdapter = new PriceAdapter();
        priceRecyclerView.setAdapter(priceAdapter);


        setSupportActionBar(toolbar);
        toolbar.setTitle(mBox.getbName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        if (mBox.getbContent().equals("")){
            textBContent.setText("为空");
        }else{
            textBContent.setText(mBox.getbContent());
        }

        textBTime.setText(sdf.format(mBox.getbTime()));
    }

    class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder>{

        List<Price> prices;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView priceName;
            TextView nodataLayout;
            ImageView createPrice;
            ImageView priceList;
            RecyclerView pricesRecyclerView;
            public ViewHolder(View itemView) {
                super(itemView);
                priceName = (TextView) itemView.findViewById(R.id.box_price_name);
                nodataLayout = (TextView) itemView.findViewById(R.id.nodata_layout);
                createPrice = (ImageView) itemView.findViewById(R.id.create_price);
                priceList = (ImageView) itemView.findViewById(R.id.price_list);
                pricesRecyclerView = (RecyclerView) itemView.findViewById(R.id.prices_recyclerview);
            }
        }

        public PriceAdapter(){
            prices = DataSupport.findAll(Price.class);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BoxInfoActivity.this).inflate(R.layout.box_prices_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Price price = prices.get(position);
            holder.priceName.setText(price.getPriceName());

            List<BoxPrice> boxPriceList = DataSupport.where("bName = ? and pName = ?",mBox.getbName(),price.getPriceName()).order("id desc").limit(mValue).find(BoxPrice.class);

            if (boxPriceList.size() != 0){
                holder.nodataLayout.setVisibility(View.GONE);
            }else{
                holder.nodataLayout.setVisibility(View.VISIBLE);
            }

            holder.pricesRecyclerView.setLayoutManager(new LinearLayoutManager(BoxInfoActivity.this));
            ItemPriceAdapter itemPriceAdapter = new ItemPriceAdapter(boxPriceList);
            holder.pricesRecyclerView.setAdapter(itemPriceAdapter);
            /**
             * 添加价格点击事件
             */
            holder.createPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            /**
             * 价格表的点击事件
             */
            holder.priceList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return prices.size();
        }


    }

    class ItemPriceAdapter extends RecyclerView.Adapter<ItemPriceAdapter.ViewHolder>{

        List<BoxPrice> boxPriceList;

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView ifLaterst;
            TextView priceTime;
            TextView itemPrice;
            public ViewHolder(View itemView) {
                super(itemView);
                ifLaterst = (ImageView) itemView.findViewById(R.id.ifLatest);
                priceTime = (TextView) itemView.findViewById(R.id.price_time);
                itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            }
        }
        public ItemPriceAdapter(List<BoxPrice> boxPriceList){
            this.boxPriceList = boxPriceList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BoxInfoActivity.this).inflate(R.layout.boxinfo_boxprice_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BoxPrice boxPrice = boxPriceList.get(position);
            if (boxPrice.getifLatest() == 1){
                holder.ifLaterst.setImageResource(R.drawable.ic_star_border_black_36dp);
            }else{
                holder.ifLaterst.setImageResource(0);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            holder.itemPrice.setText(boxPrice.getbPrice() + "");
            holder.priceTime.setText(sdf.format(boxPrice.getbDate()));

        }

        @Override
        public int getItemCount() {
            return boxPriceList.size();
        }
    }
}
