package com.test.resource.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * @author: 李刚刚
 * @date: 2020-03-16 15:31
 * @description:
 * @history:
 */
@Configuration
@EnableResourceServer
//@EnableWebSecurity
//@Order(111)
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerConfigure.class);

    @Autowired
    private DataSource dataSource;

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
//        super.configure(resources);
        // 指定资源服务器的资源ID
        // 如果给某个授权指定了可以访问的资源ID，则仅能访问指定的资源，如果不给某个授权指定指定访问的资源ID，则代表可以访问所有资源
//        resources.resourceId("Resource");
//        // 指定服务是无状态的
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
        http.exceptionHandling()
                .authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.requestMatcher(new OAuth2RequestMatcher()).
        authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated();
        http.httpBasic();
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

    @Bean
    public JdbcTokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * OAuth2 请求匹配器
     */
    private static class OAuth2RequestMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest httpServletRequest) {
            String authorization = httpServletRequest.getHeader("Authorization");
            boolean havaOAuth2Token = authorization != null && authorization.startsWith("Bearer");
            boolean havaAccessToken = httpServletRequest.getParameter("access_token") != null;
            return havaAccessToken || havaOAuth2Token;
        }
    }
}
