package com.test.resource.config;

import com.test.resource.security.UserDetailsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * @author: 李刚刚
 * @date: 2020-03-16 20:17
 * @description:
 * @history:
 */
@Configuration
@EnableWebSecurity
//对全部方法进行验证
//@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true,securedEnabled = true)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(passwordEncoder.encode("123456")).roles("ADMIN")
//                .and()
//                .withUser("user").password(passwordEncoder.encode("123456")).roles("USER");
        auth.userDetailsService(userDetailManager(userDetailsRepository()))
                .passwordEncoder(passwordEncoder());

    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**", "/js/**", "/plugins/**", "/favicon.ico");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                // 如果不使用默认的failureHandler，需要手动指定登录失败页面，也就是login.html?error的权限为跳过
                // 默认的跳过页面由 AbstractAuthenticationFilterConfigurer.updateAccessDefaults 设置跳过页面
                // 设置逻辑可以参考相应源
                .anyRequest().authenticated();
        http.httpBasic();
////                .and().formLogin()
////                .loginPage("/oauth/login.html").loginProcessingUrl("/oauth/login").permitAll()
////                .failureHandler(authenticationFailureHandler())
////                .successHandler(authenticationSuccessHandler()) // .defaultSuccessUrl(defaultSuccessUrl)
//                .and().httpBasic().disable();
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

    @Bean
    public UserDetailsRepository userDetailsRepository() {
        UserDetailsRepository repository = new UserDetailsRepository();
        User user = new User("admin", passwordEncoder().encode("123456"), AuthorityUtils.NO_AUTHORITIES);
        repository.createUser(user);
        return repository;
    }

    @Bean
    public UserDetailsManager userDetailManager(UserDetailsRepository userDetailsRepository) {
        return new UserDetailsManager() {
            @Override
            public void createUser(UserDetails user) {
                userDetailsRepository.createUser(user);
            }

            @Override
            public void updateUser(UserDetails user) {
                userDetailsRepository.updateUser(user);
            }

            @Override
            public void deleteUser(String username) {
                userDetailsRepository.deleteUser(username);
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                userDetailsRepository.changePassword(oldPassword, newPassword);
            }

            @Override
            public boolean userExists(String username) {
                return userDetailsRepository.userExists(username);
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userDetailsRepository.loadUserByUsername(username);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
