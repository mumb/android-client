package com.xplor.android.utils;

/**
 *
 * @param <T> Input parameter type
 * @param <R> Return type
 */
public interface XplorFunction<T, R> {
    R apply(T params);
}
