package cn.wildfirechat.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.apkfuns.logutils.LogUtils;

public class RecoverReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // do nothing
        LogUtils.e("进程管理"+ "main process crashed, to restart");
    }
}
