package com.example.albert.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("myrealm.realm")
                .schemaVersion(1)
                .migration(new Migration())
                .build();

        Realm.setDefaultConfiguration(config);
        Realm.getInstance(config);
    }
}
