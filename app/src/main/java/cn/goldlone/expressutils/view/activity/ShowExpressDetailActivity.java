package cn.goldlone.expressutils.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.model.ExpressInfo;
import cn.goldlone.expressutils.utils.ACache;

/**
 * Created by CN on 2017/8/29.
 */

public class ShowExpressDetailActivity extends AppCompatActivity {

    private ExpressInfo details;
    private TextView expressNum;
    private TextView time;
    private TextView receiver;
    private TextView receiverPhone;
    private TextView receiverAddr;
    private TextView sender;
    private TextView senderPhone;
    private TextView senderAddr;
    private TextView startNum;
    private TextView endNum;
    private TextView isFile;
    private TextView isGood;
    private TextView goodNum;
    private TextView goodWeight;
    private TextView isInsured;
    private TextView insuredFee;
    private TextView insuredValue;
    private TextView payMode;
    private TextView totalFee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_express_detail);

        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("快递详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expressNum = (TextView) findViewById(R.id.detail_express_num);
        time = (TextView) findViewById(R.id.detail_time);
        receiver = (TextView) findViewById(R.id.detail_receiver);
        receiverPhone = (TextView) findViewById(R.id.detail_receiver_phone);
        receiverAddr = (TextView) findViewById(R.id.detail_receiver_addr);
        sender = (TextView) findViewById(R.id.detail_sender);
        senderPhone = (TextView) findViewById(R.id.detail_sender_phone);
        senderAddr = (TextView) findViewById(R.id.detail_sender_addr);
        startNum = (TextView) findViewById(R.id.detail_start_num);
        endNum = (TextView) findViewById(R.id.detail_end_num);
        isFile = (TextView) findViewById(R.id.detail_is_file);
        isGood = (TextView) findViewById(R.id.detail_is_good);
        goodNum = (TextView) findViewById(R.id.detail_good_num);
        goodWeight = (TextView) findViewById(R.id.detail_good_weight);
        isInsured = (TextView) findViewById(R.id.detail_is_insured);
        insuredFee = (TextView) findViewById(R.id.detail_insured_fee);
        insuredValue = (TextView) findViewById(R.id.detail_insured_value);
        payMode = (TextView) findViewById(R.id.detail_pay_mode);
        totalFee = (TextView) findViewById(R.id.detail_total_fee);
    }

    private void initData() {
        ACache mCache = ACache.get(this);
        Log.i("选择的item", mCache.getAsString("expressSelected"));
        Log.i("选择的item快递单号", ((ArrayList<ExpressInfo>)mCache.getAsObject("expressList")).get(Integer.parseInt(mCache.getAsString("expressSelected"))).getExpressNum());

        details = ((ArrayList<ExpressInfo>)mCache.getAsObject("expressList")).get(Integer.parseInt(mCache.getAsString("expressSelected")));

        expressNum.setText(details.getExpressNum());
        time.setText(details.getGenTime());
        receiver.setText(details.getReceiver());
        receiverPhone.setText(details.getReceiverNum());
        receiverAddr.setText(details.getReceiverAdr());
        sender.setText(details.getSender());
        senderPhone.setText(details.getSenderNum());
        senderAddr.setText(details.getSenderAdr());
        startNum.setText(details.getStartingNum());
        endNum.setText(details.getTerminalNum());
        isFile.setText(""+details.getIsFile());
        isGood.setText(""+details.getIsGoods());
        goodNum.setText(""+details.getGoodsNums());
        goodWeight.setText(""+details.getGoodsWeight());
        isInsured.setText(""+details.getIsInsured());
        insuredFee.setText(""+details.getInsuredFee());
        insuredValue.setText(""+details.getInsuredValue());
        payMode.setText(""+details.getPayMode());
        totalFee.setText(""+details.getTotalFee());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}
