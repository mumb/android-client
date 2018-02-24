package com.xplor.android.models;

import com.google.gson.annotations.SerializedName;

public class QuizCategory {
    @SerializedName("id")
    private String quizId;

    @SerializedName("category")
    private String category;

    public String getQuizId() {
        return quizId;
    }

    public String getCategory() {
        return category;
    }
}
