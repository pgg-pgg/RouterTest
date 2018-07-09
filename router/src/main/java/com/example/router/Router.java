package com.example.router;

import android.content.Context;

import com.example.router.rule.Rule;

/**
 * Created by pengganggui on 2018/7/8.
 *
 * 1.调用Router.router方法添加路由
 * 2.调用Router.invoke方法根据pattern调用路由
 */

public class Router {

    /**
     * 添加自定义路由规则
     * @param scheme 路由scheme
     * @param rule 路由规则
     * @return
     */
    public static RouterInternal addRule(String scheme, Rule rule){
        RouterInternal router=RouterInternal.get();
        router.addRule(scheme,rule);
        return router;
    }

    /**
     * 添加路由
     * @param pattern 路由uri
     * @param klass 路由class
     * @param <T>
     * @return
     */
    public static <T> RouterInternal router(String pattern,Class<T> klass){
        return RouterInternal.get().router(pattern,klass);
    }

    /**
     * 路由调用
     * @param ctx Context
     * @param pattern 路由uri
     * @param <V>
     * @return
     */
    public static <V> V invoke(Context ctx,String pattern){
        return RouterInternal.get().invoke(ctx,pattern);
    }


    /**
     * 是否存在该路由
     * @param pattern
     * @return
     */
    public static boolean resolveRouter(String pattern) {
        return RouterInternal.get().resolveRouter(pattern);
    }

}
