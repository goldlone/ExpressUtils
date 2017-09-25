package cn.goldlone.expressutils.utils;

import android.content.ContentResolver;
import android.content.Context;

/**
 * 此线程用来循环删除通话和短信记录
 * Created by CN on 2017/8/21.
 */

public class DeleteInfoThread extends Thread {

    private Context onContext = null;
    private String phoneNum = null;
    private ContentResolver resolver = null;

    public DeleteInfoThread(Context onContext, String phoneNum) {
        this.onContext = onContext;
        this.phoneNum = phoneNum;
        if(onContext!=null)
            this.resolver = onContext.getContentResolver();
    }

    @Override
    public void run() {
        DeleteInfo del = new DeleteInfo(resolver, phoneNum);
        if(onContext!=null && phoneNum!=null) {
            for(int time=0; time<=3000; time=time+100) {
                // 删除通话记录
                del.deleteCallLog();
                // 删除短信
//                del.deleteSMS();
                try {
                    if(!this.isInterrupted())
                        Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!this.isInterrupted())
                    this.interrupt();
            }

        }


    }
}
