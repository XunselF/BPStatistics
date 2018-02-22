package me.xunself.bpstatistics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AboutCustomerActivity extends AppCompatActivity {

    private Customer customer;

    private TextView customerNameText;
    private TextView customerContentText;

    private Toolbar toolbar;

    private RecyclerView workRecyclerView;

    private WorkApdater workApdater;

    private List<Work> workList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_customer);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWorkList();
    }

    /**
     * 初始化
     */
    private void initView(){
        customer = (Customer)getIntent().getSerializableExtra("extra_customer");
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        customerNameText = (TextView) findViewById(R.id.customer_name);
        customerContentText = (TextView) findViewById(R.id.customer_content);
        workRecyclerView = (RecyclerView) findViewById(R.id.work_recyclerview);
        workRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workApdater = new WorkApdater();
        workRecyclerView.setAdapter(workApdater);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 获取订单数据
     */
    private void getWorkList(){
        if (customer != null){
            String cName = customer.getcName();
            String cContent = customer.getcContent();
            customerNameText.setText(cName);
            if (cContent.equals("")){
                customerContentText.setText("（为空）");
            }else{
                customerContentText.setText(cContent);
            }
            workList = DataSupport.where("cName = ?",cName).order("id desc").find(Work.class);
            workApdater.notifyDataSetChanged();
        }
    }

    class WorkApdater extends RecyclerView.Adapter<WorkApdater.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout workItemLayout;
            TextView workTime;
            TextView workSumPrice;
            public ViewHolder(View itemView) {
                super(itemView);
                workItemLayout = (LinearLayout) itemView.findViewById(R.id.work_item_layout);
                workTime = (TextView) itemView.findViewById(R.id.work_time);
                workSumPrice = (TextView) itemView.findViewById(R.id.work_sum_price);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AboutCustomerActivity.this).inflate(R.layout.work_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Work work = workList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH：mm");
            try{
                holder.workTime.setText(sdf.format(work.getwTime()));
                holder.workSumPrice.setText(work.getwPrice() + "");
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.workItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AboutCustomerActivity.this,AboutWorkActivity.class);
                    intent.putExtra("extra_work",work);
                    startActivity(intent);
                }
            });
            holder.workItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(AboutCustomerActivity.this,holder.workItemLayout);
                    popupMenu.getMenuInflater().inflate(R.menu.customer_popupmenu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.customer_delete:
                                    work.deleteCBox(work.getId());
                                    getWorkList();
                                    Toast.makeText(AboutCustomerActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                                    if (!customer.isHaveWork()){    //如果没有存在订单 那么删除该客户
                                        DataSupport.deleteAll(Customer.class,"cName = ?",customer.getcName());
                                        finish();
                                    }
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return workList.size();
        }
    }
}
