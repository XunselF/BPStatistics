package me.xunself.bpstatistics;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mainToolBar;



    private ImageView customerImage;
    private ImageView managementImage;
    private LinearLayout customerButtonLayout;
    private LinearLayout managementButtonLayout;

    private CustomerFragment customerFragment;
    private ManagementFragment managementFragment;


    /**
     * fragment跳转参数
     */
    private int DISPLAY_CUSTOMER_FRAGMENT = 1;
    private int DISPLAY_MANAGEMENT_FRAGMENT = 2;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);
        //数据库初始化
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        sharedPreferences = getSharedPreferences("BPS_data", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("ifInit",false)){
            initPrizeDatabase();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("ifInit",true);
            editor.commit();
        }
        mainToolBar = (Toolbar) findViewById(R.id.main_toolBar);
        setSupportActionBar(mainToolBar);
        customerImage = (ImageView) findViewById(R.id.customer_iamge);
        managementImage = (ImageView) findViewById(R.id.management_image);
        customerButtonLayout = (LinearLayout) findViewById(R.id.customer_layout);
        managementButtonLayout = (LinearLayout) findViewById(R.id.management_layout);
        customerButtonLayout.setOnClickListener(this);
        managementButtonLayout.setOnClickListener(this);
        displayFragment(DISPLAY_CUSTOMER_FRAGMENT);
    }

    /**
     * 初始化数据库
     */
    private void initPrizeDatabase(){
        String[] prices = {"A","B","C"};
        for (int i = 0; i < prices.length; i++ ){
            Price price = new Price(prices[i]);
            price.save();
        }
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.customer_layout:
                displayFragment(DISPLAY_CUSTOMER_FRAGMENT);
                break;
            case R.id.management_layout:
                displayFragment(DISPLAY_MANAGEMENT_FRAGMENT);
                break;
        }
    }
    /**
     * 显示fragment
     */
    private void displayFragment(int Tag){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (customerFragment == null || managementFragment == null){

            /**
             * 第一次进入页面的初始化
             */

            customerFragment = new CustomerFragment();
            managementFragment = new ManagementFragment();

            fragmentTransaction.add(R.id.main_frameLayout,customerFragment);
            fragmentTransaction.add(R.id.main_frameLayout,managementFragment);
        }

        if (Tag == DISPLAY_CUSTOMER_FRAGMENT){

            /**
             * 显示客户页面
             */
            fragmentTransaction.show(customerFragment);
            fragmentTransaction.hide(managementFragment);
            customerImage.setImageResource(R.drawable.ic_accessibility_amber_600_24dp);
            managementImage.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp);


        }else if (Tag == DISPLAY_MANAGEMENT_FRAGMENT){
            /**
             * 显示管理纸箱界面
             */
            fragmentTransaction.show(managementFragment);
            fragmentTransaction.hide(customerFragment);

            customerImage.setImageResource(R.drawable.ic_accessibility_black_24dp);
            managementImage.setImageResource(R.drawable.ic_account_balance_wallet_amber_600_24dp);
        }

        fragmentTransaction.commit();
    }

}
