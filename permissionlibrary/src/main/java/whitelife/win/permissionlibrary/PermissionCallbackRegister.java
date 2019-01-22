package whitelife.win.permissionlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public class PermissionCallbackRegister<A extends Activity> implements Application.ActivityLifecycleCallbacks {

    private Class<A>mClass;

    private PermissionCallback callback;

    private Context mContext;


    public PermissionCallbackRegister(Context context,Class<A> mClass, PermissionCallback callback) {
        this.mClass = mClass;
        this.callback = callback;
        this.mContext=context;
        ((Application)context.getApplicationContext()).registerActivityLifecycleCallbacks(this);
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if(activity.getLocalClassName().equals(mClass.getName())&& activity instanceof CallBackInterface){
            ((CallBackInterface)activity).setCallback(callback);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ((Application)mContext.getApplicationContext()).unregisterActivityLifecycleCallbacks(this);
    }
}
