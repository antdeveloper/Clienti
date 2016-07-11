package com.artec.mobile.clienti.domain.di;

import android.content.Context;
import android.location.Geocoder;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.Util;
import com.firebase.client.Firebase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Module
public class DomainModule {
    String firebaseURL;

    public DomainModule(String firebaseURL) {
        this.firebaseURL = firebaseURL;
    }

    @Provides
    @Singleton
    FirebaseAPI provideFirebaseAPI(Firebase firebase){
        return new FirebaseAPI(firebase);
    }

    @Provides
    @Singleton
    Firebase provideFirebase(String firebaseURL){
        return new Firebase(firebaseURL);
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
