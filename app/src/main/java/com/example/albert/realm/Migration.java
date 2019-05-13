package com.example.albert.realm;

import android.util.Log;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            RealmObjectSchema personSchema = schema.get("Person");
            personSchema
                    .addField("telefon", String.class, FieldAttribute.INDEXED);



            Log.d("Migration", "Actualizando a versión 1");

            oldVersion++;
        }
    }
}
