package whitelife.win.library.mvp;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * present 生命周期控制类
 * Created by wuzefeng on 2017/9/20.
 */

public class PresentLoader<P extends BasePresent> extends Loader<P> {


    private PresentFactory<P> mPresentFactory;

    private P mPresent;


    /**
     *
     * @param context
     * @param presentFactory
     */
    public PresentLoader(Context context,PresentFactory presentFactory) {
        super(context);
        this.mPresentFactory=presentFactory;
    }

    @Override
    protected void onStartLoading() {
       if(mPresent!=null){
           deliverResult(mPresent);
       }
       forceLoad();
    }

    @Override
    protected void onForceLoad() {
        mPresent=mPresentFactory.create();
        deliverResult(mPresent);
    }

    @Override
    protected void onReset() {
        mPresent=null;//销毁present
    }
}
