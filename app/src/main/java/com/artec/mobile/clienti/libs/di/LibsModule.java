package com.artec.mobile.clienti.libs.di;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.artec.mobile.clienti.libs.CloudinaryImageStorage;
import com.artec.mobile.clienti.libs.GlideImageLoader;
import com.artec.mobile.clienti.libs.GreenRobotEventBus;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageLoader;
import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 16/06/2016.
 */
@Module
public class LibsModule {
    /*private Fragment fragment;

    public LibsModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager){
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesRequestManager(Fragment fragment){
        return Glide.with(fragment);
    }

    @Provides
    @Singleton
    Fragment provideFragment(){
        return this.fragment;
    }

    @Provides
    @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus){
        return new GreenRobotEventBus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus(){
        return org.greenrobot.eventbus.EventBus.getDefault();
    }

    @Provides
    @Singleton
    ImageStorage providesImageStorage(Cloudinary cloudinary){
        return new CloudinaryImageStorage(cloudinary);
    }

    @Provides
    @Singleton
    Cloudinary provideCloudinary(Context context){
        return new Cloudinary(Utils.cloudinaryUrlFromContext(context));
    }*/
    private Activity activity;
    private DialogFragment dialogFragment;
    private Fragment fragment;

    public LibsModule() {
    }

    public LibsModule(Activity activity) {
        this.activity = activity;
    }

    public LibsModule(Fragment fragment) {
        this.fragment = fragment;
    }

    /*public LibsModule(DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }*/

    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager){
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesRequestManager(Activity activity){
        return Glide.with(activity);
    }

    @Provides
    @Singleton
    Activity provideActivity(){
        return this.activity;
    }

    @Provides @Singleton
    Fragment provideFragment(){
        return this.fragment;
    }

    /*@Provides
    @Singleton
    DialogFragment providesDialogFragment(){
        return this.dialogFragment;
    }*/

    @Provides
    @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus){
        return new GreenRobotEventBus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus(){
        return org.greenrobot.eventbus.EventBus.getDefault();
    }

    @Provides
    @Singleton
    ImageStorage providesImageStorage(Cloudinary cloudinary){
        return new CloudinaryImageStorage(cloudinary);
    }

    @Provides
    @Singleton
    Cloudinary provideCloudinary(Context context){
        return new Cloudinary(Utils.cloudinaryUrlFromContext(context));
    }
}
