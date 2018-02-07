package me.xunself.bpstatistics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SelectBoxActivity extends AppCompatActivity {

    /**
     * 标题栏按钮
     */
    private Button mCancelButton;
    private TextView mItemSelectNumber;
    private Button mNoSelectButton;
    private Button mSelectButton;

    private String mSelectPrice;

    private List<Box> boxList;

    private RecyclerView mSelectBoxRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_box);
        initView();
    }
    private void initView(){
        mSelectPrice = getIntent().getStringExtra("extra_price");
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mItemSelectNumber = (TextView) findViewById(R.id.item_number);
        mNoSelectButton = (Button) findViewById(R.id.no_select_all);
        mSelectButton = (Button) findViewById(R.id.select_all);
        mSelectBoxRecyclerView = (RecyclerView) findViewById(R.id.select_box_recyclerview);
    }

    class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder>{

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
        public void onBindViewHolder(ViewHolder holder, int position) {
            Box box = boxList.get(position);
            holder.boxName.setText(box.getbName());
        }

        @Override
        public int getItemCount() {
            return boxList.size();
        }
    }
}
