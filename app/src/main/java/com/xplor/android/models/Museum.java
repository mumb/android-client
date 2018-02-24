package com.xplor.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Museum implements Parcelable {
    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("country")
    private String country;

    @SerializedName("rating")
    private float rating;

    @SerializedName("description")
    private String description;

    @SerializedName("distance")
    private float distance;

    @SerializedName("id")
    private String id;

    @SerializedName("thumbnail")
    private String thumbnailUrl;

    @SerializedName("coordinates")
    private ArrayList<Double> coordinates;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public float getDistance() {
        return distance;
    }

    public String getId() {
        return id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeFloat(this.rating);
        dest.writeString(this.description);
        dest.writeFloat(this.distance);
        dest.writeString(this.id);
        dest.writeString(this.thumbnailUrl);
        dest.writeList(this.coordinates);
    }

    public Museum() {
    }

    protected Museum(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        this.rating = in.readFloat();
        this.description = in.readString();
        this.distance = in.readFloat();
        this.id = in.readString();
        this.thumbnailUrl = in.readString();
        this.coordinates = new ArrayList<Double>();
        in.readList(this.coordinates, Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Museum> CREATOR = new Parcelable.Creator<Museum>() {
        @Override
        public Museum createFromParcel(Parcel source) {
            return new Museum(source);
        }

        @Override
        public Museum[] newArray(int size) {
            return new Museum[size];
        }
    };
}
