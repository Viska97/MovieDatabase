package com.visapps.cinemaonline.interfaces;

import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.models.Film;
import com.visapps.cinemaonline.models.Filter;
import com.visapps.cinemaonline.presenters.AbstractPresenter;

import java.util.List;

/**
 * Created by Visek on 16.03.2018.
 */

public interface FilmsPresenterCallback extends AbstractPresenterCallback{
    void onLoadFilms(List<Film> films);
    void onLoadFilter(Filter filter);
    void onEmpty();
    void onLogOut();
    void onError(RequestError error);
    void ShowLoading();
    void RemoveLoading();
    void ShowProgress();
    void RemoveProgress();
}
