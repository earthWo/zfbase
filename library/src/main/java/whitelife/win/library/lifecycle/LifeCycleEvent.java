package whitelife.win.library.lifecycle;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 生命周期事件
 * Created by wuzefeng on 2017/9/25.
 */

@IntDef({LifeCycleEvent.CREATE,LifeCycleEvent.START,LifeCycleEvent.RESUME,
        LifeCycleEvent.PAUSE,LifeCycleEvent.STOP,LifeCycleEvent.DESTROY})
@Retention(RetentionPolicy.SOURCE)
public @interface LifeCycleEvent {

    int CREATE=0x00;
    int START=0x01;
    int RESUME=0x02;
    int PAUSE=0x03;
    int STOP=0x04;
    int DESTROY=0x05;



}
