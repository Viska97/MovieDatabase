package com.visapps.cinemaonline.interfaces;


import com.visapps.cinemaonline.enums.RequestError;

/**
 * Created by Visek on 12.03.2018.
 */

public interface ApiCallback {
    void onSuccess(Object object);
    void onError(RequestError error);
    void onNotAuthorized(RequestError error);
}
