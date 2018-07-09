package com.example.router.exception;

/**
 * Created by pengganggui on 2018/7/8.
 */

public class NotRouteException extends RuntimeException {

    public NotRouteException(String name, String pattern) {
        super(String.format("%s cannot be resolved with pattern %s, have you declared it in your Router?", name, pattern));
    }
}
