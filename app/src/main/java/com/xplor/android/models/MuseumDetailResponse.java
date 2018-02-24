package com.xplor.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MuseumDetailResponse extends BaseResponse {
    @SerializedName("description")
    private String description;

    @SerializedName("thumbnail")
    private String thumbnailUrl;

    @SerializedName("images")
    private List<String> photos;

    @SerializedName("upcoming_events")
    private List<UpcomingEvent> upcomingEvents;

    @SerializedName("quizzes")
    private List<QuizCategory> quizCategories;

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public List<UpcomingEvent> getUpcomingEvents() {
        return upcomingEvents;
    }

    public List<QuizCategory> getQuizCategories() {
        return quizCategories;
    }
}
