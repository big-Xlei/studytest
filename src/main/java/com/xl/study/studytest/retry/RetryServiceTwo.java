package com.xl.study.studytest.retry;

import com.xl.study.studytest.common.exception.PermissionException;
import org.springframework.stereotype.Service;

@Service
public class RetryServiceTwo {

    public void retryService(){
        throw new PermissionException("再次测试回调");

    }
}
