package cn.goldlone.expressutils.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.model.User;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/8/19.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText phoneNumEt;
    private EditText passwordEt;
    private Button loginBtn;
    private TextView toForget;
    private TextView toRegist;
    private long exitTime = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Thread loginThread;
    private String phoneNum;
    private String password;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        handler = new Handler();

        initView();
        initListener();
    }

    private void initView() {
        phoneNumEt = (EditText) findViewById(R.id.phone_num);
        passwordEt = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        toForget = (TextView) findViewById(R.id.btn_to_forget);
        toRegist = (TextView) findViewById(R.id.btn_to_regist);
    }

    private void initListener() {
        loginBtn.setOnClickListener(this);
        toForget.setOnClickListener(this);
        toRegist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                phoneNum = phoneNumEt.getText().toString();
                password = passwordEt.getText().toString();
                if(phoneNum==null || phoneNum.equals("")) {
                    ShowToast.showDefaultToastMessage(LoginActivity.this, "请输入手机号", 1000);
                    break;
                }
                if(password==null || password.equals("")) {
                    ShowToast.showDefaultToastMessage(LoginActivity.this, "请输入密码", 1000);
                    break;
                }
                loginThread = new LoginThread();
                loginThread.start();
                break;
            case R.id.btn_to_forget:
                ShowToast.showDefaultToastMessage(LoginActivity.this, "暂不支持找回密码", 800);
                break;
            case R.id.btn_to_regist:
                ShowToast.showDefaultToastMessage(LoginActivity.this, "不支持自主注册\n联系站点管理员添加", 3000);
//                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
//                startActivity(intent);
                break;
        }
    }

    /**
     * 登录线程
     */
    class LoginThread extends Thread {
        @Override
        public void run() {
            // 用户登录操作
            ServerApi api = new ServerApi();
            User user = api.login(phoneNum, password);
            Log.i("登录信息: ", user.toString());
            if(user.isSuccess()) {
                editor.putString("stationNum", user.getStationNum());
                editor.putString("phoneNum", phoneNum);
                editor.putString("password", password);
                editor.putString("stationName", user.getStationName());
                editor.putInt("level", user.getLevel());
                editor.putString("username", user.getName());
                editor.commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                handler.post(passwdWrong);
            }
            // 关闭线程
            if(loginThread != null){
                loginThread.interrupt();
                loginThread = null;
            }
        }
    }

    private Runnable passwdWrong = new Runnable() {
        @Override
        public void run() {
            ShowToast.showToastMessage(LoginActivity.this, "登录失败");
            passwordEt.setText("");
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-exitTime) <= 2000) {
                finish();
                System.exit(0);
            } else {
                ShowToast.showDefaultToastMessage(LoginActivity.this, "再按一次退出程序", 2000);
                exitTime = System.currentTimeMillis();
            }
        }

        return true;
    }
}
