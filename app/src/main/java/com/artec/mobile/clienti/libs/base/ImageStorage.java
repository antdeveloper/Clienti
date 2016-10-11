package com.artec.mobile.clienti.libs.base;

import java.io.File;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public interface ImageStorage {
    String getImageUrl(String id);
    void upload(File file, String id, String folder, ImageStorageFinishedListener listener);
    void delete(String keyId, String folder, ImageStorageFinishedListener listener);
}
