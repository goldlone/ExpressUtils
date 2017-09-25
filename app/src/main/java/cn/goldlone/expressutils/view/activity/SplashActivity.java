package cn.goldlone.expressutils.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * 打开APP的一个Splash页面
 * Created by CN on 2017/8/26.
 */

public class SplashActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private Handler handler;
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        handler = new Handler();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.post(goMainActivity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 切换到主页面
     */
    private Runnable goMainActivity = new Runnable() {
        @Override
        public void run() {
            finish();
            autoLogin();
        }
    };

    /**
     * 检测能否自动登录
     */
    private void autoLogin() {
        if(!sharedPreferences.contains("password")) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-exitTime) <= 2000) {
                finish();
                System.exit(0);
            } else {
                ShowToast.showDefaultToastMessage(SplashActivity.this, "再按一次退出程序", 2000);
                exitTime = System.currentTimeMillis();
            }
        }

        return true;
    }

}
