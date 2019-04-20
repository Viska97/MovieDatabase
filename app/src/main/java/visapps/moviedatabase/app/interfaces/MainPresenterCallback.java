package visapps.moviedatabase.app.interfaces;

import android.os.Bundle;

import visapps.moviedatabase.app.enums.RequestError;

public interface MainPresenterCallback extends AbstractPresenterCallback{
    void onLogIn(String login, Bundle roles);
    void onLogOut();
    void onError(RequestError error);
    void InitFragments();
    void SetFragmentName(String name);
    void RequestAuth();
    void ShowLoading();
    void RemoveLoading();
}
