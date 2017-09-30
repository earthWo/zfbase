package whitelife.win.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import whitelife.win.library.mvp.BasePresent;
import whitelife.win.library.mvp.BaseView;

/**
 * Fragment基类
 * Created by wuzefeng on 2017/9/20.
 */

public abstract class BaseFragment<P extends BasePresent,V extends BaseView> extends Fragment implements BaseView,LoaderManager.LoaderCallbacks<P>{

    private P mPresent;

    private Context mContext;

    private View rootView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            if(getLayoutResId()!=0){
                rootView=inflater.inflate(getLayoutResId(),container,false);
            }else{
                super.onCreateView(inflater,container,savedInstanceState);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(11,null,this);

    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return createPresentLoader();
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        mPresent=data;
        mPresent.attachView(this);
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresent=null;
    }


    protected abstract Loader<P> createPresentLoader();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract int getLayoutResId();
}
