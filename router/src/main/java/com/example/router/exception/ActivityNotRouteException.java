package com.example.router.exception;

/**
 * Created by pengganggui on 2018/7/8.
 */

public class ActivityNotRouteException extends NotRouteException {


    public ActivityNotRouteException(String pattern) {
        super("activity", pattern);
    }
}
