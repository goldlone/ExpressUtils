package cn.goldlone.expressutils.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.goldlone.expressutils.R;


/**
 * 展示一些自定义的Toast
 * Created by CN on 2017/6/22.
 */

public class ShowToast {

    /**
     * 弹出一个Toast提示框，100dp*100dp,50% 透明度的文本消息框
     * @param context
     * @param text
     */
    public static void showToastMessage(Context context, String text){
        showToastMessage(context, text, 1000);
    }

    /**
     * 弹出一个Toast提示框，100dp*100dp,50% 透明度的文本消息框
     * 自定义显示时间
     * @param context
     * @param text
     * @param cnt
     */
    public static void showToastMessage(Context context, String text, int cnt) {
        TextView info = new TextView(context);
        info.setText(text);
        info.setTextSize(15);
        info.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        info.setLayoutParams(rlp);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        RelativeLayout rl = new RelativeLayout(context);
        rl.setBackgroundResource(R.drawable.shape_toast_bg);
        rl.addView(info);
        rl.getBackground().setAlpha(100);
        toast.setView(rl);
        toast.setDuration(Toast.LENGTH_SHORT);
        showToastTime(toast, cnt);
        toast.show();
    }

    /**
     * 自定义Toast显示时长，位置为默认位置
     * @param context
     * @param text
     * @param cnt
     */
    public static void showDefaultToastMessage(Context context, String text, int cnt) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        showToastTime(toast, cnt);
    }


    public static void showToastTime(final Toast toast, int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }



}
