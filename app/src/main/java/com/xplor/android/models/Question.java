package com.xplor.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question implements Parcelable {
    @SerializedName("id")
    private int questionId;

    @SerializedName("question")
    private String questionText;

    @SerializedName("options")
    private List<String> options;

    @SerializedName("hint")
    private String hint;

    @SerializedName("trivia")
    private String trivia;

    @SerializedName("answer")
    private String answer;

    @SerializedName("user_answer_correct")
    private boolean userAnswerCorrect;

    @SerializedName("is_answered")
    private boolean answered;

    @SerializedName("image_url")
    private String questionImageUrl;

    @SerializedName("user_answer")
    private String userAnswer;

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getHint() {
        return hint;
    }

    public String getTrivia() {
        return trivia;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isUserAnswerCorrect() {
        return userAnswerCorrect;
    }

    public boolean isAnswered() {
        return answered;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public void setUserAnswerCorrect(boolean userAnswerCorrect) {
        this.userAnswerCorrect = userAnswerCorrect;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public Question() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.questionId);
        dest.writeString(this.questionText);
        dest.writeStringList(this.options);
        dest.writeString(this.hint);
        dest.writeString(this.trivia);
        dest.writeString(this.answer);
        dest.writeByte(this.userAnswerCorrect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.answered ? (byte) 1 : (byte) 0);
        dest.writeString(this.questionImageUrl);
        dest.writeString(this.userAnswer);
    }

    protected Question(Parcel in) {
        this.questionId = in.readInt();
        this.questionText = in.readString();
        this.options = in.createStringArrayList();
        this.hint = in.readString();
        this.trivia = in.readString();
        this.answer = in.readString();
        this.userAnswerCorrect = in.readByte() != 0;
        this.answered = in.readByte() != 0;
        this.questionImageUrl = in.readString();
        this.userAnswer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
