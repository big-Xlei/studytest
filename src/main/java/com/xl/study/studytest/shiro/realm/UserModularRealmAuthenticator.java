package com.xl.study.studytest.shiro.realm;

import com.xl.study.studytest.domin.po.TokenToUser;
import com.xl.study.studytest.jwt.JwtTokenUtil;
import com.xl.study.studytest.shiro.auth.AuthToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
*@Description: 在进入realm之前会先进入ModularRealmAuthenticator执行doAuthenticate方法
*@Author: xionglei
*@Date: 2021/4/7 14:48
*
*@return:
*/
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {
    private Map<String, Object> definedRealms;

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
//        CustomLoginToken token = (CustomLoginToken) authenticationToken;

        // 找到当前登录人的登录类型
//        String loginType = token.getLoginType();

        AuthToken token = (AuthToken) authenticationToken;
        TokenToUser tokenToUser = JwtTokenUtil.parseToken((String) token.getPrincipal());
        String userName = tokenToUser.getUserName();

        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 找到登录类型对应的指定Realm
        Collection<Realm> typeRealms = new ArrayList<Realm>();
        for (Realm realm : realms) {
            if (realm.getName().toLowerCase().contains(userName))
                typeRealms.add(realm);
        }

        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1)
            return doSingleRealmAuthentication(typeRealms.iterator().next(), token);
        else
            return doMultiRealmAuthentication(typeRealms, token);
    }




    public void setDefinedRealms(Map<String, Object> definedRealms) {
        this.definedRealms = definedRealms;
    }
}
