package cn.goldlone.expressutils.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.encryptAlgorithm.Base64Utils;
import cn.goldlone.expressutils.encryptAlgorithm.DES;
import cn.goldlone.expressutils.encryptAlgorithm.RSA;
import cn.goldlone.expressutils.model.SecondSecret;
import cn.goldlone.expressutils.model.UpdateFirstSecret;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.Contact;
import cn.goldlone.expressutils.utils.DeleteInfoThread;
import cn.goldlone.expressutils.utils.InfoHideUtils;
import cn.goldlone.expressutils.utils.ShowToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener{

    private DrawerLayout drawerLayout = null;
    private NavigationView navigationView = null;
    private GridView gridView1;
    private GridView gridView2;
    private Button scanBtn;
    private long exitTime = 0;
    private View view;
    private DeleteInfoThread delInfoThread;
    private boolean noBack = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Handler handler;
    private String stationNum;
    private String userPhone;
    private ServerApi api;
    private String firstSecret1;
    private String firstSecret2;
    private int selected;
    private TextView callingName;
    private TextView callingNum;
    private boolean haveShow = false;
    private String decryptInfo;
    private int level;

    private int[] icon1 = {R.mipmap.color_all_express, R.mipmap.color_search_express, R.mipmap.color_update_express_track};
    private String[] iconName1 = {"本站快递", "物流信息", "更新轨迹"};
    private int[] icon2 = {R.mipmap.color_manage_staff, R.mipmap.color_add_staff};
    private String[] iconName2 = {"查看员工", "添加员工"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        api = new ServerApi();
        handler = new Handler();

        // 设置站点编号
//        editor.putString("stationNum", "SXLL00001");
//        editor.putString("firstSecret1", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI2zkoT7VdLkS1XvWEkURLpMHOmYy+cOc95twRRB31/Fi1zObfGz3RzpFs0pjXcII8UMVz0EHhVZbFMz5Xvi9C3HVLKpxxC1yOJKruqEylPvZXoU4DTJPWkU8Wy3HOUOCCx2HsKqx0MCTb2YTLPLrd8TxKz2uz2g1MqlME6SLcVVAgMBAAECgYAT2T+A2SPgcpIK64fWVLx7zAWQwxJsvx2D3qPZDpEXfThUO6Yy4Rrr3cqP2m4yajV4eI4Pwe/k1a3SycYvYE8LlTBq8yzS5gPyAif2P10he/x3v9Qhuj5z1QLVnDiM6XdlgE3Z3eNtIcp7Vks/6n1Ct5YYjYbEX6CwLLFXYjXagQJBAL+L94MbxaVpmdnqDsRQJX9QfSNwRdJ8EO5uLSx+jPAPIvCVyF1BLJ/Jxnn73SiBqf2DmRctxBLVMxZ5FBaCockCQQC9YeGI1i5RNpFVYpMKyOjpuaN5aokquybACc/o0LtWaCqozUugs6A5B+TNmGPOpKTx3gUo2529mWuuWk7MDy0tAkAmwJ+Lw/OhupKo1sS0DO75KgSXReCqa/VU969mABhtfPJ5sCQAUe5ASadBCKa+yjTlGFnBqb2wfbe/RTG+OCGZAkEAubWkPlUZclY6Siqx2p+NlrSN0+BexeJka2iz0q/tJRxcyM8YXGwIsCRHgDzxko/tl6iKErjZhDVQXNb98ijnuQJBALkpYnqMEIZwI/xxIyqK+M4C6yj9dzP/Ol6Qrn4zSga7dFHnlJYwYeXwPqvmRHtuZAgYasAf/Jjim3w5Vs1CKrk=");
//        editor.commit();

        // 获取站点编号
        stationNum = sharedPreferences.getString("stationNum", "");
        userPhone = sharedPreferences.getString("phoneNum", "");
        Log.i("站点编号", stationNum);

        // 每次打开APP时，获取一次第一类解密密钥，查看是否更新
        updateFirstSecret();

        initView();
        initListener();

    }


    /**
     * 初始化组件
     */
    private void initView() {
        gridView1 = (GridView) findViewById(R.id.gridView1);
        gridView2 = (GridView) findViewById(R.id.gridView2);
        scanBtn = (Button) findViewById(R.id.scan_btn);

        view = View.inflate(this, R.layout.window_hide_calling, null);
        callingName = (TextView) view.findViewById(R.id.calling_name);
        callingNum = (TextView) view.findViewById(R.id.calling_num);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        String stationName = sharedPreferences.getString("stationName", getString(R.string.app_name));
        String username = sharedPreferences.getString("username", stationName);
        level = sharedPreferences.getInt("level", 1);
        // 添加标题bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(stationName);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        ((TextView) headerView.findViewById(R.id.station_name)).setText(username);

        switch (level) {
            case 1:
                navigationView.getMenu().removeItem(R.id.drawer_item_manager_express);
                navigationView.getMenu().removeItem(R.id.drawer_item_manager_staff);
                break;
            case 2:
                break;
        }

        ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        for(int i=0; i<icon1.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon1[i]);
            map.put("text", iconName1[i]);
            list1.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, list1, R.layout.item_grid, new String[]{"image", "text"}, new int[]{R.id.image, R.id.text});
        gridView1.setAdapter(simpleAdapter);
        gridView1.setOnItemClickListener(this);

        ArrayList<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        for(int i=0; i<icon2.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon2[i]);
            map.put("text", iconName2[i]);
            list2.add(map);
        }
        SimpleAdapter simpleAdapter2 = new SimpleAdapter(MainActivity.this, list2, R.layout.item_grid, new String[]{"image", "text"}, new int[]{R.id.image, R.id.text});
        gridView2.setAdapter(simpleAdapter2);
        gridView2.setOnItemClickListener(this);
    }

    private void initListener() {
        scanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
//        onActivityResult(RESULT_OK, RESULT_OK, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getCount() == 3){
            switch ((int)id) {
                case 0:
                    // 显示本站点全部快递
                    if(level==1){
                        ShowToast.showToastMessage(MainActivity.this, "无权限");
                        break;
                    }
                    startActivity(new Intent(MainActivity.this, ShowStationExpressActivity.class));
                    break;
                case 1:
                    // 查询某件快递详细信息
                    startActivity(new Intent(MainActivity.this, SearchExpressInfoActivity.class));
                    break;
                case 2:
                    // 更新物流轨迹
                    startActivity(new Intent(MainActivity.this, UpdateTrackActivity.class));
                    break;
            }
        } else {
            switch((int)id) {
                case 0:
                    // 管理员工
                    if(level==1){
                        ShowToast.showToastMessage(MainActivity.this, "无权限");
                        break;
                    }
                    startActivity(new Intent(MainActivity.this, ManageStaffActivity.class));
                    break;
                case 1:
                    // 添加员工
                    if(level==1){
                        ShowToast.showToastMessage(MainActivity.this, "无权限");
                        break;
                    }
                    startActivity(new Intent(MainActivity.this, AddStaffActivity.class));
                    break;
            }
        }
    }

    /**
     * 接收扫码后的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
//            String res0 = "\n内容："+bundle.getString("result");
//            Log.i("二维码信息：", res0);

            String encryptedStr = bundle.getString("result");
            Log.i("二维码信息", encryptedStr);

//            String encryptedStr = "680695000019#####Q410n946cOSTZLonKUlznUiQIe3aXpoiS6rNXEO6zu0ELR7E1d6irjs5QXf8Bm2cgAz3gQ+Tbk1m2/CZrVx83RO85EMT5jWiYgCn0RocOy+8ZvHxmNdQ79Iu+612uJbNU6OV4mjy6a+b1IeNRcagjvFk1SjT3UErZACJVP0kNwQ=#####Gciy9Xmi891PYGvTFLaT5Ph1TSAAMrrgMhyrt4kxNq3xIRSFV33/hATfswpzZZO0";
//            String encryptedStr = "487811000018#####A1ujrBmNIo3ISoUIHOBNFkgOqgip0qhbcNrIiyPOjslbMwJPP82EWU+Sjxo+z+R1BCDx4Nsmqcz8gtwxfyVczus7JKzyM1oRO7AhUMwqL3syLCydkI04knTLxkUHNt8Di2ZpNDTq6MjnPxYmLJ3sqSGm5rNHADbFOhjVw+ntydU=#####adO1lKt4e1LDonDIQsU2/Z4wWYfXEHxdkJg4HXuL8xcA078RJWOKjerhS3DegaY/Zn+Nt9+38tiBeU0W0ZihSLqviVqkTXs781WnhoL7FwJnDzJ4Q889xf3PlcNvNP4LNu8OoYEd1RByR5DrVRr07eF+5AebcoIIs5ptivQF0q8=";
            if(!encryptedStr.contains("#####")) {
                ShowToast.showDefaultToastMessage(MainActivity.this, "请扫描专属二维码", 2000);
                return;
            }
            String[] res = encryptedStr.split("#####");
            String expressNum = res[0];
            String encryptNum = res[1];
            String info = res[2];

            Log.i("快递单号：", expressNum);
            Log.i("加密后信息：", info);

            int decryptNum = 0;
            Log.i("加密后的算法编号", encryptNum);
            try {
                if(firstSecret1 != null) {
                    Log.i("本站点第一类密钥：", firstSecret1);
                    try {
                        decryptNum = Integer.parseInt(RSA.decryptByPrivate(encryptNum, firstSecret1));
                        Log.i("解密后算法编号：", "" + decryptNum);
                    } catch (Exception e){
                        // 如果第一个密钥解密失败，使用先前的解密密钥
                        decryptNum = Integer.parseInt(RSA.decryptByPrivate(encryptNum, firstSecret2));
                        Log.i("解密后算法编号：", "" + decryptNum);
                        e.printStackTrace();
                    }
                }
                else {
                    ShowToast.showDefaultToastMessage(MainActivity.this, "请先获取本站点密钥", 2000);
                    return;
                }

                // 在此处获取第二类解密密钥
                getSecondSecret(expressNum, decryptNum, info);

            } catch (Exception e) {
                e.printStackTrace();
                ShowToast.showToastMessage(MainActivity.this, "送错站了");
            }
        }

    }

    /**
     * 解析第二类解密内容
     */
    private Runnable decryptSecond = new Runnable() {
        @Override
        public void run() {
            try {
                JSONTokener jt = new JSONTokener(decryptInfo);
                JSONObject json = new JSONObject(jt);
                final String receiver = json.getString("receiver");
                final String phone = json.getString("phone");

                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选择通知方式：");
                builder.setSingleChoiceItems(new String[]{"拨打电话", "发送短信"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                selected = 0;
                                break;
                            case 1:
                                selected = 1;
                                break;
                        }
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Contact contact = new Contact(MainActivity.this, phone);
                        switch (selected) {
                            case 0:
                                callingName.setText(InfoHideUtils.hideName(receiver));
                                callingNum.setText(InfoHideUtils.hidePhone(phone));
                                contact.call();
                                listionpho();
                                break;
                            case 1:
                                String message = "【"+getString(R.string.app_name)+"】请在18:00前至"+sharedPreferences.getString("stationName", getString(R.string.app_name))+"领走快递";
                                contact.sendSMS(message);
                                break;
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                dialog = builder.show();
            } catch (JSONException e) {
                e.printStackTrace();
                ShowToast.showDefaultToastMessage(MainActivity.this, "数据解析失败", 2000);
            }
        }
    };

    /**
     * 监听电话状态
     */
    private void listionpho() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener listener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("TAG", "无任何状态时");
                        if (view != null && view.isShown()) {
                            WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
                            windowManager.removeView(view);
                        }
                        noBack = false;
                        delInfoThread = new DeleteInfoThread(MainActivity.this, "18435187057");
                        delInfoThread.start();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d("TAG", "拨出、接起电话时");
                        showView(view);
                        haveShow = true;
                        noBack = true;
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("TAG", "电话进来时");
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 显示悬浮窗
     * @param view2
     */
    protected void showView(View view2) {
        //窗口管理器
        WindowManager manager=(WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 此处需要悬浮窗权限
        params.type = /*WindowManager.LayoutParams.TYPE_SYSTEM_ALERT ;//| */WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        // 做此处理便无需悬浮窗权限
//        params.type = WindowManager.LayoutParams.TYPE_TOAST;

        // 设置行为选项
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity= Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置显示初始位置 屏幕左上角为原点

        // topWindow显示到最顶部
        if(haveShow && view2.isShown())
            manager.removeView(view2);
        manager.addView(view2, params);
    }

    /**
     * 定时更新第一类密钥
     */
    private void updateFirstSecret() {
        Thread upThread = new UpdateSecretThreat();
        upThread.start();
    }

    /**
     * 启动获取第二类密钥
     * @param expressNum
     * @param encryptNum
     */
    private void getSecondSecret(String expressNum, int encryptNum, String info) {
        Thread get = new GetSecondSecret(expressNum, encryptNum, info);
        get.start();
    }


    /**
     * 此线程获取更新第一类密钥
     */
    class UpdateSecretThreat extends Thread {
        @Override
        public void run() {
            if(stationNum != null){
                UpdateFirstSecret key = api.getFirstSecret(stationNum);
                if(sharedPreferences.contains("firstSecret1"))
                    firstSecret1 = sharedPreferences.getString("firstSecret1", null);
                else
                    firstSecret1 = "";
                if(sharedPreferences.contains("firstSecret2"))
                    firstSecret2 = sharedPreferences.getString("firstSecret2", "");
                else
                    firstSecret2 = "";
                if(key != null) {
                    Log.i("第一类密钥请求信息：", key.toString());
                    if(firstSecret1.equals("") || firstSecret1==null){
                        firstSecret1 = key.getPrivateKey();
                        editor.putString("firstSecret1", firstSecret1);
                        editor.putString("firstSecret2", firstSecret1);
                        editor.commit();
                    }
                    if (key.isUpdate()) {
                        editor.putString("firstSecret1", key.getPrivateKey());
                        editor.commit();
                        firstSecret1 = key.getPrivateKey();
                    }
                }
            } else {
                ShowToast.showDefaultToastMessage(MainActivity.this, "重新登录再尝试", 2000);
            }

            if(!interrupted())
                this.interrupt();
        }
    }

    /**
     * 此线程用来获取第二类解密密钥
     */
    class GetSecondSecret extends Thread{
        private String expressNum;
        private int encryptNum;
        private String info;

        public GetSecondSecret(String expressNum, int encryptNum, String info) {
            this.expressNum = expressNum;
            this.encryptNum = encryptNum;
            this.info = info;
        }

        @Override
        public void run() {
            if(expressNum!=null) {
                SecondSecret keys = api.getSecondSecret(expressNum, encryptNum);
                if (keys != null) {
                    try {
                        Log.i("第二类密钥：", keys.toString());
                        switch (encryptNum) {
                            case 0:
                                // DES
                                decryptInfo = new String(DES.decrypt(Base64Utils.decryptBase64(info), keys.getSecondDesKey()), "utf-8");
                                break;
                            case 1:
                                // RSA
                                decryptInfo = RSA.decryptByPrivate(info, keys.getSecondRsaKey());
                                break;
                            case 2:
                                // DES + RSA
                                decryptInfo = RSA.decryptByPrivate(info, keys.getSecondRsaKey());
                                decryptInfo = new String(DES.decrypt(Base64Utils.decryptBase64(decryptInfo), keys.getSecondDesKey()), "utf-8");
                                break;
                        }
                        Log.i("解密后信息：", decryptInfo);

                        api.updateExpressman(userPhone, expressNum);
                        handler.post(decryptSecond);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(decryptFail);
                    }
                } else {
                    handler.post(decryptFail);
                }
                if (!interrupted())
                    this.interrupt();
            }
        }
    }

    /**
     * 解密失败提示
     */
    private Runnable decryptFail = new Runnable() {
        @Override
        public void run() {
            ShowToast.showToastMessage(MainActivity.this, "解密失败", 3000);

        }
    };

    /**
     * 左侧抽屉监听事件
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (id) {
            //  ******* 站点管理员 *******
            case R.id.item_show_all_express:
                // 显示本站点全部快递
                startActivity(new Intent(MainActivity.this, ShowStationExpressActivity.class));
                break;
            case R.id.item_search_express_info:
                // 查询某件快递详细信息
                startActivity(new Intent(MainActivity.this, SearchExpressInfoActivity.class));
                break;
            case R.id.item_update_track:
                startActivity(new Intent(MainActivity.this, UpdateTrackActivity.class));
                break;
            case R.id.item_manage_staff:
                // 管理员工
                startActivity(new Intent(MainActivity.this, ManageStaffActivity.class));
                break;
            case R.id.item_add_staff:
                // 添加员工
                startActivity(new Intent(MainActivity.this, AddStaffActivity.class));
                break;

            //  ******* 配送员 *******
//            case R.id.item_show_gone_express:
//                // 已派快件
//                break;

            //  ******* 设置 *******
            case R.id.item_update_password:
                // 修改密码
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
            case R.id.item_log_off:
                // 注销登录
                editor.putString("password", null);
                editor.putString("firstSecret1", null);
                editor.commit();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        return false;
    }

    /**
     * 监听按键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                // 菜单键--呼出左侧抽屉
                if(!drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.openDrawer(GravityCompat.START);
                break;
            case KeyEvent.KEYCODE_BACK:
                // 返回键--关闭左侧抽屉、退出程序
                if (noBack)
                    return true;
                else if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if((System.currentTimeMillis()-exitTime) <= 2000) {
                        finish();
                        System.exit(0);
                    } else {
                        ShowToast.showDefaultToastMessage(MainActivity.this, "再按一次退出程序", 2000);
                        exitTime = System.currentTimeMillis();
                    }
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                // 音量键--扫码解密
                this.onClick(scanBtn);
                break;
            default:
                break;
        }

        return true;
    }
}
