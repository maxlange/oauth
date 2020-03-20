package com.test.oauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

/**
 * @author: 李刚刚
 * @date: 2020-03-16 15:31
 * @description:
 * @history:
 */
@Configuration
@EnableResourceServer
@EnableWebSecurity
@Order(111)
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerConfigure.class);
    /**
     * <p>
     * 添加资源服务器的特定属性（例如资源ID）。 <br>
     * 默认情况下不需要做任何处理。<br>
     * 适用于有更改资源ID需求的配置。
     * </p>
     *
     * @param resources 资源服务器的配置
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        // 指定资源服务器的资源ID
        // 如果给某个授权指定了可以访问的资源ID，则仅能访问指定的资源，如果不给某个授权指定指定访问的资源ID，则代表可以访问所有资源
//        resources.resourceId("USERS");
        // 指定服务是无状态的
//        resources.stateless(true);
    }

    /**
     * <p>
     * 配置安全资源的访问规则<br>
     * 默认情况下，"/oauth/**"中的所有资源受保护（如果没有给出有关范围的特定规则的话）。<br>
     * 默认情况下，您还会获得一个{@link OAuth2WebSecurityExpressionHandler}
     * </p>
     *
     * @param http 当前http过滤器配置
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        //会话管理
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //将这些路径认定为资源
//        http.requestMatchers().antMatchers("/**");
        //自定义登出
//        http.logout()
//                .logoutUrl("/logout")
//                .clearAuthentication(true)
//                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
//                .addLogoutHandler(customLogoutHandler());

    }
}
