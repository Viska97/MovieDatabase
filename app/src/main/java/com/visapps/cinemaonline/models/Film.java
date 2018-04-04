package com.visapps.cinemaonline.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Visek on 12.03.2018.
 */

public class Film {
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("directorID")
    @Expose
    private Integer directorID;
    @SerializedName("filmYear")
    @Expose
    private Integer filmYear;
    @SerializedName("filmLength")
    @Expose
    private String filmLength;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("fdescription")
    @Expose
    private String fdescription;
    @SerializedName("sourceLink")
    @Expose
    private String sourceLink;
    @SerializedName("director")
    @Expose
    private String director;
    @SerializedName("ageLimit")
    @Expose
    private Integer ageLimit;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("posterLink")
    @Expose
    private String posterLink;
    @SerializedName("filmId")
    @Expose
    private Integer filmId;
    @SerializedName("filmName")
    @Expose
    private String filmName;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getDirectorID() {
        return directorID;
    }

    public void setDirectorID(Integer directorID) {
        this.directorID = directorID;
    }

    public Integer getFilmYear() {
        return filmYear;
    }

    public void setFilmYear(Integer filmYear) {
        this.filmYear = filmYear;
    }

    public String getFilmLength() {
        return filmLength;
    }

    public void setFilmLength(String filmLength) {
        this.filmLength = filmLength;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getFdescription() {
        return fdescription;
    }

    public void setFdescription(String fdescription) {
        this.fdescription = fdescription;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(Integer ageLimit) {
        this.ageLimit = ageLimit;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }


}
