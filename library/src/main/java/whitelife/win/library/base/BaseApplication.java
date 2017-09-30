package whitelife.win.library.base;

import android.app.Application;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import whitelife.win.library.AppConfig;
import whitelife.win.library.database.megration.BaseMegration;
import whitelife.win.library.util.LogUtils;

/**
 * application基类
 * Created by wuzefeng on 2017/9/20.
 */

public abstract class BaseApplication extends Application {

    protected static SortedMap<Integer,BaseMegration> megrationMap=new TreeMap<>();


    @Override
    public void onCreate() {
        super.onCreate();
        initAppConfig();
        initRealm();
    }


    /**
     * 初始化realm
     */
    protected abstract void initRealm();


    protected RealmConfiguration addRealmConfiguration(long newVersion){
        RealmConfiguration configuration=new RealmConfiguration.Builder().schemaVersion(newVersion).migration(new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                SortedMap<Integer,BaseMegration> megrationSortedMap= megrationMap.subMap((int)oldVersion,(int)newVersion);
                executeMigrations(realm,megrationSortedMap.keySet());
            }
        }).build();
       return configuration;
    }


    protected void executeMigrations(DynamicRealm realm, Set<Integer>
            migrationVersions) {
        for (final Integer version : migrationVersions) {
            megrationMap.get(version).migrate(realm);
        }
    }

    private void initAppConfig(){
        AppConfig appConfig=getAppConfig();
        AppConfig.initialize(appConfig);
        LogUtils.setIsLogEnable(appConfig.isLogEnable());
    }

    public abstract AppConfig getAppConfig();
}
