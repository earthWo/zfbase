package whitelife.win.library.mvp;

import whitelife.win.library.lifecycle.LifeCycleEvent;

/**
 * Present基类
 * Created by wuzefeng on 2017/9/20.
 */

public abstract class BasePresent<V extends BaseView> {


    V mBaseView;

    public void attachView(V view){
        if(view!=null){
            mBaseView=view;
        }
    }

    public boolean isAttachView(){
        return mBaseView!=null;
    }

    public void detachView(){
        mBaseView=null;
    }


    public abstract void onLifeCycleChange(@LifeCycleEvent int lifeCycleEvent);

}
