package whitelife.win.library.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by wuzefeng on 2017/9/21.
 */

public class DialogUtils {

    private DialogUtils(){}


    /**
     * 创建loading dialog
     * @param context
     * @return
     */
    public static ProgressDialog createLoadingDialog(Context context){
        ProgressDialog mProgressDialog=new ProgressDialog(context);
        return mProgressDialog;
    }

}
