package whitelife.win.permissionlibrary;


/**
 * Created by wuzefeng on 2017/9/27.
 */

public interface PermissionCallback {

    /**
     * 权限被授权
     * @param permission
     */
    void granted(String permission);

    /**
     * 权限被拒绝
     * @param permission
     */
    void denied(String permission);


    /**
     * 请求取消
     * @param permission
     */
    void cancel(String permission);

    /**
     * 请求结束
     */
    void finish();


}
