package visapps.moviedatabase.app.interfaces;


import visapps.moviedatabase.app.enums.RequestError;

public interface ApiCallback {
    void onSuccess(Object object);
    void onError(RequestError error);
    void onNotAuthorized(RequestError error);
}
