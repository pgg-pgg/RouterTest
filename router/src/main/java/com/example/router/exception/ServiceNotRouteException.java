package com.example.router.exception;

/**
 * Created by pengganggui on 2018/7/8.
 */

public class ServiceNotRouteException extends NotRouteException {

    public ServiceNotRouteException(String pattern) {
        super("service", pattern);
    }
}
