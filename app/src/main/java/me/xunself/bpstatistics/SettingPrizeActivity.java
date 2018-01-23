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
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SettingPrizeActivity extends AppCompatActivity {

    private List<Price> priceList;
    private Toolbar toolbar;

    private RecyclerView prizeRecyclerView;
    private PrizeAdapter prizeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_prize);
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
                            Toast.makeText(SettingPrizeActivity.this,"输入不能为空！",Toast.LENGTH_SHORT).show();
                        }else{
                            for (int j = 0; j < priceList.size(); j++){
                                if (input.equals(priceList.get(j).getPriceName())){
                                    Toast.makeText(SettingPrizeActivity.this,"您已添加该价格类型！",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            Price price = new Price(input);
                            price.save();
                            Toast.makeText(SettingPrizeActivity.this,"保存成功！",Toast.LENGTH_SHORT).show();
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
        prizeRecyclerView = (RecyclerView) findViewById(R.id.prize_recyclerview);
        prizeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        prizeAdapter = new PrizeAdapter();
        prizeRecyclerView.setAdapter(prizeAdapter);
    }
    class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView prizeNameText;
            public ViewHolder(View itemView) {
                super(itemView);
                prizeNameText = (TextView) itemView.findViewById(R.id.prize_name);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SettingPrizeActivity.this).inflate(R.layout.prize_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Price prize = priceList.get(position);
            holder.prizeNameText.setText(prize.getPriceName());
        }

        @Override
        public int getItemCount() {
            return priceList.size();
        }
    }
}
