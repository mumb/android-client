package com.xplor.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerRequest extends BaseRequest {
    @SerializedName("answers")
    private List<Answer> answers;

    public AnswerRequest(List<Answer> answers) {
        this.answers = answers;
    }
}
