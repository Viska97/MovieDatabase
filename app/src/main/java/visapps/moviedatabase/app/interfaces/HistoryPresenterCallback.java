package visapps.moviedatabase.app.interfaces;

import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.models.Film;

import java.util.List;

public interface HistoryPresenterCallback extends AbstractPresenterCallback{
    void onLoadHistory(List<Film> films);
    void onEmpty();
    void onLogOut();
    void onError(RequestError error);
    void ShowLoading();
    void RemoveLoading();
}
