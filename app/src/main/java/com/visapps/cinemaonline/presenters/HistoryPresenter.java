package com.visapps.cinemaonline.presenters;


import com.visapps.cinemaonline.api.ApiService;
import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.interfaces.ApiCallback;
import com.visapps.cinemaonline.interfaces.HistoryPresenterCallback;
import com.visapps.cinemaonline.models.Film;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Visek on 18.03.2018.
 */

public class HistoryPresenter extends AbstractPresenter<HistoryPresenterCallback>{

    private String login, password;

    public void Init(String login, String password){
        this.login = login;
        this.password = password;
    }

    public void GetHistory(){
        callback.ShowLoading();
        disposables.add(ApiService.getInstance().gethistory(login, password, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.RemoveLoading();
                List<Film> result = (List<Film>) object;
                if(result.size() == 0){
                    callback.onEmpty();
                }
                else{
                    callback.onLoadHistory(result);
                }
            }

            @Override
            public void onError(RequestError error) {
                callback.onError(error);
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                callback.onLogOut();
            }
        }));
    }
}
