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
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setoTime(new Date());
        order.setoPrice(1.22);
        orders.add(order);
        for (int i = 0; i < 20; i++){
            Customer customer = new Customer();
            customer.setcName("曹客户");
            customer.setOrderList(orders);
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

            List<Order> orders = customer.getOrderList();

            if (orders.size() != 0){
                Order order = orders.get(orders.size() - 1);
                holder.oPriceText.setText(order.getoPrice() + "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                holder.oTimeText.setText(sdf.format(order.getoTime()));
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
}
