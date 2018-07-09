package com.example.router.rule;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

/**
 * Created by pengganggui on 2018/7/8.
 * 返回Intent的路由规则的基类
 */

public abstract class BaseIntentRule<T> implements Rule<T,Intent> {

    private HashMap<String,Class<T>> mIntentRules;

    public BaseIntentRule(){
        mIntentRules=new HashMap<>();
    }

    @Override
    public void router(String pattern, Class<T> klass) {
        mIntentRules.put(pattern,klass);
    }

    @Override
    public Intent invoke(Context ctx, String pattern) {
        Class<T> klass=mIntentRules.get(pattern);
        if (klass==null){
            throwException(pattern);
        }
        return new Intent(ctx,klass);
    }

    /**
     * 当找不到路由规则时抛出异常
     * @param pattern
     */
    public abstract void throwException(String pattern);

    @Override
    public boolean resolveRule(String pattern) {
        return mIntentRules.get(pattern)!=null;
    }
}
