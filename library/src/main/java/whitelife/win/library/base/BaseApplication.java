package whitelife.win.library.base;

import android.app.Application;

import whitelife.win.library.AppConfig;
import whitelife.win.library.util.LogUtils;

/**
 * application基类
 * Created by wuzefeng on 2017/9/20.
 */

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void initAppConfig(){
        AppConfig appConfig=getAppConfig();
        AppConfig.initialize(appConfig);
        LogUtils.setIsLogEnable(appConfig.isLogEnable());
    }

    public abstract AppConfig getAppConfig();
}
