package cn.goldlone.expressutils.utils;

import android.content.ContentResolver;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;

/**
 * Created by CN on 2017/8/21.
 */

public class DeleteInfo {

    private ContentResolver resolver = null;
    private String phoneNum = null;

    public DeleteInfo(ContentResolver resolver, String phoneNum) {
        this.resolver = resolver;
        this.phoneNum = phoneNum;
    }

    /**
     * 删除某个电话的所有通话记录
     */
    public void deleteCallLog() {
        if(resolver!=null && phoneNum!=null) {
            int num = resolver.delete(CallLog.Calls.CONTENT_URI, "number=?", new String[]{phoneNum});
            if(num != 0)
                Log.i("删除通话记录：", "共删除" + num + "条");
        }
    }

    /**
     * 删除某个电话的所有短信
     * 现在无法删除
     */
    public void deleteSMS() {
        if(resolver!=null && phoneNum!=null) {
            int num1 = resolver.delete(Telephony.Sms.CONTENT_URI, "_id=?", new String[]{phoneNum});
            int num2 = resolver.delete(Telephony.Sms.CONTENT_URI, "thread_id=?", new String[]{phoneNum});
            if(num1!=0 || num2!=0) {
                Log.i("1、删除短信：", "共删除" + num1 + "条");
                Log.i("2、删除短信：", "共删除" + num2 + "条");
            }
        }
    }

    /**
     * 挂断电话是删除通话记录
     */
    /*
    public void deleteWhenOff(final String phoneNum){
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener listener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_IDLE:  // 无任何状态时
                        Log.d("TAG", "无任何状态时");
                        if(haveDelete)deleteCallLog(phoneNum);
                        haveDelete = false;
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: // 接起电话时
                        Log.d("TAG", "接起电话时");
                        haveDelete = true;
                        break;
                    case TelephonyManager.CALL_STATE_RINGING: // 电话进来时
                        Log.d("TAG", "电话进来时");
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    */

    /**
     * 查询所有的记录
     */
    /*
    private void queryAll() {
        //在Activity当中通过getContentResolver()可以得到当前应用的 ContentResolver实例
        ContentResolver resolver = getContentResolver();
        // CallLog.Calls.CONTENT_URI : 等价于：Uri.parse(“content://call_log/calls”)
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        Log.d("TAG", "call log count:" + cursor.getCount());
        while (cursor.moveToNext()) {
            String callNumber = cursor.getString(cursor.getColumnIndex("number"));
            Log.d("TAG", "call log:" + callNumber);
        }
    }
    */

    /**
     * 查询某个电话的多条通话记录
     */
//    private void queryCallRecord(String phoneNum) {
//        ContentResolver resolver = getContentResolver();
//        //检查权限
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(MainActivity.this, "请确认打开了读取通话记录的权限", Toast.LENGTH_LONG).show();
//
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null, "number=?", new String[]{phoneNum}, null);
//        Log.d("TAG", phoneNum + "共计" + cursor.getCount() + "条通话记录");
//        while (cursor.moveToNext()) {
//            String callNumber = cursor.getString(cursor.getColumnIndex("number"));
//            Log.d("TAG", "call log:" + callNumber);
//        }
//    }


    /**
     * 删除某个电话最近的一次来电
     */
//    private void deleteLastCallLog(String phoneNum) {
//        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, new String[] { "_id" }, "number=?", new String[] { phoneNum }, "_id desc limit 1");
//        if (cursor.moveToFirst()) {
//            int id = cursor.getInt(0);
//            Log.d("TAG", ""+id);
//            int result = resolver.delete(CallLog.Calls.CONTENT_URI, "_id=?", new String[] { id + "" });
//            if (result > 0) {
//                Log.d("TAG", "删除成功:" + phoneNum);
//            } else {
//                Log.d("TAG", "删除失败:" + phoneNum);
//            }
//        }
//    }


}
