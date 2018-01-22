package me.xunself.bpstatistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
            Box box = new Box("test","",new Date());
            boxList.add(box);
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
            default:
                break;
        }
        return true;
    }

    class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView boxNameText;
            public ViewHolder(View itemView) {
                super(itemView);
                boxNameText = (TextView) itemView.findViewById(R.id.box_name);
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
            holder.boxNameText.setText(box.getbName());
        }

        @Override
        public int getItemCount() {
            return boxList.size();
        }
    }
}
