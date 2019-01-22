package whitelife.win.library.base;

import whitelife.win.library.bean.Response;

/**
 * 每一个模块，如果有需要在服务端主动传输数据后，更新本地数据的，需要实现该接口，
 * 同时主程序需要将所有的该接口传入到AutoUpdateDataHelper
 * Created by wuzefeng on 2017/10/10.
 */

public interface BaseAutoDataUpdate {


    /**
     * 获取更新接口的code
     * @return
     */
    int[] getCodes();


    /**
     * 更新接口的实现
     * @param code
     * @param response
     */
    void autoUpdateData(int code, Response response);


}
