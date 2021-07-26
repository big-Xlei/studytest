package com.xl.study.studytest.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xl.study.studytest.beans.UserInfo;
import com.xl.study.studytest.beans.service.UserInfoService;
import com.xl.study.studytest.common.exception.TokenException;
import com.xl.study.studytest.common.response.BaseResponse;
import com.xl.study.studytest.jwt.JwtTokenUtil;
import com.xl.study.studytest.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public BaseResponse login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password){

        String token = "";
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_name",userName);
        UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);

        if (!userInfo.getUserName().equals(userName)){
            System.out.println("用户不存在");
            throw new TokenException("用户不存在");
        }

        if (!userInfo.getPassword().equals(password)){
            System.out.println("密码错误");
            throw new TokenException("密码错误");
        }
        token = JwtTokenUtil.createToken(Integer.toString(userInfo.getId()), userInfo.getUserName(), userInfo.getPassword());
        String access_token = "access_token_" + userInfo.getId();
        redisUtil.set(access_token, token);
        redisUtil.expire(access_token, 1, TimeUnit.DAYS);
        return BaseResponse.responseSuccess(token);
    }

    @RequiresRoles({"admin"})
    @GetMapping("/add")
    public BaseResponse add(){
        return BaseResponse.responseSuccess("success");
    }

    @RequiresRoles({"admin"})
    @RequiresPermissions("user:permissions")
    @GetMapping("/permiss")
    public BaseResponse permissions(){
        return BaseResponse.responseSuccess("success");
    }

    @RequiresRoles({"user"})
    @GetMapping("/user")
    public BaseResponse userper(){
        return BaseResponse.responseSuccess("success");
    }
}
