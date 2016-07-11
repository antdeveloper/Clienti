package com.artec.mobile.clienti.libs.base;

/**
 * Created by ANICOLAS on 16/06/2016.
 */
public interface EventBus {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);
}
