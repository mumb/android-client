package com.xplor.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Leader implements Parcelable {
    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("points")
    private long points;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public long getPoints() {
        return points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.name);
        dest.writeLong(this.points);
    }

    public Leader() {
    }

    protected Leader(Parcel in) {
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.points = in.readLong();
    }

    public static final Parcelable.Creator<Leader> CREATOR = new Parcelable.Creator<Leader>() {
        @Override
        public Leader createFromParcel(Parcel source) {
            return new Leader(source);
        }

        @Override
        public Leader[] newArray(int size) {
            return new Leader[size];
        }
    };
}
