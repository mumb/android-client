package com.xplor.android.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.xplor.android.utils.Saveable;

public class User extends BaseResponse implements Saveable, Parcelable {
    private static User INSTANCE;
    private static final String SAVEABLE_USER = "saveableUser";

    @SerializedName("id")
    private String facebookId;

    @SerializedName("name")
    private String name;

    @SerializedName("accuracy")
    private float accuracy;

    @SerializedName("points")
    private long points;

    @SerializedName("quiz_count")
    private long quizCount;

    @SerializedName("rank")
    private Integer rank;

    public String getFacebookId() {
        return facebookId;
    }

    public String getName() {
        return name;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public long getPoints() {
        return points;
    }

    public long getQuizCount() {
        return quizCount;
    }

    public Integer getRank() {
        return rank;
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(facebookId);
    }

    public static User getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new User();
        }
        return INSTANCE;
    }

    public static void removeUserInstance() {
        INSTANCE = null;
    }

    public static void setUserInstance(String facebookId, String name) {
        INSTANCE = new User();
        INSTANCE.facebookId = facebookId;
        INSTANCE.name = name;
    }

    @Override
    public void saveInstanceState(Bundle bundle) {
        bundle.putParcelable(SAVEABLE_USER, INSTANCE);
    }

    @Override
    public void restoreInstanceState(Bundle bundle) {
        INSTANCE = bundle.getParcelable(SAVEABLE_USER);
    }

    public User() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.facebookId);
        dest.writeString(this.name);
        dest.writeFloat(this.accuracy);
        dest.writeLong(this.points);
        dest.writeLong(this.quizCount);
        dest.writeValue(this.rank);
    }

    protected User(Parcel in) {
        this.facebookId = in.readString();
        this.name = in.readString();
        this.accuracy = in.readFloat();
        this.points = in.readLong();
        this.quizCount = in.readLong();
        this.rank = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
