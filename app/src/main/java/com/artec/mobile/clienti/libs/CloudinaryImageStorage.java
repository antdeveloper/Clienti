package com.artec.mobile.clienti.libs;

import android.os.AsyncTask;

import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.artec.mobile.clienti.libs.base.ImageStorageFinishedListener;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class CloudinaryImageStorage implements ImageStorage {
    private Cloudinary cloudinary;

    public CloudinaryImageStorage(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String getImageUrl(String id) {
        return cloudinary.url().generate(id);
    }

    @Override
    public void upload(final File file, final String id, final String folder,
                       final ImageStorageFinishedListener listener) {
        new AsyncTask<Void, Void, Void>(){
            boolean success = false;
            @Override
            protected Void doInBackground(Void... voids) {
                Map params = ObjectUtils.asMap("public_id", folder+"/"+id);
                try {
                    cloudinary.uploader().upload(file, params);
                    success = true;
                }catch (IOException e){
                    listener.onError(e.getLocalizedMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (success){
                    listener.onSuccess();
                }
            }
        }.execute();
    }

    @Override
    public void delete(final String keyId, final String folder, final ImageStorageFinishedListener listener) {
        new AsyncTask<Void, Void, Void>(){
            boolean success = false;
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    cloudinary.uploader().destroy(folder+"/"+keyId, ObjectUtils.emptyMap());
                    success = true;
                }catch (IOException e){
                    listener.onError(e.getLocalizedMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (success){
                    listener.onSuccess();
                }
            }
        }.execute();
    }
}
