package visapps.moviedatabase.app.presenters;

import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.interfaces.FilmsPresenterCallback;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.models.Filter;

import java.util.List;

public class FilmsPresenter extends AbstractPresenter<FilmsPresenterCallback>{

    private List<Film> films = null;
    private Filter filter = null;
    private String name=null,year=null,genre=null,country=null,orderby=null;
    private String login;
    private String password;

    public void setLogin(String login) {
        this.login = login;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void RequestFilms(boolean refresh){
        if(films == null || refresh){
            callback.ShowLoading();
            disposables.add(ApiService.getInstance().getfilms(login, password, name, year, genre, country, orderby, new ApiCallback() {
                @Override
                public void onSuccess(Object object) {
                    films = (List<Film>) object;
                    callback.RemoveLoading();
                    if(films.size() == 0){
                        callback.onEmpty();
                    }
                    else{
                        callback.onLoadFilms(films);
                    }
                }

                @Override
                public void onError(RequestError error) {
                    callback.onError(error);
                    callback.RemoveLoading();
                }

                @Override
                public void onNotAuthorized(RequestError error) {
                    callback.onLogOut();
                    callback.RemoveLoading();
                }
            }));
        }
        else{
            callback.onLoadFilms(films);
        }
    };

    public void RequestFilter(){
        if(filter == null){
            callback.ShowProgress();
            disposables.add(ApiService.getInstance().getfilters(login, password, new ApiCallback() {
                @Override
                public void onSuccess(Object object) {
                    filter = (Filter) object;
                    callback.RemoveProgress();
                    callback.onLoadFilter(filter);
                }

                @Override
                public void onError(RequestError error) {
                    filter = null;
                    callback.RemoveProgress();
                    callback.onError(error);
                }

                @Override
                public void onNotAuthorized(RequestError error) {
                    filter = null;
                    callback.RemoveProgress();
                    callback.onLogOut();
                }
            }));
        }
        else{
            callback.onLoadFilter(filter);
        }
    }

    public void onFilterSelected(String name, String year, String genre, String country, String orderby){
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.country = country;
        this.orderby = orderby;
        RequestFilms(true);
    }



}
