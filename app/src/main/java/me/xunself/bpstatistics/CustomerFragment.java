package me.xunself.bpstatistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by XunselF on 2018/1/22.
 */

public class CustomerFragment extends Fragment {
    private View view;

    List<Customer> customerList;

    private RecyclerView customerRecyclerView;

    private CustomerAdapter customerAdapter;

    //字母排序控件
    private SideBar mSideBar;
    //弹窗提示
    private TextView textDialog;

    //提示没有数据的页面
    private RelativeLayout noDataLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer,container,false);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //重新刷新更新菜单
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.customer_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add_customer:
                displayAddOrderDialog();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 初始化
     */
    private void initView(){
        mSideBar = (SideBar) view.findViewById(R.id.sideBar);
        textDialog = (TextView) view.findViewById(R.id.textDialog);
        mSideBar.setTextView(textDialog);
        mSideBar.setSideBar(mSideBar);
        noDataLayout = (RelativeLayout) view.findViewById(R.id.nodata_layout);
        customerRecyclerView = (RecyclerView) view.findViewById(R.id.customer_recyclerview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        customerRecyclerView.setLayoutManager(linearLayoutManager);
        customerAdapter = new CustomerAdapter();
        customerRecyclerView.setAdapter(customerAdapter);
        mSideBar.setOnTouchLetterChangedListener(new SideBar.OnTouchLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = getPositionForSection(s.toUpperCase().charAt(0));
                if(position != -1){
                    //滚动到对应的数据
                    linearLayoutManager.scrollToPositionWithOffset(position,0);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getCustomerList();
    }

    /**
     * 返回首次出现首字母的位置
     * @param section
     * @return
     */
    private int getPositionForSection(char section){
        for (int i = 0; i < customerList.size(); i++){
            String cName = customerList.get(i).getcLetter();    //获取首字母
            char firstStr = cName.toUpperCase().charAt(0);
            if (firstStr == section){
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取数据
     */
    private void getCustomerList(){
        customerList = DataSupport.findAll(Customer.class);
        List<Work> works = DataSupport.findAll(Work.class);
        List<CBox> cBoxes = DataSupport.findAll(CBox.class);
        for (int i = 0; i < customerList.size(); i++){
            Customer customer = customerList.get(i);
            String cPinYin = PinyinUnils.getPinYin(customer.getcName());        //获取拼音
            customer.setcPinYin(cPinYin);
            String sortString = cPinYin.substring(0,1).toUpperCase();           //获取首字母
            if(sortString.matches("[A-Z]")){
                //判断是否是字母
                customer.setcLetter(sortString);
            }else{
                customer.setcLetter("#");
            }
        }
        //进行排序
        Collections.sort(customerList);
        if (customerList.size() == 0){
            //当没有数据时，显示该页面
            noDataLayout.setVisibility(View.VISIBLE);
        }else{
            //当有数据时，隐藏
            noDataLayout.setVisibility(View.GONE);
            customerAdapter.notifyDataSetChanged();
        }
    }

    class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.customer_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Customer customer = customerList.get(position);
            holder.cNameText.setText(customer.getcName());

            holder.customerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),AboutCustomerActivity.class);
                    intent.putExtra("extra_customer",customer);
                    startActivity(intent);
                }
            });

            holder.customerLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(),holder.customerLayout);
                    popupMenu.getMenuInflater().inflate(R.menu.customer_popupmenu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.customer_delete:
                                    customer.deleteCustomer();
                                    getCustomerList();
                                    Toast.makeText(getActivity(),"删除成功！",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    return false;
                }
            });

            List<Work> works = DataSupport.where("cName = ?",customer.getcName()).find(Work.class);

            if (works.size() != 0){
                Work work = works.get(works.size() - 1);
                holder.oPriceText.setText(work.getwPrice() + "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                holder.oTimeText.setText(sdf.format(work.getwTime()));
            }
        }

        @Override
        public int getItemCount() {
            return customerList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout customerLayout;
            TextView cNameText;
            TextView oTimeText;
            TextView oPriceText;
            public ViewHolder(View itemView) {
                super(itemView);
                cNameText = (TextView) itemView.findViewById(R.id.customer_name);
                oTimeText = (TextView) itemView.findViewById(R.id.order_time);
                oPriceText = (TextView) itemView.findViewById(R.id.order_price);
                customerLayout = (LinearLayout) itemView.findViewById(R.id.customer_layout);
            }
        }
    }

    /**
     * 添加订单的弹窗
     */
    private void displayAddOrderDialog(){
        final List<String> prices = new ArrayList<>();
        List<Price> priceList = DataSupport.findAll(Price.class);

        for (int i = 0; i < priceList.size(); i++){
            prices.add(priceList.get(i).getPriceName());
        }

        View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.selectprice_dialog,null);
        final Spinner selectPriceSpinner = (Spinner) dialog.findViewById(R.id.select_prize_spinner);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, prices);
        selectPriceSpinner.setAdapter(adapter);
        final PriceItemSelectedListener priceItemSelectedListener = new PriceItemSelectedListener();
        selectPriceSpinner.setOnItemSelectedListener(priceItemSelectedListener);
        new AlertDialog.Builder(getActivity()).setView(dialog)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = priceItemSelectedListener.getPosition();
                        String price = prices.get(position);
                        Intent intent = new Intent(getActivity(),SelectBoxActivity.class);
                        intent.putExtra("extra_price",price);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null).show();
    }
    class PriceItemSelectedListener implements AdapterView.OnItemSelectedListener{

        int position = 0;

        public int getPosition(){
            return position;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            position = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
