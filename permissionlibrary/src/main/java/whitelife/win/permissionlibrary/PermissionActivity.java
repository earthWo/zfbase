package whitelife.win.permissionlibrary;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuzefeng on 2017/9/27.
 */

public class PermissionActivity extends Activity {

    private  PermissionCallback callback;

    private List<String> permissions;

    private static final int REQUEST_INT=100;

    private static Map<Integer,PermissionCallback>callbackMap;
    static {
        callbackMap=new HashMap<>();
    }

    public static void addCallback(int code,PermissionCallback callback){
        callbackMap.put(code,callback);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = callbackMap.get(getIntent().getIntExtra(PermissionManager.INTENT_KEY_CALLBACK,0));
        callbackMap.remove(getIntent().getIntExtra(PermissionManager.INTENT_KEY_CALLBACK,0));
        permissions=getIntent().getStringArrayListExtra(PermissionManager.INTENT_KEY_PERMISSION);
        checkPermission();
    }



    private void checkPermission(){
        if(permissions!=null&&permissions.size()>0) {
            String[] ps = new String[permissions.size()];
            for (int i = 0; i < permissions.size(); i++) {
                ps[i] = permissions.get(i);
            }
            ActivityCompat.requestPermissions(this, ps, REQUEST_INT);
        }else{
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==REQUEST_INT){
            if(grantResults!=null&&permissions!=null){
                for(int i=0;i<permissions.length;i++){
                    if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                        callbackGranted(permissions[i]);
                    }else if(grantResults[i]== PackageManager.PERMISSION_DENIED){
                        callbackDenied(permissions[i]);
                    }else{
                        callbackCancel(permissions[i]);
                    }
                }
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callbackFinish();
    }


    private void callbackFinish(){
        if(callback!=null){
            callback.finish();
        }
    }

    private void callbackGranted(String permission){
        if(callback!=null){
            callback.granted(permission);
        }
    }

    private void callbackDenied(String permission){
        if(callback!=null){
            callback.denied(permission);
        }
    }

    private void callbackCancel(String permission){
        if(callback!=null){
            callback.cancel(permission);
        }
    }
}
