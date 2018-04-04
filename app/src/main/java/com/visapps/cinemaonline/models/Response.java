package com.visapps.cinemaonline.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Visek on 18.03.2018.
 */

public class Response {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("details")
    @Expose
    private Object details;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

}
