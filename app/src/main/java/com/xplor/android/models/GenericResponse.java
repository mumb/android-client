package com.xplor.android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenericResponse extends BaseResponse {
    @SerializedName("message")
    @Expose
    private String message;
}
