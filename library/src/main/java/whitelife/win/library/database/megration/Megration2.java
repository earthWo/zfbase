package whitelife.win.library.database.megration;

import io.realm.DynamicRealm;
import io.realm.RealmObjectSchema;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class Megration2 extends BaseMegration {
    @Override
    public void migrate(DynamicRealm realm) {
        RealmObjectSchema schema=realm.getSchema().get("User");
        schema.addField("sex",String.class);
    }
}
