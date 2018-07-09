package com.example.router.rule;

import android.content.Context;

/**
 * Created by pengganggui on 2018/7/8.
 * 路由接口规则
 */

public interface Rule<T,V> {

    /**
     * 添加路由
     * @param pattern 路由uri
     * @param klass 路由class
     */
    void router(String pattern,Class<T> klass);

    /**
     * 路由调用
     * @param ctx Context
     * @param pattern 路由Uri
     * @return 返回对应的返回值
     */
    V invoke(Context ctx,String pattern);

    /**
     * 查看是否存在pattern对应的路由
     * @param pattern
     * @return
     */
    boolean resolveRule(String pattern);
}
