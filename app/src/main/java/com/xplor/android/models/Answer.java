package com.xplor.android.models;

import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("question")
    private int question;

    @SerializedName("answer")
    private String answer;

    public Answer(int question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
