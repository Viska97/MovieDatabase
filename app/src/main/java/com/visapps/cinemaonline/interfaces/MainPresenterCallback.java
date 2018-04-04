package com.visapps.cinemaonline.interfaces;

import android.os.Bundle;

import com.visapps.cinemaonline.enums.RequestError;

/**
 * Created by Visek on 16.03.2018.
 */

public interface MainPresenterCallback extends AbstractPresenterCallback{
    void onLogIn(String login, Bundle roles);
    void onLogOut();
    void onError(RequestError error);
    void InitFragments();
    void SetFragmentName(String name);
    void RequestAuth();
    void ShowLoading();
    void RemoveLoading();
}
