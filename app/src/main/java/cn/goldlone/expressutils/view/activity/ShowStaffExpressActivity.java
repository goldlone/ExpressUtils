package cn.goldlone.expressutils.view.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.model.ExpressInfo;
import cn.goldlone.expressutils.model.StationExpress;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.ACache;
import cn.goldlone.expressutils.utils.InfoHideUtils;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/8/29.
 */

public class ShowStaffExpressActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    private Toolbar toolbar;
    private Button pickDate;
    private ListView lvStaffExpress;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> list = null;
    private Calendar calendar;
    private String dateStr;
    private String staffName;
    private String staffPhone;
    private ProgressDialog progressDialog;
    private boolean hadFinal = false;
    private int currentPage;
    private int totalPage;
    private ACache mCache;
    private ArrayList<ExpressInfo> infos;
    private int allSize = 0;
    private SharedPreferences sharedPreferences;
    private String stationNum;
    private StationExpress station;
    private GetStaffExpressThread getStaffExpressThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_staff_express);

        mCache = ACache.get(this);
        staffName = getIntent().getStringExtra("staffName");
        staffPhone = getIntent().getStringExtra("staffPhone");
        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        stationNum = sharedPreferences.getString("stationNum", "");

        initView();
    }

    private void initView() {
        calendar = Calendar.getInstance();
        dateStr = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(staffName+"("+dateStr+")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pickDate = (Button) findViewById(R.id.btn_staff_pick_date);
        pickDate.setOnClickListener(this);
        lvStaffExpress = (ListView) findViewById(R.id.lv_show_staff_express);

        currentPage = 1;

        initProgressDialog();

        infos = new ArrayList<ExpressInfo>();
        list = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(ShowStaffExpressActivity.this, list,
                R.layout.item_station_express,
                new String[] {"expressNum", "status", "payMode", "receiver", "receiver_phone", "address"},
                new int[]{R.id.item_station_express_num, R.id.item_station_express_status, R.id.item_station_pay_mode, R.id.item_station_receiver, R.id.item_station_receiver_phone, R.id.item_station_address});

        lvStaffExpress.setOnItemClickListener(this);
        lvStaffExpress.setOnScrollListener(this);
        lvStaffExpress.setAdapter(simpleAdapter);


        getStaffExpressThread = new GetStaffExpressThread(stationNum);
        getStaffExpressThread.start();
    }

    /**
     * 初始化加载对话框
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(ShowStaffExpressActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("正在加载");
        progressDialog.setMessage("加载更多快递信息中...");
        progressDialog.setCancelable(false);
    }

    /**
     * 选择查看日期
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        new DatePickerDialog(ShowStaffExpressActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateStr = year+"-"+(month+1)+"-"+dayOfMonth;
                toolbar.setTitle(staffName+"("+dateStr+")");
                Log.i("时间选择器: ", dateStr);
                if(getStaffExpressThread!=null) {
                    getStaffExpressThread.interrupt();
                    getStaffExpressThread = null;
                }
                list.removeAll(list);
                getStaffExpressThread = new GetStaffExpressThread(stationNum);
                getStaffExpressThread.start();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_TOUCH_SCROLL:
                hadFinal = false;
                if(currentPage == totalPage) {
                    ShowToast.showDefaultToastMessage(ShowStaffExpressActivity.this, "已加载全部", 3000);
                    break;
                }
                progressDialog.show();
                  // 启动网络线程
                if(getStaffExpressThread!=null) {
                    getStaffExpressThread.interrupt();
                    getStaffExpressThread = null;
                }
                getStaffExpressThread = new GetStaffExpressThread(stationNum);
                currentPage = currentPage + 1;
                getStaffExpressThread.start();
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        hadFinal = false;
        if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            View lastVisibleItemView = lvStaffExpress.getChildAt(lvStaffExpress.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lvStaffExpress.getHeight()) {
                Log.d("ListView", "##### 滚动到底部 ######");
                hadFinal = true;
            }
        } else if(firstVisibleItem == 0) {
            View firstVisibleItemView = lvStaffExpress.getChildAt(0);
            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                Log.d("ListView", "##### 滚动到顶部 #####");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCache.put("expressSelected", ""+id);
        startActivity(new Intent(this, ShowExpressDetailActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }


    /**
     * 此类用来获取站点快递信息
     */
    class GetStaffExpressThread extends  Thread {
        private String stationNum;

        public GetStaffExpressThread(String stationNum) {
            this.stationNum = stationNum;
        }

        @Override
        public void run() {
            station = new ServerApi().getStaffExpress(staffPhone, dateStr);
            if(!station.isSuccess()){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast.showDefaultToastMessage(ShowStaffExpressActivity.this, "今日无派件信息", 1500);
                    }
                });
                return;
            }
            Log.i("快递信息：", station.toString());
            infos.addAll(station.getList());
            for(int i=allSize; i<infos.size(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("expressNum", infos.get(i).getExpressNum());
                switch (infos.get(i).getStatus()) {
                    case 0:
                        map.put("status", "运输中");
                        break;
                    case 1:
                        map.put("status", "正在派送");
                        break;
                    case 2:
                        map.put("status", "已签收");
                        break;
                    default:
                        map.put("status", "状态码异常");
                }
                switch (infos.get(i).getPayMode()) {
                    case 0:
                        map.put("payMode", "寄方付款");
                        break;
                    case 1:
                        map.put("payMode", "货到付款");
                        break;
                    default:
                        map.put("payMode", "付款状态错误");
                }
                map.put("receiver", InfoHideUtils.hideName(infos.get(i).getReceiver()));
                map.put("receiver_phone", InfoHideUtils.hidePhone(infos.get(i).getReceiverNum()));
                map.put("address", infos.get(i).getReceiverAdr());
                list.add(map);
            }
            allSize = list.size();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    simpleAdapter.notifyDataSetChanged();
                    lvStaffExpress.setAdapter(simpleAdapter);
                    if(progressDialog.isShowing()) {
                        ShowToast.showToastMessage(ShowStaffExpressActivity.this, "加载完成");
                        progressDialog.dismiss();
                    }
                    mCache.put("expressList", infos);
                }
            });
        }
    }
}
