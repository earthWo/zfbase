package whitelife.win.testproject;

import android.app.Activity;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class TServiceActivity extends Activity {

    ServiceConnection connection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
