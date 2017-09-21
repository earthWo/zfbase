package whitelife.win.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import whitelife.win.library.mvp.BasePresent;
import whitelife.win.library.mvp.BaseView;

/**
 * activity基类
 * Created by wuzefeng on 2017/9/20.
 */

public abstract class BaseActivity<P extends BasePresent,V extends BaseView> extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<P>,BaseView{


    protected Context mContext;


    protected P mPresent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getLayoutResId()!=0){
            setContentView(getLayoutResId());
        }

        mContext=this;

        getSupportLoaderManager().initLoader(11,null,this);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresent!=null)mPresent.detachView();
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
    public abstract int getLayoutResId();

    /**
     * 加载数据
     */
    public abstract void fetchData();


    public abstract Loader<P>createPresentLoader(int id, Bundle args);






}
