package com.test.client.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author: 李刚刚
 * @date: 2020-03-16 20:17
 * @description:
 * @history:
 */
@EnableOAuth2Sso
@Configuration
//对全部方法进行验证
//@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true,securedEnabled = true)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.inMemoryAuthentication()
////                .withUser("admin").password(passwordEncoder.encode("123456")).roles("ADMIN")
////                .and()
////                .withUser("user").password(passwordEncoder.encode("123456")).roles("USER");
//        auth.userDetailsService(userDetailManager(userDetailsRepository()))
//                .passwordEncoder(passwordEncoder());
//
//    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**", "/js/**", "/plugins/**", "/favicon.ico");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                // 如果不使用默认的failureHandler，需要手动指定登录失败页面，也就是login.html?error的权限为跳过
                // 默认的跳过页面由 AbstractAuthenticationFilterConfigurer.updateAccessDefaults 设置跳过页面
                // 设置逻辑可以参考相应源
                .antMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated()
////                .and().formLogin()
////                .loginPage("/oauth/login.html").loginProcessingUrl("/oauth/login").permitAll()
////                .failureHandler(authenticationFailureHandler())
////                .successHandler(authenticationSuccessHandler()) // .defaultSuccessUrl(defaultSuccessUrl)
                .and().httpBasic();
    }

    /**
     * 重写AuthenticationManager，防止启动不了
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
