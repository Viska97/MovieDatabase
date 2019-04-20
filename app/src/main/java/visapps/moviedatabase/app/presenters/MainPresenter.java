package visapps.moviedatabase.app.presenters;

import android.os.Bundle;

import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.interfaces.MainPresenterCallback;
import visapps.moviedatabase.app.models.Auth;



public class MainPresenter extends AbstractPresenter<MainPresenterCallback>{



    public MainPresenter(){

    }

    private String nickname=null;
    private Bundle userloles=null;
    private String fragmentname;

    public void setFragmentname(String fragmentname) {
        this.fragmentname = fragmentname;
    }

    public void Auth(final String login, String password) {
        if(login == null || password == null) {
            callback.RequestAuth();
        }
        else{
            if(nickname == null){
                callback.ShowLoading();
                disposables.add(ApiService.getInstance().auth(login,password, new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        Auth auth = (Auth) object;
                        nickname = login;
                        userloles = new Bundle();
                        userloles.putBoolean("USER",true);
                        userloles.putBoolean("MODERATOR",false);
                        userloles.putBoolean("ADMIN",false);
                        callback.RemoveLoading();
                        callback.onLogIn(nickname, userloles);
                        callback.InitFragments();
                    }

                    @Override
                    public void onError(RequestError error) {
                        nickname = null;
                        userloles = null;
                        callback.RemoveLoading();
                        callback.onError(error);
                    }

                    @Override
                    public void onNotAuthorized(RequestError error) {
                        nickname = null;
                        userloles = null;
                        callback.RemoveLoading();
                        callback.onLogOut();
                        callback.RequestAuth();
                    }
                }));
            }
            else{
                callback.onLogIn(nickname , userloles);
                callback.SetFragmentName(fragmentname);
            }
        }
    }

}
