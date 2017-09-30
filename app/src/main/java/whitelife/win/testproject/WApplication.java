package whitelife.win.testproject;

import io.realm.Realm;
import whitelife.win.library.AppConfig;
import whitelife.win.library.base.BaseApplication;
import whitelife.win.library.database.megration.Megration1;
import whitelife.win.library.database.megration.Megration2;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class WApplication extends BaseApplication {


    /**
     * Integer: oldVersion
     * BaseMegration: magration
     */

    static {
        megrationMap.put(1,new Megration1());
        megrationMap.put(2,new Megration2());
    }


    @Override
    protected void initRealm() {
        Realm.init(this);
        Realm.setDefaultConfiguration(addRealmConfiguration(3));
    }


    @Override
    public AppConfig getAppConfig() {
        return new AppConfig.Builder().setLogEnable(true).build();
    }
}
