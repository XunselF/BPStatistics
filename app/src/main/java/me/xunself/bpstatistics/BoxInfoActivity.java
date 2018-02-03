package me.xunself.bpstatistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.List;

public class BoxInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView textBContent;

    private TextView textBTime;



    private Box mBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_info);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aboutbox_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_update:
                showAlertDialog();
                break;
            case R.id.action_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("是否删除该纸箱信息？");
                dialog.setTitle("删除");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataSupport.delete(Box.class,mBox.getId());
                        DataSupport.deleteAll(BoxPrice.class,"bName = ?",mBox.getbName());
                        Toast.makeText(BoxInfoActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                dialog.setNegativeButton("取消",null);
                dialog.show();
                break;
        }
        return true;
    }
    private void showAlertDialog(){
        View view = (LinearLayout) getLayoutInflater().inflate(R.layout.updatebox_dialog,null);
        final EditText inputBName = (EditText) view.findViewById(R.id.input_boxName);
        final EditText inputBContent = (EditText) view.findViewById(R.id.input_boxContent);

        inputBName.setText(mBox.getbName());
        inputBContent.setText(mBox.getbContent());
        inputBName.setSelection(mBox.getbName().length());
        inputBContent.setSelection(mBox.getbContent().length());

        new AlertDialog.Builder(this).setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        List<Box> boxList = DataSupport.findAll(Box.class);
                        String bName = inputBName.getText().toString().trim();
                        String bContent = inputBContent.getText().toString().trim();
                        if (bName.equals("")){
                            ManagementFragment.noCloseDialog(dialogInterface);
                            Toast.makeText(BoxInfoActivity.this,"请填写纸箱型号！",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (Box box : boxList){
                            if (box.getbName().equals(bName) && !box.getbName().equals(mBox.getbName())){
                                ManagementFragment.noCloseDialog(dialogInterface);
                                Toast.makeText(BoxInfoActivity.this,"您填写的纸箱已存在！",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Box updateBox = new Box();
                        updateBox.setbName(bName);
                        updateBox.setbContent(bContent);
                        updateBox.updateAll("bName = ?",mBox.getbName());

                        BoxPrice boxPrice = new BoxPrice();
                        boxPrice.setbName(bName);

                        boxPrice.updateAll("bName = ?",mBox.getbName());
                        Toast.makeText(BoxInfoActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        ManagementFragment.closeDialog(dialogInterface);

                        toolbar.setTitle(bName);
                        if (bContent.equals("")){
                            textBContent.setText("为空");
                        }else{
                            textBContent.setText(bContent);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ManagementFragment.noCloseDialog(dialogInterface);
            }
        }).show();
    }

    /**
     * 初始化
     */
    private void initView(){
        mBox = (Box)getIntent().getSerializableExtra("extra_box");
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        textBContent = (TextView) findViewById(R.id.box_content);
        textBTime = (TextView) findViewById(R.id.box_createtime);
        setSupportActionBar(toolbar);
        toolbar.setTitle(mBox.getbName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        if (mBox.getbContent().equals("")){
            textBContent.setText("为空");
        }else{
            textBContent.setText(mBox.getbContent());
        }

        textBTime.setText(sdf.format(mBox.getbTime()));
    }
}
