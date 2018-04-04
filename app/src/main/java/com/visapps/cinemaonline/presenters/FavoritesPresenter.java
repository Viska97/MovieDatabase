package com.visapps.cinemaonline.presenters;

import com.visapps.cinemaonline.api.ApiService;
import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.interfaces.ApiCallback;
import com.visapps.cinemaonline.interfaces.FavoritesPresenterCallback;
import com.visapps.cinemaonline.interfaces.HistoryPresenterCallback;
import com.visapps.cinemaonline.models.Film;

import java.util.List;

/**
 * Created by Visek on 18.03.2018.
 */

public class FavoritesPresenter extends AbstractPresenter<FavoritesPresenterCallback>{

    private String login, password;

    public void Init(String login,String password){
        this.login = login;
        this.password = password;
    }

    public void GetFavorites(){
        callback.ShowLoading();
        disposables.add(ApiService.getInstance().getfavorites(login, password, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.RemoveLoading();
                List<Film> result = (List<Film>) object;
                if(result.size() == 0){
                    callback.onEmpty();
                }
                else{
                    callback.onLoadFavorites(result);
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

    public void RemoveFavorite(int id){
        callback.ShowLoading();
        disposables.add(ApiService.getInstance().updatefavorite(login, password, id, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                GetFavorites();
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
