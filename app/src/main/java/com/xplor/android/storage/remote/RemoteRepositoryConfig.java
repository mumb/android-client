package com.xplor.android.storage.remote;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.xplor.android.BuildConfig;
import com.xplor.android.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteRepositoryConfig {
    private Context appContext;
    private OkHttpClient httpClient;
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson;
    private Gson errorGson;
    protected PersistentCookieJar persistentCookieJar;

    /**
     * @param appContext Should be Application Context
     */
    public RemoteRepositoryConfig(Context appContext) {
        this.appContext = appContext;
        if (!(appContext instanceof Application)) {
            throw new RuntimeException();
        }
    }

    protected OkHttpClient getHttpClient() {
        if (httpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logging);
            }

            CookieCache cookieCache = new SetCookieCache();
            persistentCookieJar =
                    new PersistentCookieJar(cookieCache, new SharedPrefsCookiePersistor(appContext));
            builder.cookieJar(persistentCookieJar);

            builder.addNetworkInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                //Set Cookie Header
                for (Cookie cookie : cookieCache) {
                    if ("csrftoken".equals(cookie.name())) {
                        requestBuilder.header("X-CSRFToken", cookie.value());
                        break;
                    }
                }

                Response response = chain.proceed(requestBuilder.build());
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        String strContentType = contentType.toString();
                        if (!TextUtils.isEmpty(strContentType) && strContentType.contains("image")) {
                            return response;
                        }
                    }
                    String strResponseBody = responseBody.string();
                    if (strResponseBody.length() == 0) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("message", "ok");
                            ResponseBody body = ResponseBody.create(contentType, jsonObject.toString());
                            return response.newBuilder().body(body).build();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ResponseBody newResponseBody = ResponseBody.create(contentType, strResponseBody);
                        return response.newBuilder().body(newResponseBody).build();
                    }
                }
                return response;
            });

            builder.connectTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
            httpClient = builder.build();
        }
        return httpClient;
    }

    protected Retrofit getApiClient() {
        return new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
                .build();
    }

    public RemoteDataRepository getApiService() {
        return getApiClient().create(RemoteDataRepository.class);
    }

    public Gson getGsonInstance() {
        if (gson == null) {
            gsonBuilder.registerTypeAdapter(User.class, (InstanceCreator<User>) type -> User.getInstance());
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public Gson getErrorGsonInstance() {
        if (errorGson == null) {
            gsonBuilder
                    //This adapter converts the array of errors to a single String object
                    .registerTypeAdapter(String.class,
                            (JsonDeserializer<String>) (json, typeOfSrc, context) -> {
                                StringBuilder strBuilder = new StringBuilder();
                                if (json.isJsonArray()) {
                                    JsonArray strArr = json.getAsJsonArray();
                                    int size = strArr.size();
                                    for (int i = 0; i < size; i++) {
                                        if (i != 0) {
                                            strBuilder.append("\n");
                                        }
                                        strBuilder.append((String) context.deserialize(strArr.get(i), String.class));
                                    }
                                } else {
                                    strBuilder.append(json.getAsString());
                                }
                                return strBuilder.toString();
                            });
            errorGson = gsonBuilder.create();
        }
        return errorGson;
    }

    public void clearCache() {
        if (persistentCookieJar != null) {
            persistentCookieJar.clear();
        }
    }
}
