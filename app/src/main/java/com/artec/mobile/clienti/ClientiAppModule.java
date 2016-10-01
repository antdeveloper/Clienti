package com.artec.mobile.clienti;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Module
public class ClientiAppModule {
    ClientiApp app;

    public ClientiAppModule(ClientiApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(){
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application){
        return application.getSharedPreferences(app.getSharedPreferencesName(), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return this.app;
    }
}
