package com.xl.study.studytest.common.exception;

import com.xl.study.studytest.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Description: 统一异常类处理
 * @Author: xionglei
 * @Date: 2021/2/24 11:49
 * @return:
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = TokenException.class)
    public BaseResponse<String> tokenExceptionHandler(TokenException ex) {
        ex.printStackTrace();
        return BaseResponse.responseTokenInvalid(ex.getMessage());
    }

    @ExceptionHandler(value = PermissionException.class)
    public BaseResponse<String> permissionExceptionHandler(PermissionException ex) {
        ex.printStackTrace();
        return BaseResponse.responseUnauthorized(ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse exceptionHandler(Exception ex) {
        ex.getStackTrace();
        if (ex instanceof BindException){
            BindException e = (BindException)ex;
            List<ObjectError> errors = e.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return BaseResponse.responseParamInvalid(msg);
        }else {
            return BaseResponse.responseFail(ex.getMessage());
        }
    }


    @ExceptionHandler(value = ServiceException.class)
    public BaseResponse<String> userExceptionHandler(ServiceException ex) {
        ex.printStackTrace();
        return BaseResponse.responseService(ex.getMessage());
    }
    @ExceptionHandler(value = UnauthorizedException.class)
    public BaseResponse<String> unauthorizedExceptionHandler(UnauthorizedException ex) {
        ex.printStackTrace();
        return BaseResponse.responseUnauthorized("权限不足");
    }
}
