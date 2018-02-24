package com.xplor.android.models;

import com.google.gson.annotations.SerializedName;

public class UpcomingEvent {
    @SerializedName("name")
    private String name;

    @SerializedName("date")
    private String date;

    @SerializedName("image")
    private String imageUrl;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
