package com.xplor.android.storage.remote;

import com.xplor.android.models.AnswerRequest;
import com.xplor.android.models.GenericResponse;
import com.xplor.android.models.Leader;
import com.xplor.android.models.Museum;
import com.xplor.android.models.MuseumDetailResponse;
import com.xplor.android.models.QuizResponse;
import com.xplor.android.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteDataRepository {
    @FormUrlEncoded
    @POST("/login")
    Call<GenericResponse> login(@Field("facebookId") String facebookId, @Field("name") String name,
                                @Field("gender") String gender, @Field("access_token") String accessToken);

    @GET("/museums")
    Call<List<Museum>> getMuseums(@Query("lat") double lat, @Query("lng") double lng);

    @GET("/quiz/{quizId}")
    Call<QuizResponse> getQuiz(@Path("quizId") String quizId);

    @GET("museums/{museumId}")
    Call<MuseumDetailResponse> getMuseumDetail(@Path("museumId") String museumId);

    @GET("/profile")
    Call<User> getUser();

    @POST("/quiz/{quizId}/submit")
    Call<QuizResponse> submitQuiz(@Path("quizId") String quizId, @Body AnswerRequest answerRequest);

    @GET("/leaderboard")
    Call<List<Leader>> getLeaderBoard();
}
