package me.xunself.bpstatistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SettingPriceActivity extends AppCompatActivity {
    private List<Price> priceList;
    private Toolbar toolbar;

    private RecyclerView priceRecyclerView;
    private SettingPriceActivity.PriceAdapter priceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_price);
        init();
    }

    /**
     * 获取数据
     */
    private void getPrizes(){
        priceList = DataSupport.findAll(Price.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                showDialog();
                break;
        }
        return true;
    }

    /**
     * 弹窗
     */
    private void showDialog(){
        View view = (LinearLayout) getLayoutInflater().inflate(R.layout.alertdialog_price,null);
        final EditText et = (EditText) view.findViewById(R.id.input_price);

        new AlertDialog.Builder(this).setTitle("价格类型")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = et.getText().toString();
                        if (input.equals("")){
                            Toast.makeText(SettingPriceActivity.this,"输入不能为空！",Toast.LENGTH_SHORT).show();
                        }else{
                            for (int j = 0; j < priceList.size(); j++){
                                if (input.equals(priceList.get(j).getPriceName())){
                                    Toast.makeText(SettingPriceActivity.this,"您已添加该价格类型！",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            Price price = new Price(input);
                            priceList.add(price);
                            price.save();
                            Toast.makeText(SettingPriceActivity.this,"保存成功！",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    /**
     * 初始化
     */
    private void init(){
        getPrizes();
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        priceRecyclerView = (RecyclerView) findViewById(R.id.price_recyclerview);
        priceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        priceAdapter = new SettingPriceActivity.PriceAdapter();
        priceRecyclerView.setAdapter(priceAdapter);
    }
    class PriceAdapter extends RecyclerView.Adapter<SettingPriceActivity.PriceAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout priceItemLayout;
            TextView priceNameText;
            public ViewHolder(View itemView) {
                super(itemView);
                priceItemLayout = (LinearLayout) itemView.findViewById(R.id.price_item_layout);
                priceNameText = (TextView) itemView.findViewById(R.id.price_name);
            }
        }

        @Override
        public SettingPriceActivity.PriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SettingPriceActivity.this).inflate(R.layout.prize_item,parent,false);
            SettingPriceActivity.PriceAdapter.ViewHolder holder = new SettingPriceActivity.PriceAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SettingPriceActivity.PriceAdapter.ViewHolder holder, int position) {
            Price price = priceList.get(position);
            holder.priceNameText.setText(price.getPriceName());
            holder.priceItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(SettingPriceActivity.this,holder.priceItemLayout);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return priceList.size();
        }
    }
}
