package me.xunself.bpstatistics;

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
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SettingPrizeActivity extends AppCompatActivity {

    private List<Prize> prizeList;
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
        prizeList = DataSupport.findAll(Prize.class);
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
                break;
        }
        return true;
    }

    /**
     * 弹窗
     */
    private void showDialog(){
        
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
            Prize prize = prizeList.get(position);
            holder.prizeNameText.setText(prize.getPrizeName());
        }

        @Override
        public int getItemCount() {
            return prizeList.size();
        }
    }
}
