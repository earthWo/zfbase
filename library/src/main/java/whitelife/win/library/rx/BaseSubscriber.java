package whitelife.win.library.rx;

import android.app.ProgressDialog;
import android.content.Context;

import io.reactivex.subscribers.DisposableSubscriber;
import whitelife.win.library.util.DialogUtils;

/**
 * 请求结果处理类
 * Created by wuzefeng on 2017/9/21.
 */

public abstract class BaseSubscriber<T> extends DisposableSubscriber<T> {


    private boolean isShowLoading;
    private ProgressDialog mProgressDialog;

    public BaseSubscriber(Context context,boolean isShowLoading) {
        this.isShowLoading=isShowLoading;
        if(isShowLoading){
            mProgressDialog= DialogUtils.createLoadingDialog(context);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading();
    }

    @Override
    public void onError(Throwable t) {
        dismissLoading();
        onError(0,t);
    }

    private void showLoading(){
        if(isShowLoading&&mProgressDialog!=null){
            mProgressDialog.show();
        }
    }
    private void dismissLoading(){
        if(!isShowLoading&&mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onComplete() {
        dismissLoading();
    }

    public abstract void onError(int type,Throwable t);
}
