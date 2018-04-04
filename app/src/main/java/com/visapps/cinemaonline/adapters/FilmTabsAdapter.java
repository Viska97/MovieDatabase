package com.visapps.cinemaonline.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.visapps.cinemaonline.fragments.ActorsFragment;
import com.visapps.cinemaonline.fragments.FilmFragment;
import com.visapps.cinemaonline.fragments.ReviewsFragment;
import com.visapps.cinemaonline.interfaces.FilmActivityCallback;

import java.util.List;

/**
 * Created by Visek on 18.03.2018.
 */

public class FilmTabsAdapter extends FragmentStatePagerAdapter {


    private Bundle tabsnames;
    private int FilmID;
    private FilmFragment filmFragment;
    private ActorsFragment actorsFragment;
    private ReviewsFragment reviewsFragment;


    public FilmTabsAdapter(FragmentManager fragmentManager, Bundle tabnames, int FilmID){
        super(fragmentManager);
        this.tabsnames = tabnames;
        this.FilmID = FilmID;
    }



    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return tabsnames.getString("FILM");
            case 1:
                return tabsnames.getString("ACTORS");
            case 2:
                return tabsnames.getString("REVIEWS");
            default:
                return null;

        }
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                FilmFragment filmFragment= new FilmFragment();
                filmFragment.setFilmID(FilmID);
                return filmFragment;
            case 1:
                ActorsFragment actorsFragment = new ActorsFragment();
                actorsFragment.setFilmID(FilmID);
                return actorsFragment;
            case 2:
                ReviewsFragment reviewsFragment = new ReviewsFragment();
                reviewsFragment.setFilmID(FilmID);
                return reviewsFragment;
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return 3;
    }
}
