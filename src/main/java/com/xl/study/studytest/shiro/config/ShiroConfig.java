package com.xl.study.studytest.shiro.config;

import com.xl.study.studytest.shiro.filter.AuthFilter;
import com.xl.study.studytest.shiro.realm.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

    //shiroFilterFactoryBean:第三步
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //设置自定义过滤器
        HashMap<String, Filter> filters = new HashMap<>();
        filters.put("auth",new AuthFilter());
        shiroFilterFactoryBean.setFilters(filters);

        //添加shiro的内置过滤器
        /*
            anon:无需认证就可以访问
            authc:必须认证才能访问
            user:必须拥有记住我功能才能使用
            perms:拥有对某个资源的权限才能访问
            roles:拥有某个角色的权限才能访问
         */
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/user/login","anon");
//        filterMap.put("/user/add","roles[admin]");
        filterMap.put("/**","auth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);


        //设置登入请求
//        shiroFilterFactoryBean.setLoginUrl("/login");
        //设置未授权页面
//        shiroFilterFactoryBean.setUnauthorizedUrl("noauth");

        return shiroFilterFactoryBean;
    }

    //DefaultWebSecurityManager：第二步
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }


    //创建realm对象，需要定义类：第一步
    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}

