package me.xunself.bpstatistics;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FrameLayout mainFrameLayout;
    private LinearLayout customerButtonLayout;
    private LinearLayout managementButtonLayout;

    private CustomerFragment customerFragment;
    private ManagementFragment managementFragment;


    /**
     * fragment跳转参数
     */
    private int DISPLAY_CUSTOMER_FRAGMENT = 1;
    private int DISPLAY_MANAGEMENT_FRAGMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        mainFrameLayout = (FrameLayout) findViewById(R.id.main_frameLayout);
        customerButtonLayout = (LinearLayout) findViewById(R.id.customer_layout);
        managementButtonLayout = (LinearLayout) findViewById(R.id.management_layout);
        customerButtonLayout.setOnClickListener(this);
        managementButtonLayout.setOnClickListener(this);
        displayFragment(DISPLAY_CUSTOMER_FRAGMENT);
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

        }else if (Tag == DISPLAY_MANAGEMENT_FRAGMENT){
            /**
             * 显示管理纸箱界面
             */
            fragmentTransaction.show(managementFragment);
            fragmentTransaction.hide(customerFragment);
        }

        fragmentTransaction.commit();
    }

}
