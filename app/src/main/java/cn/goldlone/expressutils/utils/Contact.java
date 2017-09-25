package cn.goldlone.expressutils.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

/**
 * Created by CN on 2017/8/21.
 */

public class Contact {
    private String phoneNum = null;
    private Context context = null;

    public Contact(Context context, String phoneNum) {
        this.phoneNum = phoneNum;
        this.context = context;
    }

    /**
     * 拨打电话
     * @return
     */
    public boolean call() {
        if(context==null || phoneNum==null) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);

        return true;
    }

    /**
     * 发送短信
     * @param message
     * @return
     */
    public boolean sendSMS(String message){
        if(phoneNum==null || message.equals("")){
            return false;
        }
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNum, null, text, null, null);
        }

        return true;
    }

}
