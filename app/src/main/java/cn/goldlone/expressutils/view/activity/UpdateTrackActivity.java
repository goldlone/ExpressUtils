package cn.goldlone.expressutils.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.zxing.client.android.CaptureActivity;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/9/4.
 */

public class UpdateTrackActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sharedPreferences;
    private Button btnUpdate;
    private String expressNum;
    private String stationNum;
    private String stationName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_track);

        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        stationNum = sharedPreferences.getString("stationNum", "");
        stationName = sharedPreferences.getString("stationName", "");

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("更新物流轨迹");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate = (Button) findViewById(R.id.btn_update_track);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(UpdateTrackActivity.this, CaptureActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanStr = bundle.getString("result");
            Log.i("二维码信息", scanStr);

            if(scanStr.contains("#")) {
                expressNum = scanStr.split("#####")[0];
            }
            else {
                expressNum = scanStr;
            }

            new Thread(){
                @Override
                public void run() {
                    ServerApi api = new ServerApi();
                    final String info = api.updateTrack(expressNum, stationNum, stationName);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowToast.showDefaultToastMessage(UpdateTrackActivity.this, info, 2000);
                        }
                    });
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
