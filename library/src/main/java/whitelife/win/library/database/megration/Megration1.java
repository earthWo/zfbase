package whitelife.win.library.database.megration;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmObjectSchema;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class Megration1 extends BaseMegration {
    @Override
    public void migrate(DynamicRealm realm) {
        RealmObjectSchema schema=realm.getSchema().get("User");
        schema.addField("age",Integer.class, FieldAttribute.REQUIRED);
    }
}
