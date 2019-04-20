package visapps.moviedatabase.app.interfaces;

import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.models.Mark;

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
