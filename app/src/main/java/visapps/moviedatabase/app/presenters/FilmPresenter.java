package visapps.moviedatabase.app.presenters;

import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.interfaces.FilmPresenterCallback;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.models.Mark;
import visapps.moviedatabase.app.models.Response;

public class FilmPresenter extends AbstractPresenter<FilmPresenterCallback>{

    private int id;
    private String login;
    private String password;


    public void Init(String login, String password, int id) {
        this.login = login;
        this.password = password;
        this.id = id;
    }



    public void LoadFilm(){
        callback.ShowLoading();
        callback.HidePage();
        callback.DisableButtons();
        disposables.add(ApiService.getInstance().getfilm(login, password, id, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.onLoadFilm((Film) object);
                callback.RemoveLoading();
                callback.ShowPage();
                GetFavoriteStatus();
                GetMarkStatus();
            }

            @Override
            public void onError(RequestError error) {
                callback.RemoveLoading();
                callback.HidePage();
                callback.onError(error);
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                callback.RemoveLoading();
                callback.HidePage();
                callback.onLogOut();
            }
        }));
    }

    public void PlayFilm(){
        callback.ShowLoading();
        disposables.add(ApiService.getInstance().storeview(login, password, id, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.RemoveLoading();
                callback.onLoadVideo();
            }

            @Override
            public void onError(RequestError error) {
                callback.RemoveLoading();
                callback.onError(error);
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                callback.RemoveLoading();
                callback.HidePage();
                callback.onLogOut();
            }
        }));
    }

    private void GetFavoriteStatus(){
        disposables.add(ApiService.getInstance().getfavorite(login, password, id, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                Response response = (Response) object;
                if(response.getCode() == 1){
                    callback.onLoadFavorite(true);
                }
                else{
                    callback.onLoadFavorite(false);
                }
                callback.EnableButtons();
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

    private void GetMarkStatus(){
        disposables.add(ApiService.getInstance().getmark(login, password, id, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                Mark mark = (Mark) object;
                if(mark.getRating() == 0 && mark.getComment() == null){
                    callback.onLoadMark(true);
                }
                else{
                    callback.onLoadMark(false);
                }
                callback.EnableButtons();
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

    public void ChangeFavoriteStatus(){
        callback.DisableButtons();
        callback.ShowLoading();
        disposables.add(ApiService.getInstance().updatefavorite(login, password, id, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.RemoveLoading();
                GetFavoriteStatus();
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
