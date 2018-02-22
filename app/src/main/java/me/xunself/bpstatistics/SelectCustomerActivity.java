package me.xunself.bpstatistics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SelectCustomerActivity extends AppCompatActivity {

    private ListView customerListView;

    private Toolbar toolbar;

    private List<String> customerNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer);
        initView();

    }

    private void getCName(){
        List<Customer> customers = DataSupport.findAll(Customer.class);
        customerNames = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++){
            customerNames.add(customers.get(i).getcName());
        }
    }
    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        customerListView = (ListView) findViewById(R.id.customer_listview);
        getCName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,customerNames);
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("return_customer",customerNames.get(i));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        customerListView.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
