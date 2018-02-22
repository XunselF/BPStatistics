package me.xunself.bpstatistics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddCustomerActivity extends AppCompatActivity {

    private List<BoxPrice> boxPrices;

    private String mCustomerName;

    private Toolbar toolbar;

    private EditText inputCustomerEdit;
    private EditText inputContentEdit;
    private Button addCustomerButton;

    private LinearLayout selectBeforeCustomer;

    private LinearLayout inputCustomerLayout;
    private LinearLayout displayCustomerLayout;
    private TextView customerNames;
    private TextView cancelButton;

    private TextView allBoxPriceSum;

    private RecyclerView boxRecyclerView;

    private BoxAdapter boxAdapter;

    private InputMethodManager inputMethodManager;

    public static final int REQUEST_CODE = 1;   //结果码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        initView();
    }

    private void initView(){
        boxPrices = (List<BoxPrice>) getIntent().getSerializableExtra("extra_boxprice");
        toolbar = (Toolbar) findViewById(R.id.toolBar);

        allBoxPriceSum = (TextView) findViewById(R.id.price_sum);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        selectBeforeCustomer = (LinearLayout) findViewById(R.id.select_before_customer);
        inputCustomerLayout = (LinearLayout) findViewById(R.id.inputCustomer_layout);
        displayCustomerLayout = (LinearLayout) findViewById(R.id.display_customer_layout);
        customerNames = (TextView) findViewById(R.id.display_customer_name);
        cancelButton = (TextView) findViewById(R.id.cancel_button);
        inputCustomerEdit = (EditText) findViewById(R.id.input_customer_name);
        inputContentEdit = (EditText) findViewById(R.id.input_customer_content);
        addCustomerButton = (Button) findViewById(R.id.add_button);

        displayLayout(null);
        boxRecyclerView = (RecyclerView) findViewById(R.id.box_recyclerview);
        boxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        boxAdapter = new BoxAdapter();
        boxRecyclerView.setAdapter(boxAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = inputCustomerEdit.getText().toString().trim();
                if (cName.equals("")){
                    if (customerNames.getText().equals("")){
                        Toast.makeText(AddCustomerActivity.this,"请填写客户名！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cName = customerNames.getText().toString();
                }
                if (!boxAdapter.isInputNumber()){
                    Toast.makeText(AddCustomerActivity.this,"请填完整全部数量！",Toast.LENGTH_SHORT).show();
                    return;
                }
                addCustomer(cName);
            }
        });

        selectBeforeCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCustomerActivity.this,SelectCustomerActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLayout(null);
            }
        });
    }

    private void displayLayout(String customerName){
        if (customerName != null){
            inputCustomerLayout.setVisibility(View.GONE);
            displayCustomerLayout.setVisibility(View.VISIBLE);
            customerNames.setText(customerName);
            inputContentEdit.setText("");
            inputContentEdit.setText("");
        }else{
            inputCustomerLayout.setVisibility(View.VISIBLE);
            displayCustomerLayout.setVisibility(View.GONE);
            customerNames.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    String cName = data.getStringExtra("return_customer");
                    displayLayout(cName);
                }
                break;
        }
    }

    class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder>{

        SparseArray<String> inputNumbers = new SparseArray<>();
        SparseArray<ViewHolder> holderList = new SparseArray<>();
        //输入框位置
        int mFocusPos = -1;

        public SparseArray<String> getInputNumbers(){
            return inputNumbers;
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputNumbers.put(mFocusPos,charSequence.toString());
                displayItemPrice(mFocusPos);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputNumbers.put(mFocusPos,editable.toString());
                displayItemPrice(mFocusPos);
            }
        };

        /**
         * Called when a view created by this adapter has been detached from its window.
         * @param holder
         */
        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            if (mFocusPos == holder.getAdapterPosition()){
                inputMethodManager.hideSoftInputFromWindow(holder.inputNumberEdit.getWindowToken(),0);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AddCustomerActivity.this).inflate(R.layout.add_customer_box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            BoxPrice boxPrice = boxPrices.get(position);
            holderList.put(position,holder);

            holder.inputNumberEdit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        mFocusPos = position;
                    }
                    return false;
                }
            });
            holder.inputNumberEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    mFocusPos = position;
                }
            });
            holder.inputNumberEdit.addTextChangedListener(textWatcher);

            if (isInputNumber(position) != null){
                holder.inputNumberEdit.setText(isInputNumber(position));
                holder.inputNumberEdit.setSelection(isInputNumber(position).length());
            }

            displayItemPrice(position);
            holder.boxNameText.setText(boxPrice.getbName());
            holder.boxPriceText.setText(boxPrice.getbPrice() + "");
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.inputNumberEdit.removeTextChangedListener(textWatcher);
        }



        @Override
        public int getItemCount() {
            return boxPrices.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout boxItemLayout;
            TextView boxNameText;
            TextView boxPriceText;
            EditText inputNumberEdit;
            TextView sumPriceText;
            public ViewHolder(View itemView) {
                super(itemView);
                boxItemLayout = (LinearLayout) itemView.findViewById(R.id.box_item_layout);
                boxNameText = (TextView) itemView.findViewById(R.id.box_name);
                boxPriceText = (TextView) itemView.findViewById(R.id.box_price);
                inputNumberEdit = (EditText) itemView.findViewById(R.id.input_number);
                sumPriceText = (TextView) itemView.findViewById(R.id.item_price_sum);
            }
        }

        /**
         * 获取数据
         * @param position
         * @return
         */
        private String isInputNumber(int position){
            return inputNumbers.get(position);
        }

        /**
         * 获取每个纸箱的价格
         * @param position
         */
        private double getItemPriceSum(int position){
            BoxPrice boxPrice = boxPrices.get(position);
            double price = boxPrice.getbPrice();
            double priceSum = 0;
            String input = inputNumbers.get(position);
            if (input != null){
                try{
                    int number = input.equals("") ? 0:Integer.valueOf(input);
                    priceSum = (double)(number * price);
                    //保留小数位
                    BigDecimal bigDecimal = new BigDecimal(priceSum);
                    priceSum = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
            return priceSum;
        }


        /**
         * 显示单个纸箱的数据
         * @param position
         */
        private void displayItemPrice(int position){
            ViewHolder holder = holderList.get(position);
            holder.sumPriceText.setText(getItemPriceSum(position) + "");
            allBoxPriceSum.setText(String.format("%.2f",displayAllPrice()));
        }

        /**
         * 计算总价
         * @return
         */
        public double displayAllPrice(){
            double priceSum = 0;
            for (int i = 0; i < boxPrices.size(); i++){
                priceSum += getItemPriceSum(i);
            }
            BigDecimal bigDecimal = new BigDecimal(priceSum);
            priceSum = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            return priceSum;
        }

        /**
         * 判断是否都填写了数量
         */
        public boolean isInputNumber(){
            for (int i = 0; i < boxPrices.size(); i++){
                if (isInputNumber(i) == null || isInputNumber(i).equals("") || Integer.parseInt(isInputNumber(i)) == 0){
                    return false;
                }
            }
            return true;
        }
    }
    private void addCustomer(String customerName){
        //备注保存
        String cContent = inputContentEdit.getText().toString().trim();
        //取出客户数据
        List<Customer> customers = DataSupport.where("cName = ?",customerName).find(Customer.class);
        Customer customer = null;
        //判断是否存在该客户
        if (customers.size() == 0){
            //创建新的客户
            customer = new Customer();
            customer.setcName(customerName);
            customer.setcContent(cContent);
            customer.save();
        }else{
            //获取以前的客户记录
            customer = customers.get(0);
        }
        //工单的数据记录
        Work work = new Work();

        work.setwPrice(boxAdapter.displayAllPrice());   //记录总价
        work.setcName(customerName);                    //客户名
        work.setwTime(new Date());                      //记录时间
        if (work.save()){
            //保存成功 提示
            work.setOrderBoxList(work.getId(),boxAdapter.getInputNumbers(),boxPrices);   //保存纸箱数据
            Toast.makeText(AddCustomerActivity.this,"保存成功！",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddCustomerActivity.this,MainActivity.class);
            startActivity(intent);
        }else{
            //保存失败 提示
            Toast.makeText(AddCustomerActivity.this,"异常错误！请重新进行保存",Toast.LENGTH_SHORT).show();
        }
    }
}
