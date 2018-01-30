package me.xunself.bpstatistics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    //标题栏
    private Toolbar settingToolbar;
    //设置价格板块
    private LinearLayout settingPriceLayout;
    //关于板块
    private LinearLayout aboutAppLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        settingToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(settingToolbar);
        settingPriceLayout = (LinearLayout) findViewById(R.id.setting_price_layout);
        aboutAppLayout = (LinearLayout) findViewById(R.id.about_app_layout);

        settingPriceLayout.setOnClickListener(this);
        aboutAppLayout.setOnClickListener(this);

        settingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.setting_price_layout:
                intent = new Intent(SettingActivity.this,SettingPriceActivity.class);
                startActivity(intent);
                break;
            case R.id.about_app_layout:
                intent = new Intent(SettingActivity.this,AboutAppActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
