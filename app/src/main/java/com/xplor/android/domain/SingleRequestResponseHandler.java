package com.xplor.android.domain;


import com.xplor.android.storage.XplorDataException;

public class SingleRequestResponseHandler<T, R> extends UseCaseHandler<R> {
    private T input;
    private XplorHandlerFunction<T, R> handlerFunc;
    private XplorSimpleFunction<R> simpleFunc;

    SingleRequestResponseHandler(T input, XplorHandlerFunction<T, R> handlerFunc) {
        this.input = input;
        this.handlerFunc = handlerFunc;
    }

    SingleRequestResponseHandler(XplorSimpleFunction<R> simpleFunc) {
        this.simpleFunc = simpleFunc;
    }

    @Override
    public R runUseCase() throws XplorDataException {
        if (handlerFunc != null) {
            return handlerFunc.apply(this, input);
        } else {
            return simpleFunc.apply(this);
        }
    }

    public interface XplorHandlerFunction<T, R> {
        R apply(UseCaseHandler useCaseHandler, T params) throws XplorDataException;
    }

    public interface XplorSimpleFunction<R> {
        R apply(UseCaseHandler useCaseHandler) throws XplorDataException;
    }
}
