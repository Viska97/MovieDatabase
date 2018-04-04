package com.visapps.cinemaonline.presenters;

import com.visapps.cinemaonline.interfaces.AbstractPresenterCallback;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Visek on 13.03.2018.
 */

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
