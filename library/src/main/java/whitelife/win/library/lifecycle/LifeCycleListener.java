package whitelife.win.library.lifecycle;

/**
 * 生命周期变化接口
 * Created by wuzefeng on 2017/9/25.
 */

public interface LifeCycleListener {

    void onLifeCycleChange(@LifeCycleEvent int lifeCycleEvent);

}
