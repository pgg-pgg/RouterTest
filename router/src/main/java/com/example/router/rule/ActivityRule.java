package com.example.router.rule;

import android.app.Activity;

import com.example.router.exception.ActivityNotRouteException;

/**
 * Created by pengganggui on 2018/7/8.
 * activity路由规则
 */

public class ActivityRule extends BaseIntentRule<Activity>{

    /**
     * activity路由scheme
     */
    public static final String ACTIVITY_SCHEME="activity://";

    @Override
    public void throwException(String pattern) {
        throw new ActivityNotRouteException(pattern);
    }
}
