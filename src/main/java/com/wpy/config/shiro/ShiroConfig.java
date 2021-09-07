package com.wpy.config.shiro;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class ShiroConfig {

    /**
     * 初始化Authenticator验证管理器，如不注入，则会导致验证失败返回未登录
     * Authorizer授权器：赋予主体有哪些权限
     */
    @Bean
    public Authenticator authenticator() {
        //扩展父类原方法，捕获原始异常
        ModularRealmAuthenticator authenticator = new MyModularRealmAuthenticator();
        //设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
        authenticator.setRealms(Arrays.asList(myAccountRealm(), myPhoneRealm()));
        /**
         FirstSuccessfulStrategy：只要有一个 Realm 验证成功即可，只返回第
         一个 Realm 身份验证成功的认证信息，其他的忽略；
         AtLeastOneSuccessfulStrategy：只要有一个 Realm 验证成功即可，和
         FirstSuccessfulStrategy 不同，返回所有 Realm 身份验证成功的认证信息；（默认）
         AllSuccessfulStrategy：所有 Realm 验证成功才算成功，且返回所有 Realm
         身份验证成功的认证信息，如果有一个失败就失败了。
         */
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 自定义realm
     * @return
     */
    @Bean
    public AccountRealm myAccountRealm(){
        AccountRealm accountRealm=new AccountRealm();
        accountRealm.setCredentialsMatcher(new CredentialsMatcher());
        return accountRealm;
    }

    @Bean
    public PhoneRealm myPhoneRealm(){
        return new PhoneRealm();
    }

    //配置安全管理器
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        List<Realm> realms= Lists.newArrayList();
        realms.add(myAccountRealm());
        realms.add(myPhoneRealm());
        //securityManager.setRealm(myUserPealm());
        securityManager.setRealms(realms);
        securityManager.setAuthenticator(authenticator());
        return securityManager;
    }



    //配置过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义过滤器
        Map<String, Filter> filterMap= Maps.newHashMap();
        filterMap.put("jwt",null);
        shiroFilterFactoryBean.setFilters(filterMap);

        //自定义拦截
//        shiroFilterFactoryBean.setLoginUrl("/user/jumpMain");
//        shiroFilterFactoryBean.setSuccessUrl("/user/jumpMain");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/templates/error.html");

//        Map<String,String> map=new HashMap<String,String>();
//        map.put("/","anon");
//        //放过静态资源
//        map.put("/static/**/*","anon");
//        //user的所有请求放过
//        map.put("/user/**","anon");
//        //disaster的请求放过
//        map.put("/disaster/**/*","anon");
//        //news的请求过滤
//        map.put("/news/**/*","anon");
//        //medical的请求过滤
//        map.put("/medical/**/*","anon");
//        //登出
//        map.put("/logout","logout");
//        //其余都需要验证
//        map.put("/**","authc");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
