package com.xplor.android.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("facebookId")
    private String facebookId;

    @SerializedName("name")
    private String name;

    @SerializedName("gender")
    private String gender;

    @SerializedName("access_token")
    private String accessToken;

    public LoginRequest(String facebookId, String name, String gender, String accessToken) {
        this.facebookId = facebookId;
        this.name = name;
        this.gender = gender;
        this.accessToken = accessToken;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
