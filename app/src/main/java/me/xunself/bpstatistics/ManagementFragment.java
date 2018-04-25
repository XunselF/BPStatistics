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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XunselF on 2018/1/22.
 */

public class ManagementFragment extends Fragment {
    private View view;

    private Toolbar toolbar;
    private LinearLayout bottomLayout;
    private LinearLayout itemSelectedTitle;

    private Button cancelButton;
    private Button noSelectAllButton;
    private Button selectAllButton;
    private RelativeLayout deleteAllButton;
    private TextView itemNumber;

    private RecyclerView boxRecyclerview;
    private BoxAdapter boxAdapter;

    private List<Box> boxList;

    private SideBar sideBar;
    private TextView textDialog;



    private RelativeLayout dataInfoLayout;
    private RelativeLayout noDataInfoLayout;

    private PinyinComparator pinyinComparator = new PinyinComparator();

    InputMethodManager inputMethodManager;

    private boolean ifItemSelected = false;
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
        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolBar);
        bottomLayout = (LinearLayout) getActivity().findViewById(R.id.bottom_layout);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        noSelectAllButton = (Button) view.findViewById(R.id.no_select_all);
        selectAllButton = (Button) view.findViewById(R.id.select_all);
        itemNumber = (TextView) view.findViewById(R.id.item_number);
        deleteAllButton = (RelativeLayout) view.findViewById(R.id.delete_all_layout);
        itemSelectedTitle = (LinearLayout) view.findViewById(R.id.itemselected_title);
        sideBar = (SideBar) view.findViewById(R.id.sideBar);
        textDialog = (TextView) view.findViewById(R.id.textDialog);
        dataInfoLayout = (RelativeLayout) view.findViewById(R.id.datainfo_layout);
        noDataInfoLayout = (RelativeLayout) view.findViewById(R.id.nodatainfo_layout);
        boxRecyclerview = (RecyclerView) view.findViewById(R.id.box_recyclerview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        boxRecyclerview.setLayoutManager(linearLayoutManager);
        boxAdapter = new BoxAdapter();
        boxRecyclerview.setAdapter(boxAdapter);

        sideBar.setTextView(textDialog);
        sideBar.setSideBar(sideBar);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退出多选模式
                boxAdapter.ifDisplayItemSelected(false);
            }
        });
        noSelectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boxAdapter.noSelectAllItem();
            }
        });
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            boxAdapter.selectAllItem();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boxAdapter.deleteAllItem();
            }
        });



        sideBar.setOnTouchLetterChangedListener(new SideBar.OnTouchLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = getPositionForSection(s.toUpperCase().charAt(0));
                if (position != -1) {
                    //跳转到首次出现该字符的位置
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

        getBoxList();

    }

    /**
     * 根据首字符找出第一次出现该首字母的位置
     */
    private int getPositionForSection(int section){
        for (int i = 0; i < boxList.size(); i++){
            String sortStr = boxList.get(i).getbLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section){
                return i;
            }
        }
        return -1;
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

        if (boxList.size() == 0){
            dataInfoLayout.setVisibility(View.GONE);
            noDataInfoLayout.setVisibility(View.VISIBLE);
        }else{
            dataInfoLayout.setVisibility(View.VISIBLE);
            noDataInfoLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < boxList.size(); i++){
            Box box = boxList.get(i);
            String pinYin = PinyinUnils.getPinYin(box.getbName());

            String sortString = pinYin.substring(0,1).toUpperCase();

            box.setbPinyin(pinYin.toUpperCase());

            //通过正则表达式判断首字母为英文字母
            if (sortString.matches("[A-Z]")){
                box.setbLetter(sortString);
            }else{
                box.setbLetter("#");
            }
            boxList.set(i,box);
        }

        //进行排序
        Collections.sort(boxList,pinyinComparator);





        boxAdapter.notifyDataSetChanged();
    }



    /**
     * 获取数据
     */
    private List<BoxPrice> getBoxPriceList(String boxName){
        List<BoxPrice> boxPriceList = new ArrayList<>();
        List<Price> prices = DataSupport.findAll(Price.class);
        for (Price price : prices){
            List<BoxPrice> boxPrices = DataSupport.where("bName = ? and ifLatest = ? and pName = ?",boxName,"1",price.getPriceName()).find(BoxPrice.class);
            if (boxPrices.size() != 0){
                boxPriceList.add(boxPrices.get(0));
            }
        }
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
        inflater.inflate(R.menu.management_menu,menu);
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

        SparseBooleanArray mSelectedPositions = new SparseBooleanArray();

        HashMap<Integer,Boolean> boxHashMap = new HashMap<>();

        //设置状态
        private void setItemChecked(int position,boolean isChecked){
            mSelectedPositions.put(position,isChecked);
        }

        //获取状态
        private boolean isItemChecked(int position){
            return mSelectedPositions.get(position);
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            CardView boxitemLayout;
            TextView boxNameText;
            TextView boxContentText;
            TextView ifHavePrice;
            RecyclerView boxPriceRecyclerView;
            CheckBox boxItemSelected;
            public ViewHolder(View itemView) {
                super(itemView);
                boxitemLayout = (CardView) itemView.findViewById(R.id.box_item_layout);
                boxNameText = (TextView) itemView.findViewById(R.id.box_name);
                boxContentText = (TextView) itemView.findViewById(R.id.box_content);
                ifHavePrice = (TextView) itemView.findViewById(R.id.ifHavePrice);
                boxPriceRecyclerView = (RecyclerView) itemView.findViewById(R.id.box_price_recyclerview);
                boxItemSelected = (CheckBox) itemView.findViewById(R.id.box_selected);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Box box = boxList.get(position);
            //数据加载  对纸箱姓名，备注的加载
            holder.boxNameText.setText(box.getbName());
            if (box.getbContent().equals("")){
                holder.boxContentText.setText("(为空)");
            }else{
                holder.boxContentText.setText(box.getbContent());
            }

            //显示各类价格
            BoxPriceAdapter boxPriceAdapter = new BoxPriceAdapter(getBoxPriceList(box.getbName()));
            if (getBoxPriceList(box.getbName()).size() == 0){
                holder.ifHavePrice.setVisibility(View.VISIBLE);
            }else{
                holder.ifHavePrice.setVisibility(View.GONE);
            }
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
            holder.boxPriceRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            holder.boxPriceRecyclerView.setAdapter(boxPriceAdapter);

            //对UI的修改
            if (ifItemSelected){    //判断是否进入多选模式
                ifSelect(holder,position);
            }else{
                //如果不是多选模式则隐藏
                holder.boxItemSelected.setVisibility(View.GONE);
                holder.boxitemLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
            }

            //对点击数据的监听（分进入多选模式或者正常模式）
            holder.boxitemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ifItemSelected) {   //判断是否进入多选模式
                        //正常模式 进入详细页面
                        Intent intent = new Intent(getActivity(), BoxInfoActivity.class);
                        intent.putExtra("extra_box", box);
                        startActivity(intent);
                    }else {
                        selectOrCancel(position);
                    }
                }
            });

            /**
             * 长按键 可以进行删除操作
             */
            holder.boxitemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!ifItemSelected){   //进入多选模式后就不用继续进行响应
                        //被长按的按钮默认选中
                       selectOrCancel(position);
                        // 开启多选模式
                        ifDisplayItemSelected(true);
                    }
                    return true;
                }
            });

        }

        @Override
        public int getItemCount() {
            return boxList.size();
        }

        /**
         * 进入多选模式进行删除
         */
        public void ifDisplayItemSelected(boolean ifSelect){
            //参数初始化
            int select = View.VISIBLE;
            int noSelect = View.GONE;
            //将多选模式参数修改为true
            ifItemSelected = ifSelect;

            //进行判断哪些控件应该被隐藏
            if (!ifItemSelected){
                select = View.GONE;
                noSelect = View.VISIBLE;
                //对数据进行清空
                boxHashMap.clear();
                mSelectedPositions.clear();
            }
            //UI布局修改
            toolbar.setVisibility(noSelect);       //标题
            bottomLayout.setVisibility(noSelect);      //下方菜单
            sideBar.setVisibility(noSelect);           //右边排序控件
            itemSelectedTitle.setVisibility(select);        //选择标题栏
            deleteAllButton.setVisibility(select);            //删除键

            boxAdapter.notifyDataSetChanged();
        }



        /**
         * 显示数据的状态
         * @param holder
         * @param position
         */
        private void ifSelect(ViewHolder holder,int position){
            updateBoxList();
            //确定进入多选模式 显示选中项
            holder.boxItemSelected.setVisibility(View.VISIBLE);
            if(isItemChecked(position)){    //如果是选中的状态
                holder.boxItemSelected.setChecked(true);
                holder.boxitemLayout.setBackgroundResource(R.drawable.selected_layout);
            }else{
                holder.boxItemSelected.setChecked(false);
                holder.boxitemLayout.setBackgroundResource(R.drawable.box_item_background);
            }
        }
        /**
         * 点击数据 进行选择或取消
         */
        public void selectOrCancel(int position) {
            boolean ifSelect = !isItemChecked(position);

            setItemChecked(position, ifSelect);

            getItemNumber();//统计数据

            boxAdapter.notifyDataSetChanged();
        }




        /**
         * 全选
         */
        public void selectAllItem(){
            if (mSelectedPositions != null){
                mSelectedPositions.clear();
            }

            for (int i = 0; i < boxList.size(); i++){
                setItemChecked(i,true);
            }
            boxAdapter.notifyDataSetChanged();
        }

        /**
         * 全不选
         */
        public void noSelectAllItem(){
            mSelectedPositions.clear();
            boxAdapter.notifyDataSetChanged();
        }

        /**
         * 进行删除操作
         */
        public void deleteAllItem(){
            if (boxHashMap.size() != 0){
                for (Map.Entry<Integer,Boolean> box : boxHashMap.entrySet()){
                    if (box.getValue()){
                        DataSupport.delete(Box.class,boxList.get(box.getKey()).getId());
                        DataSupport.deleteAll(BoxPrice.class,"bName = ?",boxList.get(box.getKey()).getbName());
                    }
                }
                ManagementFragment.this.getBoxList();
                boxAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"删除成功！",Toast.LENGTH_SHORT).show();
                ifDisplayItemSelected(false);//退出多选模式
            }
        }

        public void getItemNumber(){
            List<Boolean> selectList = new ArrayList<>();
            for (int i = 0; i < boxList.size(); i++){
                if (isItemChecked(i)){
                    selectList.add(mSelectedPositions.get(i));
                }
            }
            itemNumber.setText(selectList.size() + "");
        }

        private void updateBoxList() {
            if (boxHashMap != null) {
                boxHashMap.clear();
            } else {
                boxHashMap = new HashMap<>();
            }
            for (int i = 0; i < boxList.size(); i++) {
                boxHashMap.put(i, mSelectedPositions.get(i));
            }
        }
            }


    class BoxPriceAdapter extends RecyclerView.Adapter<BoxPriceAdapter.ViewHolder>{
        List<BoxPrice> boxPriceList;
        class ViewHolder extends RecyclerView.ViewHolder{

            CardView boxPriceLayout;
            TextView boxPriceText;
            TextView bPNameText;
            public ViewHolder(View itemView) {
                super(itemView);
                boxPriceLayout = (CardView) itemView.findViewById(R.id.cardview_boxPrice_layout);
                boxPriceText = (TextView) itemView.findViewById(R.id.box_price);
                bPNameText = (TextView) itemView.findViewById(R.id.box_price_name);
            }
        }
        public BoxPriceAdapter(List<BoxPrice> boxPriceList){
            this.boxPriceList = boxPriceList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.boxprice_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            BoxPrice boxPrice = boxPriceList.get(position);
            holder.bPNameText.setText(boxPrice.getpName());
            holder.boxPriceText.setText(boxPrice.getbPrice() + "");



            //点击模式 -- 进入该商品资料 或 在多选模式下选择
            holder.boxPriceLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ifItemSelected){
                        //多选模式下

                    }else{
                        //普通点击模式下

                    }
                }
            });

            //长按事件  -- 进入多选模式
            holder.boxPriceLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!ifItemSelected){            //判断是否进入多选模式
                        multipleSelect(position);
                    }
                    return true;
                }
            });
        }


        /**
         * 进入多选模式
         */
        public void multipleSelect(int position){
            //进入多选模式
            boxAdapter.selectOrCancel(position);
            boxAdapter.ifDisplayItemSelected(true);
            boxAdapter.notifyDataSetChanged();
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
        final RecyclerView boxPriceRecyclerView = (RecyclerView) view.findViewById(R.id.dialog_box_price_recyclerview);
        boxPriceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final PriceAdapter priceAdapter = new PriceAdapter(DataSupport.findAll(Price.class));
        boxPriceRecyclerView.setAdapter(priceAdapter);

        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        boolean ifExist = false;

                        String boxName = inputBoxName.getText().toString().trim();
                        String boxContent = inputBoxContent.getText().toString().trim();


                        SparseArray<String> priceList = priceAdapter.getPrice();

                        if (boxName == null || boxName.trim().equals("")){
                            OperateDialog.noCloseDialog(dialogInterface);
                            Toast.makeText(getActivity(),"请填写纸箱型号！",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (Box box : boxList){
                            if (box.getbName().equals(boxName)){
                                ifExist = true;
                                break;
                            }
                        }


                        Box box = new Box(boxName,boxContent,new Date());

                        if (!ifExist){
                            if (box.save()){
                                Toast.makeText(getActivity(),"保存成功！",Toast.LENGTH_SHORT).show();

                                if (priceList.size() != 0){
                                    BoxPrice boxPrice;

                                    int key = 0;//用于进行搜索集合的索引
                                    for (int j = 0; j < priceList.size(); j++){
                                        key = priceList.keyAt(j);   //获取索引

                                        try{
                                            boxPrice = new BoxPrice(boxName,DataSupport.findAll(Price.class).get(key).getPriceName(),Double.valueOf(priceList.get(key)),new Date(),1);
                                            if (boxPrice.save()){
                                                Log.d("price",DataSupport.findAll(Price.class).get(key).getPriceName() + " success");
                                            }
                                        }catch (NumberFormatException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(getActivity(),"保存失败！",Toast.LENGTH_SHORT).show();
                            }
                        }else{


                            Box oldBox = DataSupport.where("bName = ?",boxName).find(Box.class).get(0);
                            Box newBox = new Box();
                            if (oldBox.getbContent().equals("")){
                                newBox.setbContent(boxContent);
                            }else if (!boxContent.equals("")){
                                newBox.setbContent(oldBox.getbContent() + "\n" + boxContent);
                            }
                            newBox.update(oldBox.getId());

                            if (priceList.size() != 0){
                                BoxPrice boxPrice;

                                int key = 0;//用于进行搜索集合的索引
                                int number = 0;
                                for (int j = 0; j < priceList.size(); j++){
                                    key = priceList.keyAt(j);   //获取索引
                                    if (!priceList.get(key).equals("")){
                                        boxPrice = new BoxPrice(boxName,DataSupport.findAll(Price.class).get(key).getPriceName(),Double.valueOf(priceList.get(key)),new Date(),BoxPrice.LATEST_PRICE);

                                        //对之前的数据进行修改
                                        BoxPrice.updateIfLatest(boxName,DataSupport.findAll(Price.class).get(key).getPriceName());

                                        if (boxPrice.save()){
                                            Log.d("price",DataSupport.findAll(Price.class).get(key).getPriceName() + " success");
                                        }
                                    }else{
                                        number ++;
                                    }

                                }
                                if (number == priceList.size()){
                                    OperateDialog.noCloseDialog(dialogInterface);
                                    Toast.makeText(getActivity(),"该纸箱已存在！",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(getActivity(),"已为您修改为最新价格！",Toast.LENGTH_SHORT).show();
                            }else{
                                OperateDialog.noCloseDialog(dialogInterface);
                                Toast.makeText(getActivity(),"该纸箱已存在！",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }



                        OperateDialog.closeDialog(dialogInterface);

                        getBoxList();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OperateDialog.closeDialog(dialogInterface);
                    }
                })
                .show();
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
