package visapps.moviedatabase.app.interfaces;

import visapps.moviedatabase.app.enums.RequestError;

public interface InfoPresenterCallback extends AbstractPresenterCallback{
    void onLoad(Object object);
    void onEmpty();
    void onLogOut();
    void onError(RequestError error);
    void ShowLoading();
    void RemoveLoading();
}
