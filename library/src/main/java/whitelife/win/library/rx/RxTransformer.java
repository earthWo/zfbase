package whitelife.win.library.rx;

import org.reactivestreams.Publisher;

import java.util.Observable;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuzefeng on 2017/9/21.
 */

public class RxTransformer {

    private RxTransformer(){}

    public static class Observable{

        /**
         * thread线程
         */
        public static final ObservableTransformer THREAD=new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread());
            }
        };

        public static final ObservableTransformer THREAD_UI=new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final ObservableTransformer IO=new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(Schedulers.io());
            }
        };


        public static final ObservableTransformer IO_UI=new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final ObservableTransformer COMPUTATION_UI=new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.computation()).unsubscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final ObservableTransformer COMPUTATION_IO=new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull io.reactivex.Observable upstream) {
                return upstream.subscribeOn(Schedulers.computation()).unsubscribeOn(Schedulers.io()).observeOn(Schedulers.io());
            }
        };

    }


    public static class Flowable{

        /**
         * thread线程
         */
        public static final FlowableTransformer THREAD=new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread());
            }

        };

        public static final FlowableTransformer THREAD_UI=new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final FlowableTransformer IO=new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(Schedulers.io());
            }
        };


        public static final FlowableTransformer IO_UI=new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final FlowableTransformer COMPUTATION_UI=new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.computation()).unsubscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };

        public static final FlowableTransformer COMPUTATION_IO=new FlowableTransformer() {
            @Override
            public Publisher apply(@NonNull io.reactivex.Flowable upstream) {
                return upstream.subscribeOn(Schedulers.computation()).unsubscribeOn(Schedulers.io()).observeOn(Schedulers.io());
            }
        };

    }

    public static <T> ObservableTransformer<T,T> applyTransformer(ObservableTransformer transformer){
        return (ObservableTransformer<T,T>)transformer;
    }

    public static <T> FlowableTransformer<T,T> applyTransformer(FlowableTransformer flowableTransformer){
        return (FlowableTransformer<T,T>)flowableTransformer;
    }

}
