package cn.goldlone.expressutils.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.HttpUtils;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/8/26.
 */

public class SearchExpressInfoActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    private TextView tvCompany;
    private TextView tvSuccess;
    private ListView lvTrack;
    private Handler handler;

    private SearchThread search;
    private List<Map<String, String>> list;
    private SimpleAdapter simpleAdapter;
    private String company;
    private String success;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_express_info);
        handler = new Handler();
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("查询快递信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = (SearchView) findViewById(R.id.search_express);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("查询快递单号");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        tvCompany = (TextView) findViewById(R.id.tv_express_company);
        lvTrack = (ListView) findViewById(R.id.lv_express_info);
        tvSuccess = (TextView) findViewById(R.id.tv_express_success);

        list = new ArrayList<>();
    }

    /**
     * 点击提交时触发
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if(!isNumeric(query)) {
            ShowToast.showDefaultToastMessage(SearchExpressInfoActivity.this, "请输入正确的快递单号", 1500);
            return false;
        }
        if(search!=null) {
            search.interrupt();
            search = null;
        }
        search = new SearchThread(query);
        search.start();

        return false;
    }

    /**
     * 输入字符改变时触发
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    class SearchThread extends Thread {
        private String expressNum;

        public SearchThread(String expressNum) {
            this.expressNum = expressNum;
        }

        @Override
        public void run() {
            try {
                list.removeAll(list);
                JSONTokener jt;
                JSONObject res;
                String result = new ServerApi().getTrack(expressNum);
                if(result==null || result.equals("")) {
                    result = HttpUtils.getExpress(expressNum);
                }
                if(result==null || result.equals("")){
                    handler.post(failTips);
                    return;
                }
                Log.i("快递信息1：", result);
                jt = new JSONTokener(result);
                res = new JSONObject(jt);
                if(res.has("status")){
                    if(!res.getBoolean("status")){
                        result = HttpUtils.getExpress(expressNum);
                        Log.i("快递信息2：", result);
                        jt = new JSONTokener(result);
                        res = new JSONObject(jt);
                    }
                }
                if(res.has("data")) {
                    JSONArray arr = res.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("context", arr.getJSONObject(i).getString("context"));
                        map.put("time", arr.getJSONObject(i).getString("time"));
                        list.add(map);
                    }
                    if (res.has("company"))
                        company = res.getString("company");
                    else
                        company = getString(R.string.app_name);

                    if (res.getBoolean("success")) {
                        success = "已签收";
                    } else {
                        success = "正在路上";
                    }
                    handler.post(showTrack);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否为数字,小数点也不可以
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches()){
            return false;
        }

        return true;
    }

    /**
     * 展示查询结果
     */
    private Runnable showTrack = new Runnable() {
        @Override
        public void run() {
            simpleAdapter = new SimpleAdapter(SearchExpressInfoActivity.this, list, R.layout.item_express_track_info, new String[]{"context", "time"}, new int[]{R.id.tv_express_track_context, R.id.tv_express_track_time});
            lvTrack.setAdapter(simpleAdapter);
            setListViewHeightBasedOnChildren(lvTrack);
            tvCompany.setText(company);
            tvSuccess.setText(success);
        }
    };

    private Runnable failTips = new Runnable() {
        @Override
        public void run() {
            ShowToast.showToastMessage(SearchExpressInfoActivity.this, "无查询结果");
        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}
