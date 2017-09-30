package whitelife.win.testproject;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class LocalBinder extends TestInterface.Stub implements Serializable {


    TestService service;

    public TestService getService(TestService service) {
        return service;
    }


    public void setServce(TestService service){
        this.service=service;
    }


    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public void test() throws RemoteException {
        Log.d("显示","test");
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
