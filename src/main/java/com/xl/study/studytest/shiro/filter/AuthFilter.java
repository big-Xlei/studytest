package com.xl.study.studytest.shiro.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xl.study.studytest.common.exception.TokenException;
import com.xl.study.studytest.jwt.JwtTokenUtil;
import com.xl.study.studytest.shiro.auth.AuthToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Component
public class AuthFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String token = JwtTokenUtil.getRequestToken((HttpServletRequest) servletRequest);
        return new AuthToken(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        //获取请求token，如果token不存在，直接返回
        String token = JwtTokenUtil.getRequestToken((HttpServletRequest) servletRequest);
        if (StringUtils.isBlank(token)) {
            throw new TokenException("请携带token令牌");
        }
        return executeLogin(servletRequest, servletResponse);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        throw new TokenException("登录凭证已失效，请重新登录");
    }
}
