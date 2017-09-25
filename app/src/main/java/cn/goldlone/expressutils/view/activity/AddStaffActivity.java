package cn.goldlone.expressutils.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.model.RegistInfo;
import cn.goldlone.expressutils.model.User;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.AccountValidatorUtil;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/8/27.
 */

public class AddStaffActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etAddStaffName;
    private EditText etAddStaffPhone;
    private EditText etAddStaffPassword;
    private EditText etAddStaffRepassword;
    private Button btnAddStaff;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加员工");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etAddStaffName = (EditText) findViewById(R.id.et_add_staff_name);
        etAddStaffPhone = (EditText) findViewById(R.id.et_add_staff_phone);
        etAddStaffPassword = (EditText) findViewById(R.id.et_add_staff_password);
        etAddStaffRepassword = (EditText) findViewById(R.id.et_add_staff_repassword);
        btnAddStaff = (Button) findViewById(R.id.btn_add_staff);
        btnAddStaff.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(etAddStaffName.getText().toString().equals("") ||
                etAddStaffPassword.getText().toString().equals("") ||
                etAddStaffRepassword.getText().toString().equals("") ||
                etAddStaffPhone.getText().toString().equals("")){
            ShowToast.showDefaultToastMessage(this, "请将信息填写完整", 1500);
            return;
        }
        Log.i("密码：", etAddStaffPassword.getText().toString());
        Log.i("确认密码：", etAddStaffRepassword.getText().toString());
        if(!AccountValidatorUtil.isMobile(etAddStaffPhone.getText().toString())) {
            ShowToast.showDefaultToastMessage(this, "请输入正确的手机号", 1500);
            return;
        }
        if(!etAddStaffPassword.getText().toString().equals(etAddStaffRepassword.getText().toString())) {
            ShowToast.showDefaultToastMessage(this, "两次密码输入不一致", 1500);
            return;
        }

        try {
            String str = new String(etAddStaffName.getText().toString().getBytes(), "utf-8");
            Log.i("aaaaa", str);

            User user = new User();
            user.setName(new String(etAddStaffName.getText().toString().getBytes(), "utf-8"));
            user.setPhone(etAddStaffPhone.getText().toString());
            user.setPassword(etAddStaffPassword.getText().toString());
            user.setStationNum(sharedPreferences.getString("stationNum", ""));
            user.setLevel(1);
            AddUserThread add = new AddUserThread(user);
            add.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建用户子线程
     */
    class AddUserThread extends Thread {
        private User user;

        public AddUserThread(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            final RegistInfo info = new ServerApi().addUser(user);
            if(!info.isStatus()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast.showDefaultToastMessage(AddStaffActivity.this, info.getInfo(), 1500);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast.showToastMessage(AddStaffActivity.this, "添加成功", 1500);
                    }
                });
            }
        }
    }

}
