package whitelife.win.library.mvp;

/**
 * Present工厂类
 * Created by wuzefeng on 2017/9/20.
 */

public interface PresentFactory<P extends BasePresent> {


    P create();
}
