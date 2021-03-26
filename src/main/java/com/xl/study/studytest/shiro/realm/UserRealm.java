package com.xl.study.studytest.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xl.study.studytest.beans.UserInfo;
import com.xl.study.studytest.beans.service.UserInfoService;
import com.xl.study.studytest.domin.po.TokenToUser;
import com.xl.study.studytest.jwt.JwtTokenUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoService userInfoService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //给用户授权
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        UserInfo userInfo = (UserInfo)principalCollection.getPrimaryPrincipal();
        String role = userInfo.getRole();
        simpleAuthorizationInfo.addRole(role);

        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("进行了 -> doGetAuthenticationInfo");

        String toke = (String)authenticationToken.getPrincipal();

        TokenToUser tokenToUser = JwtTokenUtil.parseToken(toke);

        Integer userId = tokenToUser.getUserId();

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("id",userId);
        UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);

        //用来验证密码
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userInfo, toke, getName());

        return simpleAuthenticationInfo;
    }
}
