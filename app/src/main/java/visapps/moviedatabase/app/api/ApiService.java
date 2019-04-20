package visapps.moviedatabase.app.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.models.Auth;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.models.Filter;
import visapps.moviedatabase.app.models.Mark;
import visapps.moviedatabase.app.models.Response;
import visapps.moviedatabase.app.models.Role;
import visapps.moviedatabase.app.models.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class ApiService {

    private static final String TAG = "ApiService";

    private static ApiService instance;
    private CinemaOnlineApi CinemaApi;

    private ApiService(Retrofit retrofit){
        CinemaApi = retrofit.create(CinemaOnlineApi.class);
    }

    public static void initInstance(Retrofit retrofit){
        if(instance == null){
            instance = new ApiService(retrofit);
        }
    }

    public static ApiService getInstance(){
        return instance;
    }

    public String getErrorMessage(Context context, RequestError error){
        switch(error){
            case BadCredentials:
                return context.getString(R.string.badcredentialserror);
            case UserBlocked:
                return context.getString(R.string.userblocked);
            case InternalServerError:
                return context.getString(R.string.internalservererror);
            case NetworkError:
                return context.getString(R.string.networkerror);
            case UnknownError:
                return context.getString(R.string.unknownerror);
            default:
                return context.getString(R.string.unknownerror);
        }
    }

    public void ReLogin(Context context){

    }

    public void LogOut(Context context){
        SharedPreferences account = context.getSharedPreferences("Account", MODE_PRIVATE);
        SharedPreferences.Editor editor = account.edit();
        editor.putString("login", null);
        editor.putString("password", null);
        editor.apply();
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage( context.getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    public Disposable auth(String login, String password, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.auth(authkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Auth>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Auth auth) {
                        callback.onSuccess(auth);
                    }
                    @Override public void onError(Throwable t) {
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getfilms(String login, String password, String name, String year, String genre, String country, String orderby, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getfilms(authkey, name, year, genre,country,orderby)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Film>>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(List<Film> films) {
                        callback.onSuccess(films);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getfilters(String login, String password, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getfilters(authkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Filter>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Filter filter) {
                        callback.onSuccess(filter);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getfilm(String login, String password, int id, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getfilm(authkey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Film>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Film film) {
                        callback.onSuccess(film);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable storeview(String login, String password, int id, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.storeview(authkey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Response response) {
                        callback.onSuccess(response);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getfavorite(String login, String password, int id, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getfavorite(authkey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Response response) {
                        callback.onSuccess(response);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable updatefavorite(String login, String password, int id, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.updatefavorite(authkey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Response response) {
                        callback.onSuccess(response);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable gethistory(String login, String password, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.gethistory(authkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Film>>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(List<Film> films) {
                        callback.onSuccess(films);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getfavorites(String login, String password, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getfavorites(authkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Film>>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(List<Film> films) {
                        callback.onSuccess(films);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getroles(String login, String password, int id, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getroles(authkey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Role>>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(List<Role> roles) {
                        callback.onSuccess(roles);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getmarks(String login, String password, int id, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getmarks(authkey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Mark>>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(List<Mark> marks) {
                        callback.onSuccess(marks);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getmark(String login, String password, int id, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getmark(authkey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Mark>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Mark mark) {
                        callback.onSuccess(mark);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable register(User user, final ApiCallback callback){
        return CinemaApi.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Response response) {
                        callback.onSuccess(response);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable getprofile(String login, String password, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.getprofile(authkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(User user) {
                        callback.onSuccess(user);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable updateprofile(String login, String password, User user, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.updateprofile(authkey, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Response response) {
                        callback.onSuccess(response);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }

    public Disposable updatemark(String login, String password, int id, Mark mark, final ApiCallback callback){
        String authkey = getAuthKey(login,password);
        return CinemaApi.updatemark(authkey, id, mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override public void onStart() {

                    }
                    @Override public void onSuccess(Response response) {
                        callback.onSuccess(response);
                    }
                    @Override public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                        handleError(callback, t);
                    }
                });
    }


    private String getAuthKey(String login, String password) {
        byte[] data = new byte[0];
        try {
            data = (login + ":" + password).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "Basic " + "";
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }

    private void handleError(ApiCallback callback, Throwable t){
        if (t instanceof HttpException) {
            int code = ((HttpException) t).code();
            switch (code){
                case 401:
                    callback.onNotAuthorized(RequestError.BadCredentials);
                    break;
                case 403:
                    callback.onNotAuthorized(RequestError.UserBlocked);
                    break;
                case 500:
                    callback.onError(RequestError.InternalServerError);
                    break;
                default:
                    callback.onError(RequestError.UnknownError);
                    break;
            }
        }
        else if(t instanceof IOException){
            callback.onError(RequestError.NetworkError);
        }
        else{
            callback.onError(RequestError.UnknownError);
        }
    }


}
