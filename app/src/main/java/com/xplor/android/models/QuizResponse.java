package com.xplor.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizResponse extends BaseResponse implements Parcelable {
    @SerializedName("id")
    private String quizId;

    @SerializedName("museum_name")
    private String museumName;

    @SerializedName("category")
    private String quizCategory;

    @SerializedName(value = "answers", alternate = {"questions"})
    private List<Question> questions;

    @SerializedName("points")
    private int points;

    public String getQuizId() {
        return quizId;
    }

    public String getMuseumName() {
        return museumName;
    }

    public String getQuizCategory() {
        return quizCategory;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getPoints() {
        return points;
    }

    public QuizResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quizId);
        dest.writeString(this.museumName);
        dest.writeString(this.quizCategory);
        dest.writeTypedList(this.questions);
        dest.writeInt(this.points);
    }

    protected QuizResponse(Parcel in) {
        this.quizId = in.readString();
        this.museumName = in.readString();
        this.quizCategory = in.readString();
        this.questions = in.createTypedArrayList(Question.CREATOR);
        this.points = in.readInt();
    }

    public static final Creator<QuizResponse> CREATOR = new Creator<QuizResponse>() {
        @Override
        public QuizResponse createFromParcel(Parcel source) {
            return new QuizResponse(source);
        }

        @Override
        public QuizResponse[] newArray(int size) {
            return new QuizResponse[size];
        }
    };
}
