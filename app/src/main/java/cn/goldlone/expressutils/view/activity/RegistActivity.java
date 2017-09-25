package cn.goldlone.expressutils.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.utils.AccountValidatorUtil;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/8/19.
 */

public class RegistActivity extends AppCompatActivity implements View.OnClickListener{

    private Handler handler;
    private EditText phoneNumEt;
    private EditText passwordEt;
    private EditText verEt;
    private Button registBtn;
    private Button getVerBtn;
    private TextView toLogin;
    private String verification;
    private String phoneVer;
    private String time;
    private Thread updateSmsTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_regist);

        handler = new Handler();
        initView();
        initLister();
    }

    private void initView() {
        phoneNumEt = (EditText) findViewById(R.id.phone_num);
        passwordEt = (EditText) findViewById(R.id.password);
        verEt = (EditText) findViewById(R.id.sms_verification);
        registBtn = (Button) findViewById(R.id.btn_regist);
        getVerBtn = (Button) findViewById(R.id.btn_get_sms);
        toLogin = (TextView) findViewById(R.id.toLogin);
    }

    private void initLister() {
        registBtn.setOnClickListener(this);
        getVerBtn.setOnClickListener(this);
        toLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phoneNum = phoneNumEt.getText().toString();
        String password = passwordEt.getText().toString();
        String verNum = verEt.getText().toString();
        switch (v.getId()) {
            case R.id.btn_get_sms:
                // 获取验证码
                if(!AccountValidatorUtil.isMobile(phoneNum)) {
                    ShowToast.showDefaultToastMessage(RegistActivity.this, "请输入正确手机号", 500);
                    return;
                }
                phoneVer = phoneNum;
                updateSmsTime = new varBtnThreat();
                updateSmsTime.start();
                getVerBtn.setClickable(false);
                break;
            case R.id.btn_regist:
                // 注册
                if(verification==null || verification.equals("")) {
                    ShowToast.showDefaultToastMessage(RegistActivity.this, "请先获取验证码", 500);
                    return;
                }
                if(phoneNum==null || phoneNum.equals("")) {
                    ShowToast.showDefaultToastMessage(RegistActivity.this, "手机号不能为空", 500);
                    return;
                }
                if(password==null || password.equals("")) {
                    ShowToast.showDefaultToastMessage(RegistActivity.this, "密码不能为空", 500);
                    return;
                }
                if(verNum==null || verNum.equals("")) {
                    ShowToast.showDefaultToastMessage(RegistActivity.this, "验证码不能为空", 500);
                    return;
                }
                if(verification.equals(verNum)) {
                    if(!phoneVer.equals(phoneNum)) {
                        ShowToast.showDefaultToastMessage(RegistActivity.this, "您已修改手机号,请重新获取验证码", 1000);
                        return;
                    }
                    // 向服务器提交注册信息

                    if(true)
                        ShowToast.showToastMessage(RegistActivity.this, "注册成功");
                    else
                        ShowToast.showToastMessage(RegistActivity.this, "网络故障");
                } else {
                    ShowToast.showToastMessage(RegistActivity.this, "验证码错误");
                    return;
                }

                break;
            case R.id.toLogin:
                finish();
                break;
        }
    }

    /**
     * 获取
     */
    class varBtnThreat extends Thread {
        @Override
        public void run() {
            try {
                // 网络请求获取短信验证码

                handler.post(tipsOk);
                int second = 60;
                while(true) {
                    time = ""+second;
                    handler.post(updateTime);
                    Thread.sleep(1000);
                    second--;
                    if(second == 0) {
                        handler.post(changeBtn);
                        // 关闭线程
                        if(updateSmsTime!=null) {
                            updateSmsTime.interrupt();
                            updateSmsTime = null;
                        }
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            getVerBtn.setText(time+"s");
        }
    };

    private Runnable changeBtn = new Runnable() {
        @Override
        public void run() {
            getVerBtn.setClickable(true);
            getVerBtn.setText("获取验证码");
        }
    };

    private Runnable tipsOk = new Runnable() {
        @Override
        public void run() {
            ShowToast.showToastMessage(RegistActivity.this, "已发送");
            verification = "123123";
        }
    };

    /**
     * 按下返回键，注销此activity
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
            finish();

        return true;
    }
}
