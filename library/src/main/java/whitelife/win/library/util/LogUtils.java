package whitelife.win.library.util;

import android.util.Log;

/**
 * Created by wuzefeng on 2017/9/20.
 */

public class LogUtils {


    public static boolean isLogEnable=true;

    private LogUtils(){}

    public static boolean isLogEnable() {
        return isLogEnable;
    }

    public static void setIsLogEnable(boolean isLogEnable) {
        LogUtils.isLogEnable = isLogEnable;
    }


    public static void d(String tag,String msg){
        if(isLogEnable){
            Log.d(tag,msg);
        }
    }



}
