package com.visapps.cinemaonline.presenters;


import com.visapps.cinemaonline.api.ApiService;
import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.interfaces.ApiCallback;
import com.visapps.cinemaonline.interfaces.InfoPresenterCallback;
import com.visapps.cinemaonline.models.Mark;
import com.visapps.cinemaonline.models.Role;

import java.util.List;

/**
 * Created by Visek on 18.03.2018.
 */

public class ReviewsPresenter extends AbstractPresenter<InfoPresenterCallback>{

    private String login, password;
    private int id;

    public void Init(String login, String password, int id){
        this.login = login;
        this.password = password;
        this.id = id;
    }

    public void GetReviews(){
        callback.ShowLoading();
        disposables.add(ApiService.getInstance().getmarks(login, password, id, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.RemoveLoading();
                List<Mark> result = (List<Mark>) object;
                if(result.size() == 0){
                    callback.onEmpty();
                }
                else{
                    callback.onLoad(result);
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
