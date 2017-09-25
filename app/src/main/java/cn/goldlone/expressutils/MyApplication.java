package cn.goldlone.expressutils;

import android.app.Application;

import java.util.ArrayList;

import cn.goldlone.expressutils.model.ExpressInfo;

/**
 * Created by CN on 2017/8/19.
 */

public class MyApplication extends Application {
    public ArrayList<ExpressInfo> expressInfoList;// = new ArrayList<>();
    public int selectedExpress;

}
