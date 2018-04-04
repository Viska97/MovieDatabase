package com.visapps.cinemaonline.models;

/**
 * Created by Visek on 16.03.2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filter {

    @SerializedName("years")
    @Expose
    private List<String> years = null;
    @SerializedName("countries")
    @Expose
    private List<String> countries = null;
    @SerializedName("genres")
    @Expose
    private List<String> genres = null;

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

}
