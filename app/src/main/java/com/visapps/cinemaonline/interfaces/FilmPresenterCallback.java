package com.visapps.cinemaonline.interfaces;

import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.models.Film;
import com.visapps.cinemaonline.models.Mark;

/**
 * Created by Visek on 18.03.2018.
 */

public interface FilmPresenterCallback extends AbstractPresenterCallback{
    void onLoadFilm(Film film);
    void onLoadReview(Mark mark);
    void onLoadFavorite(boolean added);
    void onLoadMark(boolean added);
    void onLoadVideo();
    void onError(RequestError error);
    void onLogOut();
    void ShowLoading();
    void RemoveLoading();
    void ShowPage();
    void HidePage();
    void DisableButtons();
    void EnableButtons();
}
