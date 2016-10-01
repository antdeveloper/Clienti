package com.artec.mobile.clienti.domain.di;

import android.app.Activity;
import android.content.Context;
import android.location.Geocoder;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Module
public class DomainModule {
    String firebaseURL;
    Activity activity;

    public DomainModule(String firebaseURL) {
        this.firebaseURL = firebaseURL;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    FirebaseAPI provideFirebaseAPI(DatabaseReference firebase){
        return new FirebaseAPI(firebase, activity);
    }

    @Provides
    @Singleton
    DatabaseReference provideFirebase(String firebaseURL){
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    @Singleton
    String provideFirebaseURL(){
        return this.firebaseURL;
    }

    @Provides
    @Singleton
    Util provideUtil(Geocoder geocoder){
        return new Util(geocoder);
    }

    @Provides
    @Singleton
    Geocoder provideGeocooder(Context context){
        return new Geocoder(context);
    }
}
