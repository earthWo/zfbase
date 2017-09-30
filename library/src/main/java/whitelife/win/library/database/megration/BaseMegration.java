package whitelife.win.library.database.megration;

import io.realm.DynamicRealm;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public abstract class BaseMegration {

   public abstract void migrate(DynamicRealm realm);
}
