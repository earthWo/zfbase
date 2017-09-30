package whitelife.win.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import whitelife.win.library.lifecycle.LifeCycleCleanHelper;
import whitelife.win.library.lifecycle.LifeCycleCleanListener;
import whitelife.win.library.lifecycle.LifeCycleEvent;
import whitelife.win.library.lifecycle.LifeCycleListener;
import whitelife.win.library.mvp.BasePresent;
import whitelife.win.library.mvp.BaseView;

/**
 * activity基类
 * Created by wuzefeng on 2017/9/20.
 */

public abstract class BaseActivity<P extends BasePresent,V extends BaseView> extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<P>,BaseView,LifeCycleListener,LifeCycleCleanListener{


    protected Context mContext;

    private LifeCycleCleanHelper mLifeCycleCleanHelper;


    protected P mPresent;

    private int mLifeCycleEvent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onLifeCycleChange(LifeCycleEvent.CREATE);
        if(getLayoutResId()!=0){
            setContentView(getLayoutResId());
        }
        mContext=this;
        mLifeCycleCleanHelper=new LifeCycleCleanHelper();
        getSupportLoaderManager().initLoader(11,null,this);
        initViews();
    }


    @Override
    protected void onStart() {
        super.onStart();
        onLifeCycleChange(LifeCycleEvent.START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onLifeCycleChange(LifeCycleEvent.PAUSE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLifeCycleChange(LifeCycleEvent.RESUME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onLifeCycleChange(LifeCycleEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresent!=null)mPresent.detachView();
        onLifeCycleChange(LifeCycleEvent.DESTROY);
    }



    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return createPresentLoader(id,args);
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        mPresent=data;
        mPresent.attachView((V)this);
        fetchData();
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresent=null;
    }


    /**
     * 获取res id
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 加载数据
     */
    protected abstract void fetchData();


    protected abstract Loader<P>createPresentLoader(int id, Bundle args);



    protected abstract void initViews();


    @Override
    public void onLifeCycleChange(@LifeCycleEvent int lifeCycleEvent) {
        mLifeCycleEvent=lifeCycleEvent;
        if(mPresent!=null){
            mPresent.onLifeCycleChange(lifeCycleEvent);
        }
        mLifeCycleCleanHelper.onLifeCycleChange(lifeCycleEvent);
    }

    public int getLifeCycleEvent() {
        return mLifeCycleEvent;
    }


    @Override
    public void recycle(Disposable disposable) {
        mLifeCycleCleanHelper.recycle(disposable);
    }

    @Override
    public void recycleImmediately(Disposable disposable) {
        mLifeCycleCleanHelper.recycleImmediately(disposable);
    }

    @Override
    public <Type> ObservableTransformer<Type, Type> bindEvent(@LifeCycleEvent int lifeCycle) {
        return mLifeCycleCleanHelper.bindEvent(lifeCycle);
    }

    @Override
    public <T> FlowableTransformer<T, T> bindEventWithFlowable(@LifeCycleEvent int lifeCycle) {
        return mLifeCycleCleanHelper.bindEventWithFlowable(lifeCycle);
    }
}
