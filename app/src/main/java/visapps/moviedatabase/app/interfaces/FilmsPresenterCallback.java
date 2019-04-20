package visapps.moviedatabase.app.interfaces;

import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.models.Filter;

import java.util.List;

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
