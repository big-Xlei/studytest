package com.xl.study.studytest.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
*@Description: 统一结果集处理
*@Author: xionglei
*@Date: 2021/2/24 10:33
*
*@return:
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private int code;
    private String date;
    private String msg;
    private T data;


    public BaseResponse(ResponseCode responseCode, T data) {
        this.date = this.getDate();
        this.code = responseCode.getCode();
        this.success = responseCode.getSuccess();
        this.data = data;
    }

    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
        String format = simpleDateFormat.format(Calendar.getInstance().getTime());
        return format;
    }


    //响应成功
    public static <T> BaseResponse responseSuccess(T data) {
        return new BaseResponse<T>(ResponseCode.SUCCESS, data);
    }

    //响应失败，系统异常
    public static BaseResponse responseFail(String msg) {
        BaseResponse resp = new BaseResponse(ResponseCode.SYSTEM_ERROR, null);
        resp.setMsg(msg == null || msg.equals("") ? ResponseCode.SYSTEM_ERROR.getMsg() : msg);
        return resp;
    }

    //token过期或错误
    public static BaseResponse responseTokenInvalid(String msg) {
        BaseResponse resp = new BaseResponse(ResponseCode.TOKEN_ERROR, null);
        resp.setMsg(msg == null || msg.equals("") ? ResponseCode.TOKEN_ERROR.getMsg() : msg);
        return resp;
    }

    //参数校验错误
    public static BaseResponse responseParamInvalid(String msg) {
        BaseResponse resp = new BaseResponse(ResponseCode.PARAM_ERROR, null);
        resp.setMsg(msg == null || msg.equals("") ? ResponseCode.PARAM_ERROR.getMsg() : msg);
        return resp;
    }

    //缺少必要参数
    public static BaseResponse responseMissRequestParam(String msg) {
        BaseResponse resp = new BaseResponse(ResponseCode.MISSING_REQUEST_PARAM_ERROR, null);
        resp.setMsg(msg == null || msg.equals("") ? ResponseCode.MISSING_REQUEST_PARAM_ERROR.getMsg() : msg);
        return resp;
    }

    //权限不足
    public static BaseResponse responseUnauthorized(String msg) {
        BaseResponse resp = new BaseResponse(ResponseCode.UNAUTHORIZED, null);
        resp.setMsg(msg == null || msg.equals("") ? ResponseCode.UNAUTHORIZED.getMsg() : msg);
        return resp;
    }

    //服务异常
    public static BaseResponse responseService(String msg) {
        BaseResponse resp = new BaseResponse(ResponseCode.SERVICE_ERROR, null);
        resp.setMsg(msg == null || msg.equals("") ? ResponseCode.SERVICE_ERROR.getMsg() : msg);
        return resp;
    }

    //上传文件失败，返回文件名
    public static BaseResponse responseUpload(String msg) {
        BaseResponse resp = new BaseResponse(ResponseCode.UPLOAD_ERROR, null);
        resp.setMsg(msg == null || msg.equals("") ? ResponseCode.SERVICE_ERROR.getMsg() : msg);
        return resp;
    }

}
