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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goldlone.expressutils.R;
import cn.goldlone.expressutils.model.StaffInfo;
import cn.goldlone.expressutils.model.User;
import cn.goldlone.expressutils.server.ServerApi;
import cn.goldlone.expressutils.utils.ACache;
import cn.goldlone.expressutils.utils.ShowToast;

/**
 * Created by CN on 2017/8/26.
 */

public class ManageStaffActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView lvStaff;
    private List<Map<String, String>> list;
    private SimpleAdapter simpleAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_staff);

        sharedPreferences = getSharedPreferences("express", MODE_PRIVATE);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("查看员工");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvStaff = (ListView) findViewById(R.id.lv_show_all_staff);
        list = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(ManageStaffActivity.this, list,
                R.layout.item_staff_info,
                new String[]{"name", "phone"},
                new int[]{R.id.tv_staff_name, R.id.tv_staff_phone});
        getStaffInfo();
        lvStaff.setAdapter(simpleAdapter);
        lvStaff.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ManageStaffActivity.this, ShowStaffExpressActivity.class);
        intent.putExtra("staffName", list.get((int) id).get("name"));
        intent.putExtra("staffPhone", list.get((int) id).get("phone"));
        Log.i("TAG", ""+id);
        startActivity(intent);
    }

    private void getStaffInfo() {
        new Thread() {
            @Override
            public void run() {
                String phoneNum = sharedPreferences.getString("phoneNum", "");
                StaffInfo info = new ServerApi().getStationStaff(sharedPreferences.getString("stationNum", ""), phoneNum);

                if(info.isSuccess()){
                    ArrayList<User> staffs = info.getUsers();
                    for(int i=0; i<staffs.size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", staffs.get(i).getName());
                        map.put("phone", staffs.get(i).getPhone());
                        list.add(map);
                    }
                    staffs = info.getAdmins();
                    for(int i=0; i<staffs.size(); i++) {
                        if(staffs.get(i).getPhone().equals(phoneNum)){
                            Map<String, String> map = new HashMap<>();
                            map.put("name", staffs.get(i).getName());
                            map.put("phone", staffs.get(i).getPhone());
                            list.add(map);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            simpleAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowToast.showToastMessage(ManageStaffActivity.this, "获取失败");
                        }
                    });
                }
            }
        }.start();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
            finish();

        return true;
    }

}
