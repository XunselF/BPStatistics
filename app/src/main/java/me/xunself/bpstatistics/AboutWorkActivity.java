package me.xunself.bpstatistics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AboutWorkActivity extends AppCompatActivity {

    private Work mWork;

    private Toolbar toolbar;

    private List<CBox> cBoxes = new ArrayList<>();

    private RecyclerView cBoxRecyclerView;

    private TextView workTimeText;
    private TextView sumPriceText;

    private CBoxAdapter cBoxAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_work);
        initView();
    }

    /**
     * 获取数据
     */
    private void getCBoxList(){
        if (mWork != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH：mm");
            double sumPrice = mWork.getwPrice();
            BigDecimal bigDecimal = new BigDecimal(sumPrice);
            sumPrice = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            sumPriceText.setText(sumPrice + "");
            workTimeText.setText(sdf.format(mWork.getwTime()));
            cBoxes = DataSupport.where("workId = ?",mWork.getId() + "").find(CBox.class);
        }
        cBoxAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCBoxList();
    }

    /**
     * 初始化
     */
    private void initView(){
        mWork = (Work) getIntent().getSerializableExtra("extra_work");

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        workTimeText = (TextView) findViewById(R.id.work_time);
        sumPriceText = (TextView) findViewById(R.id.work_sumprice);

        cBoxRecyclerView = (RecyclerView) findViewById(R.id.work_box_recyclerview);
        cBoxAdapter = new CBoxAdapter();
        cBoxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cBoxRecyclerView.setAdapter(cBoxAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class CBoxAdapter extends RecyclerView.Adapter<CBoxAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView cBoxName;
            TextView cBoxNumber;
            TextView cBoxPrice;
            TextView cBoxSumPrice;
            public ViewHolder(View itemView) {
                super(itemView);
                cBoxName = (TextView) itemView.findViewById(R.id.cbox_name);
                cBoxNumber = (TextView) itemView.findViewById(R.id.cbox_number);
                cBoxPrice = (TextView) itemView.findViewById(R.id.cbox_price);
                cBoxSumPrice = (TextView) itemView.findViewById(R.id.cbox_sum_price);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AboutWorkActivity.this).inflate(R.layout.work_box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CBox cBox = cBoxes.get(position);
            holder.cBoxName.setText(cBox.getbName());
            holder.cBoxPrice.setText(cBox.getbPrice() + "");
            holder.cBoxNumber.setText(cBox.getbNumber() + "");
            try{
                double sumPrice = cBox.getbNumber() * cBox.getbPrice();
                BigDecimal bigDecimal = new BigDecimal(sumPrice);
                sumPrice = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                holder.cBoxSumPrice.setText(sumPrice + "");
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return cBoxes.size();
        }
    }
}
