package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import engine.FloatViewManager;

/**
 * 作者：Administrator on 2016/8/13 09:58
 */
public class MyFloatService extends Service {
    @Override
    public void onCreate() {
        FloatViewManager manager = FloatViewManager.getInstance(this);
        manager.showFloatCircleView();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
