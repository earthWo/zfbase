package whitelife.win.library.lifecycle;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 事件生命周期处理类
 * Created by wuzefeng on 2017/9/25.
 */

public class LifeCycleCleanHelper implements LifeCycleCleanListener,LifeCycleListener {


    private int mLifeCycle;

    private CompositeDisposable mCompositeDisposable;

    private BehaviorSubject<Integer>mBehaviorSubject=BehaviorSubject.create();


    @Override
    public void recycle(Disposable disposable) {
        if(disposable==null){
            return;
        }
        if(mCompositeDisposable==null){
            mCompositeDisposable=new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void recycleImmediately(Disposable disposable) {
        if(disposable!=null){
            disposable.dispose();
        }
    }



    @Override
    public<T> FlowableTransformer<T,T> bindEventWithFlowable(@LifeCycleEvent final int lifeCycle) {
        final Flowable<Integer> flowable=mBehaviorSubject.toFlowable(BackpressureStrategy.LATEST)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer==lifeCycle;
                    }
                }).take(1);
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.takeUntil(flowable);
            }
        };
    }

    @Override
    public <T> ObservableTransformer<T,T> bindEvent(@LifeCycleEvent final int lifeCycle) {
        final Observable<Integer> observable=mBehaviorSubject.filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
                return integer==lifeCycle;
            }
        }).take(1);
        return new ObservableTransformer<T,T>(){

            @Override
            public ObservableSource apply(@NonNull Observable upstream) {
                return upstream.takeUntil(observable);
            }
        };
    }

    @Override
    public void onLifeCycleChange(@LifeCycleEvent int lifeCycleEvent) {
        mLifeCycle=lifeCycleEvent;
        if(lifeCycleEvent==LifeCycleEvent.DESTROY&&mCompositeDisposable!=null){
            mCompositeDisposable.dispose();
        }
        mBehaviorSubject.onNext(lifeCycleEvent);
    }


}
