package visapps.moviedatabase.app.presenters;

import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.interfaces.HistoryPresenterCallback;
import visapps.moviedatabase.app.models.Film;

import java.util.List;


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
