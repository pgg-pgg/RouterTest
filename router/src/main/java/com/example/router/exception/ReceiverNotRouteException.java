package com.example.router.exception;

/**
 * Created by pengganggui on 2018/7/8.
 */

public class ReceiverNotRouteException extends NotRouteException {

    public ReceiverNotRouteException(String pattern) {
        super("receiver", pattern);
    }
}
