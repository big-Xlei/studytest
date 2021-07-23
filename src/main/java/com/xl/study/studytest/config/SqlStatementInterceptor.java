package com.xl.study.studytest.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;
import java.util.concurrent.Executor;

@Slf4j
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SqlStatementInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            long timeConsuming = System.currentTimeMillis()-startTime;
            log.debug("执行SQL:{}ms", timeConsuming);
            if(timeConsuming>999&&timeConsuming<5000){
                log.debug("执行SQL大于1s:{}ms", timeConsuming);
            }else if(timeConsuming>=5000&&timeConsuming<10000){
                log.debug("执行SQL大于5s:{}ms", timeConsuming);
            }else if(timeConsuming>=10000){
                log.debug("执行SQL大于10s:{}ms", timeConsuming);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
