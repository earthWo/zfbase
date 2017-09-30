package whitelife.win.library.lifecycle;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;

/**
 * 事件生命周期绑定清除接口
 * Created by wuzefeng on 2017/9/25.
 */

public interface LifeCycleCleanListener {

    /**
     * 将事件清除(在页面销毁时清除)
     * @param disposable
     */
    void recycle(Disposable disposable);


    /**
     * 将事件立即清除
     * @param disposable
     */
    void recycleImmediately(Disposable disposable);


    /**
     * Observable绑定生命周期
     * @param lifeCycle
     * @param <Type>
     * @return
     */
    <Type> ObservableTransformer<Type,Type> bindEvent (@LifeCycleEvent final int lifeCycle);


    /**
     * Flowable绑定生命周期
     * @param lifeCycle
     * @param <T>
     * @return
     */
    <T>FlowableTransformer<T,T>bindEventWithFlowable(@LifeCycleEvent int lifeCycle);



}
