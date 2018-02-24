package com.xplor.android.storage;

import android.content.Context;

import com.facebook.login.LoginManager;
import com.xplor.android.domain.UseCaseHandler;
import com.xplor.android.models.AnswerRequest;
import com.xplor.android.models.GenericResponse;
import com.xplor.android.models.Leader;
import com.xplor.android.models.LoginRequest;
import com.xplor.android.models.Museum;
import com.xplor.android.models.MuseumDetailResponse;
import com.xplor.android.models.QuizResponse;
import com.xplor.android.models.User;
import com.xplor.android.models.XplorError;
import com.xplor.android.storage.local.LocalDataRepository;
import com.xplor.android.storage.remote.RemoteDataRepository;
import com.xplor.android.storage.remote.RemoteRepositoryConfig;
import com.xplor.android.utils.Constants;
import com.xplor.android.utils.Functions;
import com.xplor.android.utils.XplorFunction;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RepositoriesManager {
    final RemoteRepositoryConfig remoteRepositoryConfig;
    private final LocalDataRepository localDataRepository;
    private final RemoteDataRepository remoteDataRepository;
    private final Context appContext;

    public RepositoriesManager(Context appContext, RemoteRepositoryConfig remoteConfig) {
        this.appContext = appContext;
        this.localDataRepository = new LocalDataRepository(appContext);
        this.remoteRepositoryConfig = remoteConfig;
        this.remoteDataRepository = remoteRepositoryConfig.getApiService();
    }

    /**
     * Convenience method used when the error type is similar to request type
     *
     * @param request  param for the function. Also used to get the error type
     * @param function to execute
     * @param <E>      function request type
     * @param <T>      return type
     * @return
     * @throws XplorDataException
     */
    protected <E, T> T executeCall(UseCaseHandler handler, E request, XplorFunction<E, Call<T>> function) throws XplorDataException {
        return executeCall(handler, request, function.apply(request));
    }

    protected <T, R> T executeCall(UseCaseHandler handler, R errorResponseType, Call<T> call) throws XplorDataException {
        try {
            if (handler != null) {
                if (handler.isCancelled()) {
                    throw new XplorDataException();
                }
                handler.registerRepositoryHandler(new RepositoryHandler().registerCall(call));
            }
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                ResponseBody errorResponseBody = response.errorBody();
                XplorDataException exception = new XplorDataException();
                int errorCode = response.code();
                if (errorResponseBody != null && errorResponseType != null) {
                    String errorJson = errorResponseBody.string();
                    R errorObject;
                    if (errorResponseType instanceof Type) {
                        errorObject = (R) remoteRepositoryConfig.getErrorGsonInstance().fromJson(errorJson,
                                (Type) (errorResponseType));
                    } else {
                        errorObject = (R) remoteRepositoryConfig.getErrorGsonInstance().fromJson(errorJson,
                                (Class<R>) (errorResponseType instanceof Class ? errorResponseType : errorResponseType.getClass()));
                    }
                    exception.setErrorObject(errorObject);
                    exception.setErrorCode(errorCode);
                } else {
                    if (errorCode > 0) {
                        exception.setErrorCode(errorCode);
                    } else {
                        exception.setErrorCode(Functions.isOnline(appContext) ?
                                Constants.ERROR_UNEXPECTED : Constants.ERROR_NO_NETWORK);
                    }
                }
                throw exception;
            }
        } catch (IOException e) {
            XplorDataException exception = new XplorDataException();
            exception.setErrorCode(Functions.isOnline(appContext) ?
                    Constants.ERROR_UNEXPECTED : Constants.ERROR_NO_NETWORK);
            throw exception;
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof XplorDataException) {
                throw e;
            }
            throw new XplorDataException();
        }
    }

    public User getUser(UseCaseHandler handler) throws XplorDataException {
        return executeCall(handler, XplorError.class, remoteDataRepository.getUser());
    }

    public GenericResponse login(UseCaseHandler useCaseHandler, LoginRequest loginRequest) throws XplorDataException {
        return executeCall(useCaseHandler, null, remoteDataRepository.login(
                loginRequest.getFacebookId(), loginRequest.getName(), loginRequest.getGender(), loginRequest.getAccessToken())
        );
    }

    public List<Museum> getMuseums(UseCaseHandler useCaseHandler, double lat, double lng) throws XplorDataException {
        return executeCall(useCaseHandler, null, remoteDataRepository.getMuseums(lat, lng));
    }

    public QuizResponse getQuiz(UseCaseHandler useCaseHandler, String quizId) throws XplorDataException {
        return executeCall(useCaseHandler, quizId, remoteDataRepository::getQuiz);
    }

    public MuseumDetailResponse getMuseumDetail(UseCaseHandler useCaseHandler, String museumId) throws XplorDataException {
        return executeCall(useCaseHandler, museumId, remoteDataRepository::getMuseumDetail);
    }

    public QuizResponse submitQuiz(UseCaseHandler useCaseHandler, String quizId, AnswerRequest request) throws XplorDataException {
        return executeCall(useCaseHandler, null,
                remoteDataRepository.submitQuiz(quizId, request));
    }

    public List<Leader> getLeaderBoard(UseCaseHandler useCaseHandler) throws XplorDataException {
        return executeCall(useCaseHandler, null, remoteDataRepository.getLeaderBoard());
    }

    public void logout() {
        remoteRepositoryConfig.clearCache();
        LoginManager.getInstance().logOut();
    }
}
