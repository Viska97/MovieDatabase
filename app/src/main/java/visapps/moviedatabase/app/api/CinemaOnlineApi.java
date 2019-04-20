package visapps.moviedatabase.app.api;

import visapps.moviedatabase.app.models.Auth;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.models.Filter;
import visapps.moviedatabase.app.models.Mark;
import visapps.moviedatabase.app.models.Response;
import visapps.moviedatabase.app.models.Role;
import visapps.moviedatabase.app.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CinemaOnlineApi {

    @GET("user/auth")
    Single<Auth> auth(@Header("Authorization") String authkey);

    @GET("user/films/getfilms")
    Single<List<Film>> getfilms(@Header("Authorization") String authkey, @Query("name") String name, @Query("year") String year, @Query("genre") String genre, @Query("country") String country, @Query("orderby") String orderby);

    @GET("user/films/getfilters")
    Single<Filter> getfilters(@Header("Authorization") String authkey);

    @GET("user/films/getfilm")
    Single<Film> getfilm(@Header("Authorization") String authkey, @Query("id") int id);

    @GET("user/films/getroles")
    Single<List<Role>> getroles(@Header("Authorization") String authkey, @Query("id") int id);

    @GET("user/films/getmarks")
    Single<List<Mark>> getmarks(@Header("Authorization") String authkey, @Query("id") int id);

    @GET("user/films/getfavorite")
    Single<Response> getfavorite(@Header("Authorization") String authkey, @Query("id") int id);

    @GET("user/films/getmark")
    Single<Mark> getmark(@Header("Authorization") String authkey, @Query("id") int id);

    @GET("user/profile/gethistory")
    Single<List<Film>> gethistory(@Header("Authorization") String authkey);

    @GET("user/profile/getfavorites")
    Single<List<Film>> getfavorites(@Header("Authorization") String authkey);

    @GET("user/profile/getprofile")
    Single<User> getprofile(@Header("Authorization") String authkey);

    @POST("user/films/updatemark")
    @Headers({"Content-Type: application/json"})
    Single<Response> updatemark(@Header("Authorization") String authkey, @Query("id") int id, @Body Mark mark);

    @POST("user/profile/updateprofile")
    @Headers({"Content-Type: application/json"})
    Single<Response> updateprofile(@Header("Authorization") String authkey, @Body User user);

    @POST("user/films/storeview")
    Single<Response> storeview(@Header("Authorization") String authkey, @Query("id") int id);

    @POST("user/films/updatefavorite")
    Single<Response> updatefavorite(@Header("Authorization") String authkey, @Query("id") int id);

    @POST("public/register")
    @Headers({"Content-Type: application/json"})
    Single<Response> register(@Body User user);
}
