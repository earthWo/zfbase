package whitelife.win.permissionlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wuzefeng on 2017/9/27.
 */

public class PermissionManager{

    private Context context;

    private PermissionCallback callback;

    public static final String INTENT_KEY_PERMISSION="intent_key_permission";

    public static final String INTENT_KEY_CALLBACK="intent_key_callback";


    private PermissionManager(Context context){
        this.context=context;
    }

    public static PermissionManager getInstance(Context context){
        return new PermissionManager(context.getApplicationContext());
    }



    private ArrayList<String> permissionList=new ArrayList<>();


    public PermissionManager addPermission(String permission){
        if(TextUtils.isEmpty(permission)||TextUtils.isEmpty(permission.trim())){
            throw new NullPointerException("权限名字不能为空");
        }else{
            if(!permissionList.contains(permission)){
                permissionList.add(permission);
            }
        }
        return this;
    }


    public void checkPermission(PermissionCallback callback){
        this.callback=callback;
        if(callback!=null){
            this.callback=callback;
            Iterator<String> iterator=permissionList.iterator();
            while(iterator.hasNext()){
                String permission=iterator.next();
                if(permissionAuthorized(permission)){
                    callback.granted(permission);
                    iterator.remove();
                }
            }

            if(permissionList.size()>0){
                Intent intent=new Intent(context,PermissionActivity.class);
                intent.putStringArrayListExtra(INTENT_KEY_PERMISSION,permissionList);
                intent.putExtra(INTENT_KEY_CALLBACK,callback.hashCode());
                PermissionActivity.addCallback(callback.hashCode(),callback);
                context.startActivity(intent);
            }else{
                callback.finish();
            }
        }else{
            throw new NullPointerException("callback不能为空");
        }
    }


    private boolean permissionAuthorized(String permission){
        return ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED;
    }

    public PermissionCallback getCallback() {
        return callback;
    }
}
