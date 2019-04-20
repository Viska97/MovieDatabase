package visapps.moviedatabase.app.presenters;

import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.interfaces.InfoPresenterCallback;
import visapps.moviedatabase.app.models.Mark;

import java.util.List;

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
