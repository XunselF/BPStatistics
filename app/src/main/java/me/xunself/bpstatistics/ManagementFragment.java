package me.xunself.bpstatistics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
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


    InputMethodManager inputMethodManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_management,container,false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getBoxList();
    }

    /**
     * 初始化
     */
    private void init(){
        boxRecyclerview = (RecyclerView) view.findViewById(R.id.box_recyclerview);
        boxRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        boxAdapter = new BoxAdapter();
        boxRecyclerview.setAdapter(boxAdapter);

        getBoxList();
    }

    /**
     * 获取数据
     */
    private void getBoxList(){
        if (boxList == null){
            boxList = new ArrayList<>();
        }else{
            boxList.clear();
        }
        boxList = DataSupport.findAll(Box.class);
        boxAdapter.notifyDataSetChanged();
    }

    /**
     * 获取数据
     */
    private List<BoxPrice> getBoxPrizeList(String boxName){
        List<BoxPrice> boxPriceList = new ArrayList<>();
        boxPriceList = DataSupport.where("bName = ? and ifLatest = ?",boxName,"1").find(BoxPrice.class);
        return boxPriceList;
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
                showAddBoxDialog();
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
            TextView ifHavePrice;
            RecyclerView boxPrizeRecyclerView;
            public ViewHolder(View itemView) {
                super(itemView);
                boxitemLayout = (CardView) itemView.findViewById(R.id.box_item_layout);
                boxNameText = (TextView) itemView.findViewById(R.id.box_name);
                boxContentText = (TextView) itemView.findViewById(R.id.box_content);
                ifHavePrice = (TextView) itemView.findViewById(R.id.ifHavePrice);
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
            if (box.getbContent().equals("")){
                holder.boxContentText.setText("(为空)");
            }else{
                holder.boxContentText.setText(box.getbContent());
            }

            BoxPriceAdapter boxPrizeAdapter = new BoxPriceAdapter(getBoxPrizeList(box.getbName()));
            if (getBoxPrizeList(box.getbName()).size() == 0){
                holder.ifHavePrice.setVisibility(View.VISIBLE);
            }else{
                holder.ifHavePrice.setVisibility(View.GONE);
            }
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            holder.boxPrizeRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            holder.boxPrizeRecyclerView.setAdapter(boxPrizeAdapter);
        }

        @Override
        public int getItemCount() {
            return boxList.size();
        }
    }

    class BoxPriceAdapter extends RecyclerView.Adapter<BoxPriceAdapter.ViewHolder>{
        List<BoxPrice> boxPriceList;
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView boxPriceText;
            TextView bPNameText;
            public ViewHolder(View itemView) {
                super(itemView);
                boxPriceText = (TextView) itemView.findViewById(R.id.box_price);
                bPNameText = (TextView) itemView.findViewById(R.id.box_price_name);
            }
        }
        public BoxPriceAdapter(List<BoxPrice> boxPriceList){
            this.boxPriceList = boxPriceList;
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
    private void hideInputMethod(LinearLayout layout){
        if (inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
            layout.clearFocus();
            layout.setFocusable(false);
        }
    }

    /**
     * 添加纸箱信息的弹窗
     */
    private void showAddBoxDialog(){
        View view = (LinearLayout) getLayoutInflater().inflate(R.layout.addbox_dialog,null);
        final EditText inputBoxName = (EditText) view.findViewById(R.id.input_boxName);
        final EditText inputBoxContent = (EditText) view.findViewById(R.id.input_boxContent);
        final LinearLayout addBoxDialogLayout = (LinearLayout) view.findViewById(R.id.addbox_dialog_layout);
        addBoxDialogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideInputMethod(addBoxDialogLayout);
            }
        });
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final RecyclerView boxPriceRecyclerView = (RecyclerView) view.findViewById(R.id.box_prize_recyclerview);
        boxPriceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final PriceAdapter priceAdapter = new PriceAdapter(DataSupport.findAll(Price.class));
        boxPriceRecyclerView.setAdapter(priceAdapter);

        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String boxName = inputBoxName.getText().toString();
                        String boxContent = inputBoxContent.getText().toString();


                        SparseArray<String> priceList = priceAdapter.getPrice();

                        if (boxName == null || boxName.trim().equals("")){
                            noCloseDialog(dialogInterface);
                            Toast.makeText(getActivity(),"请填写纸箱型号！",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (Box box : boxList){
                            if (box.getbName().equals(boxName)){
                                noCloseDialog(dialogInterface);
                                Toast.makeText(getActivity(),"您填写的纸箱已存在！",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }


                        Box box = new Box(boxName,boxContent,new Date());


                        if (box.save()){
                            Toast.makeText(getActivity(),"保存成功！",Toast.LENGTH_SHORT).show();

                            if (priceList.size() != 0){
                                BoxPrice boxPrice;

                                int key = 0;//用于进行搜索集合的索引
                                for (int j = 0; j < priceList.size(); j++){
                                    key = priceList.keyAt(j);   //获取索引

                                    boxPrice = new BoxPrice(boxName,DataSupport.findAll(Price.class).get(key).getPriceName(),Double.valueOf(priceList.get(key)),new Date(),1);

                                    if (boxPrice.save()){
                                        Log.d("price",DataSupport.findAll(Price.class).get(key).getPriceName() + " success");
                                    }
                                }
                            }
                        }else{
                            Toast.makeText(getActivity(),"保存失败！",Toast.LENGTH_SHORT).show();
                        }


                        closeDialog(dialogInterface);

                        getBoxList();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeDialog(dialogInterface);
                    }
                })
                .show();
    }

    /**
     * 不关闭dialog
     */
    private void noCloseDialog(DialogInterface dialog){
        //不关闭对话框
        try{
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭dialog
     */
    private void closeDialog(DialogInterface dialog){
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder>{

        List<Price> prices;
        //当前输入框的位置
        int etFocusPos = -1;
        //输入法

        SparseArray<String> priceList = new SparseArray<>();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                priceList.put(etFocusPos,editable.toString());
            }
        };

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout addBoxItemLayout;
            TextView priceName;
            EditText boxPrice;
            public ViewHolder(View itemView) {
                super(itemView);
                priceName = (TextView) itemView.findViewById(R.id.boxPrice_name);
                boxPrice = (EditText) itemView.findViewById(R.id.input_boxPrice);
                addBoxItemLayout = (LinearLayout) itemView.findViewById(R.id
                        .addbox_item_layout);
            }
        }
        public PriceAdapter(List<Price> prices){
            this.prices = prices;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_boxprice_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Price price = prices.get(position);
            holder.priceName.setText(price.getPriceName());


            holder.addBoxItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideInputMethod(holder.addBoxItemLayout);
                }
            });

            holder.boxPrice.setOnTouchListener(new View.OnTouchListener() {
                @Override
                //当抬起时，记录下当前ed的位置
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        etFocusPos = position;
                    }
                    return false;
                }
            });

            holder.boxPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    etFocusPos = position;
                }
            });




            holder.boxPrice.setText(priceList.get(position));
            //添加监听
            holder.boxPrice.addTextChangedListener(textWatcher);
        }


        /**
         * Called when a view created by this adapter has been detached from its window.
         * @param holder
         */
        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            if (etFocusPos == holder.getAdapterPosition()){
                inputMethodManager.hideSoftInputFromWindow(holder.boxPrice.getWindowToken(),0);
            }
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.boxPrice.removeTextChangedListener(textWatcher);
        }

        @Override
        public int getItemCount() {
            return prices.size();
        }

        public SparseArray<String> getPrice(){
            return priceList;
        }

    }

}
