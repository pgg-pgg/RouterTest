package com.example.router.rule;

import android.content.BroadcastReceiver;

import com.example.router.exception.ReceiverNotRouteException;

/**
 * Created by pengganggui on 2018/7/8.
 */

public class ReceiverRule extends BaseIntentRule<BroadcastReceiver> {

    public static final String RECEIVER_SCHEME="receiver://";
    @Override
    public void throwException(String pattern) {
        throw new ReceiverNotRouteException(pattern);
    }
}
