package com.xl.study.studytest.retry;

import com.xl.study.studytest.common.exception.PermissionException;
import com.xl.study.studytest.common.response.BaseResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableRetry
@EnableScheduling
@Component
@Slf4j
@Data
@EnableAsync
public class RetryService {

    @Autowired
    private RetryServiceTwo retryServiceTwo;

    /**
     * @EnableRetry：此注解用于开启重试框架，可以修饰在SpringBoot启动类上面，也可以修饰在需要重试的类上
     *    proxyTargetClass：Boolean类型，用于指明代理方式【true：cglib代理，false：jdk动态代理】默认使用jdk动态代理
     * @Retryable
     *    value：Class[]类型，用于指定需要重试的异常类型，
     *    include：Class[]类型，作用于value类似，区别尚未分析
     *    exclude：Class[]类型，指定不需要重试的异常类型
     *    maxAttemps：int类型，指定最多重试次数，默认3
     *    backoff：Backoff类型，指明补偿机制
     *    @BackOff
     *       delay:指定延迟后重试，默认为1000L,即1s后开始重试 ;
     *       multiplier:指定延迟的倍数
     * @Recover
     *    当重试次数耗尽依然出现异常时，执行此异常对应的@Recover方法。
     *    异常类型需要与Recover方法参数类型保持一致，
     *    recover方法返回值需要与重试方法返回值保证一致
     */

    @Scheduled(fixedRate = 10000000)
    @Retryable(value = PermissionException.class,maxAttempts = 4,backoff = @Backoff(delay = 1500,multiplier = 2.0))
    public void testRetry(){
        System.out.println("开始重试测试---------------------------------------");
        retryServiceTwo.retryService();
    }
    @Recover
    public void testRecover(){
        System.out.println("从事回调机制成功");
    }

}
