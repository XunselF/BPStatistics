package me.xunself.bpstatistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XunselF on 2018/1/22.
 */

public class ManagementFragment extends Fragment {
    private View view;

    private RecyclerView boxRecyclerview;
    private BoxAdapter boxAdapter;

    private List<Box> boxList;
    private List<BoxPrice> boxPriceList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_management,container,false);
        init();
        return view;
    }

    /**
     * 初始化
     */
    private void init(){
        boxList = new ArrayList<>();
        getBoxList();
        boxRecyclerview = (RecyclerView) view.findViewById(R.id.box_recyclerview);
        boxRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        boxAdapter = new BoxAdapter();
        boxRecyclerview.setAdapter(boxAdapter);
    }

    /**
     * 获取数据
     */
    private void getBoxList(){
        for (int i = 0; i < 20; i ++){
            Box box = new Box("test","123",new Date());
            boxList.add(box);
        }
        getBoxPrizeList();
    }

    /**
     * 获取数据
     */
    private void getBoxPrizeList(){
        boxPriceList = new ArrayList<>();
        for (int i = 0; i < 4; i ++){
            BoxPrice boxPrice = new BoxPrice("box","A",1.2,new Date());
            boxPriceList.add(boxPrice);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 用于设置全局变量mHasMenu，用于执行onCreateOptionsMenu
         */
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.management_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add:
                Toast.makeText(getActivity(),"test",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_setting:
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            CardView boxitemLayout;
            TextView boxNameText;
            TextView boxContentText;
            RecyclerView boxPrizeRecyclerView;
            public ViewHolder(View itemView) {
                super(itemView);
                boxitemLayout = (CardView) itemView.findViewById(R.id.box_item_layout);
                boxNameText = (TextView) itemView.findViewById(R.id.box_name);
                boxContentText = (TextView) itemView.findViewById(R.id.box_content);
                boxPrizeRecyclerView = (RecyclerView) itemView.findViewById(R.id.box_prize_recyclerview);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Box box = boxList.get(position);
            holder.boxitemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"test",Toast.LENGTH_SHORT).show();
                }
            });
            holder.boxNameText.setText(box.getbName());
            holder.boxContentText.setText(box.getbContent());
            BoxPrizeAdapter boxPrizeAdapter = new BoxPrizeAdapter();
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            holder.boxPrizeRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            holder.boxPrizeRecyclerView.setAdapter(boxPrizeAdapter);
        }

        @Override
        public int getItemCount() {
            return boxList.size();
        }
    }

    class BoxPrizeAdapter extends RecyclerView.Adapter<BoxPrizeAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView boxPriceText;
            TextView bPNameText;
            public ViewHolder(View itemView) {
                super(itemView);
                boxPriceText = (TextView) itemView.findViewById(R.id.box_price);
                bPNameText = (TextView) itemView.findViewById(R.id.box_price_name);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.boxprize_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BoxPrice boxPrice = boxPriceList.get(position);
            holder.bPNameText.setText(boxPrice.getpName());
            holder.boxPriceText.setText(boxPrice.getbPrice() + "");
        }

        @Override
        public int getItemCount() {
            return boxPriceList.size();
        }
    }
}
