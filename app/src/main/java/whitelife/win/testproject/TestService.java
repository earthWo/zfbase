package whitelife.win.testproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        LocalBinder localBinder=(LocalBinder)intent.getSerializableExtra("binder");
        localBinder.setServce(this);
        return (IBinder) localBinder;
    }




    private TestInterface testInterface=new TestInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void test() throws RemoteException {
            Log.d("test","点击了一下");
        }
    } ;



    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
