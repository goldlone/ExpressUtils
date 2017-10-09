package cn.goldlone.expressutils.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import cn.goldlone.expressutils.Datas;
import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.model.RegistInfo;
import cn.goldlone.expressutils.model.User;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.HttpUtils;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/7/20.
 */

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private String password = "";
    private EditText scourcePassword = null;
    private EditText newPassword = null;
    private EditText newPassword2 = null;
    private Button updatePasswordBtn = null;
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        handler = new Handler();
        password = sharedPreferences.getString("password", "");
        Log.i("原密码：", password);

        ininView();
    }

    private void ininView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scourcePassword = (EditText) findViewById(R.id.scource_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        newPassword2 = (EditText) findViewById(R.id.new_password2);

        updatePasswordBtn = (Button) findViewById(R.id.update_password_btn);
        updatePasswordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!scourcePassword.getText().toString().equals(password))
            ShowToast.showToastMessage(this, "旧密码错误");
        else if(newPassword.getText().toString().equals(""))
            ShowToast.showToastMessage(this, "新密码不能为空");
        else if(!newPassword.getText().toString().equals(newPassword2.getText().toString()))
            ShowToast.showToastMessage(this, "新密码不一致");
        else {
            // 修改服务器上的密码
            new Thread() {
                @Override
                public void run() {
                    User user = new User();
                    user.setPhone(sharedPreferences.getString("phoneNum", ""));
                    user.setPassword(newPassword.getText().toString());

                    Log.i("TAG", user.toString());

                    final RegistInfo info = new ServerApi().updatePassword(user, password);

                    Log.i("TAG", info.getInfo());

                    if(info.isStatus()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editor.putString("password", newPassword.getText().toString());
                                editor.commit();
                                ShowToast.showToastMessage(UpdatePasswordActivity.this, "修改成功");
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowToast.showToastMessage(UpdatePasswordActivity.this, info.getInfo());
                            }
                        });
                    }
                }
            }.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

}
