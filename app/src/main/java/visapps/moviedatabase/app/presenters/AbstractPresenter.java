package visapps.moviedatabase.app.presenters;

import visapps.moviedatabase.app.interfaces.AbstractPresenterCallback;

import io.reactivex.disposables.CompositeDisposable;

public abstract class AbstractPresenter<T extends AbstractPresenterCallback> {


    protected T callback;
    protected CompositeDisposable disposables = new CompositeDisposable();



    public AbstractPresenter(){
        disposables = new CompositeDisposable();
    }

    public void onAttach(T callback){
        this.callback = callback;
    }

    public void onDeattach(){
        callback=null;
        disposables.clear();
    }

    public boolean isAttached(){
        return callback!=null;
    }

}
