package me.xunself.bpstatistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        customerRecyclerView = (RecyclerView) view.findViewById(R.id.customer_recyclerview);
        customerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        customerAdapter = new CustomerAdapter();
        customerRecyclerView.setAdapter(customerAdapter);
        getCustomerList();
    }

    /**
     * 获取数据
     */
    private void getCustomerList(){
        customerList = new ArrayList<>();
        List<Work> works = new ArrayList<>();
        Work work = new Work();
        work.setwTime(new Date());
        work.setwPrice(1.22);
        works.add(work);
        for (int i = 0; i < 20; i++){
            Customer customer = new Customer();
            customer.setcName("曹客户");
            customer.setWorkList(works);
            customerList.add(customer);
        }
        customerAdapter.notifyDataSetChanged();
    }

    class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.customer_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Customer customer = customerList.get(position);
            holder.cNameText.setText(customer.getcName());

            List<Work> works = customer.getWorkList();

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
            TextView cNameText;
            TextView oTimeText;
            TextView oPriceText;
            public ViewHolder(View itemView) {
                super(itemView);
                cNameText = (TextView) itemView.findViewById(R.id.customer_name);
                oTimeText = (TextView) itemView.findViewById(R.id.order_time);
                oPriceText = (TextView) itemView.findViewById(R.id.order_price);
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
