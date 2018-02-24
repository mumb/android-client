package com.xplor.android.domain;

import com.xplor.android.models.AnswerRequest;
import com.xplor.android.models.GenericResponse;
import com.xplor.android.models.Leader;
import com.xplor.android.models.LoginRequest;
import com.xplor.android.models.Museum;
import com.xplor.android.models.MuseumDetailResponse;
import com.xplor.android.models.QuizResponse;
import com.xplor.android.models.User;
import com.xplor.android.storage.RepositoriesManager;
import com.xplor.android.storage.XplorDataException;

import java.util.List;

public class UseCaseManager {

    protected final UseCaseScheduler useCaseScheduler;
    protected final RepositoriesManager repositoriesManager;
    protected static UseCaseManager INSTANCE;

    public UseCaseManager(UseCaseScheduler useCaseScheduler, RepositoriesManager repositoriesManager) {
        this.useCaseScheduler = useCaseScheduler;
        this.repositoriesManager = repositoriesManager;
    }

    public void execute(UseCaseHandler handler) {
        if (handler.isActive()) {
            return;
        }
        handler.setActiveState(true);
        useCaseScheduler.execute(handler);
    }

    public UseCaseHandler<GenericResponse> login(LoginRequest request) {
        return new UseCaseHandler<GenericResponse>() {
            @Override
            public GenericResponse runUseCase() throws XplorDataException {
                GenericResponse genericResponse = repositoriesManager.login(this, request);
                repositoriesManager.getUser(this);
                return genericResponse;
            }
        };
    }

    public UseCaseHandler<List<Museum>> getMuseums(double lat, double lng) {
        return new UseCaseHandler<List<Museum>>() {
            @Override
            public List<Museum> runUseCase() throws XplorDataException {
                return repositoriesManager.getMuseums(this, lat, lng);
            }
        };
    }

    public UseCaseHandler<QuizResponse> getQuiz(String quizId) {
        return new SingleRequestResponseHandler<>(quizId, repositoriesManager::getQuiz);
    }

    public UseCaseHandler<MuseumDetailResponse> getMuseumDetail(String museumId) {
        return new SingleRequestResponseHandler<>(museumId, repositoriesManager::getMuseumDetail);
    }

    public UseCaseHandler<User> getUser() {
        return new SingleRequestResponseHandler<>(repositoriesManager::getUser);
    }

    public UseCaseHandler<QuizResponse> submitQuiz(String quizId, AnswerRequest request) {
        return new UseCaseHandler<QuizResponse>() {
            @Override
            public QuizResponse runUseCase() throws XplorDataException {
                return repositoriesManager.submitQuiz(this, quizId, request);
            }
        };
    }

    public UseCaseHandler<QuizResponse> submitQuizWithProfileRefresh(String quizId, AnswerRequest request) {
        return new UseCaseHandler<QuizResponse>() {
            @Override
            public QuizResponse runUseCase() throws XplorDataException {
                QuizResponse quizResponse = repositoriesManager.submitQuiz(this, quizId, request);
                repositoriesManager.getUser(this);
                return quizResponse;
            }
        };
    }

    public UseCaseHandler<List<Leader>> getLeaderBoard() {
        return new SingleRequestResponseHandler<>(repositoriesManager::getLeaderBoard);
    }

    public void logout() {
        repositoriesManager.logout();
    }
}

