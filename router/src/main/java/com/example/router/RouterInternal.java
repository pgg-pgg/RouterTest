package com.example.router;

import android.content.Context;

import com.example.router.exception.NotRouteException;
import com.example.router.rule.ActivityRule;
import com.example.router.rule.ReceiverRule;
import com.example.router.rule.Rule;
import com.example.router.rule.ServiceRule;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by pengganggui on 2018/7/8.
 */

public class RouterInternal {

    private static RouterInternal sInstance;

    //scheme路由规则
    private HashMap<String,Rule> mRules;

    private RouterInternal(){
        mRules=new HashMap<>();
        initDefaultRouter();
    }

    /**
     * 添加默认的Activity，Service，Receiver路由
     */
    private void initDefaultRouter() {
        addRule(ActivityRule.ACTIVITY_SCHEME,new ActivityRule());
        addRule(ServiceRule.SERVICE_SCHEME,new ServiceRule());
        addRule(ReceiverRule.RECEIVER_SCHEME,new ReceiverRule());
    }

    static RouterInternal get(){
        if (sInstance==null){
            synchronized (RouterInternal.class){
                if (sInstance==null){
                    sInstance=new RouterInternal();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加自定义路由规则
     * @param scheme 路由scheme
     * @param rule 路由规则
     * @return Router真实调用类
     */
    public final RouterInternal addRule(String scheme,Rule rule){
        mRules.put(scheme,rule);
        return this;
    }

    /**
     * 获取路由
     * @param pattern
     * @param <T>
     * @param <V>
     * @return
     */
    private <T,V> Rule<T,V> getRule(String pattern){
        HashMap<String,Rule> rules=mRules;
        Set<String> keySet=rules.keySet();
        Rule<T,V> rule=null;
        for (String scheme:keySet){
            if (pattern.startsWith(scheme)){
                rule=rules.get(scheme);
                break;
            }
        }
        return rule;
    }

    /**
     * 获取路由真实调用的方法
     * @param pattern
     * @param klass
     * @param <T>
     * @return
     */
    public final <T>RouterInternal router(String pattern,Class<T> klass){
        Rule<T,?> rule=getRule(pattern);
        if (rule==null){
            throw new NotRouteException("unKnown",pattern);
        }
        rule.router(pattern,klass);
        return this;
    }

    /**
     * 路由调用
     * @param ctx Context
     * @param pattern 路由uri
     * @return {@code V} 返回对应的返回值
     */
    final <V> V invoke(Context ctx,String pattern){
        Rule<?,V> rule=getRule(pattern);
        if (rule==null){
            throw new NotRouteException("unKnown",pattern);
        }
        return rule.invoke(ctx,pattern);
    }

    /**
     * 是否存在该路由
     * @param pattern
     * @return
     */
    final boolean resolveRouter(String pattern) {
        Rule<?, ?> rule = getRule(pattern);
        return rule != null && rule.resolveRule(pattern);
    }
}
