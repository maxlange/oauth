package com.test.oauth.config;

import com.test.oauth.exception.QbnWebResponseExceptionTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * 配置信息参看{@link AuthorizationServerConfigurer}
 *
 * @author: 李刚刚
 * @date: 2020-03-16 11:18
 * @description: oauth服务器配置
 * @history:
 */
@Configuration
@EnableAuthorizationServer
public class OauthServerConfigurer extends AuthorizationServerConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OauthServerConfigurer.class);


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;

    @Resource
    private UserDetailsManager userDetailsManager;

    /**
     * <p>
     * 授权服务器的security配置，实际上指的就是 /oauth/token端点,可以添加过滤器之类自定义的处理<br>
     * 通常情况下,Spring Security获取token的认证模式是基于http basic的,<br>
     * 也就是client的client_id和client_secret是通过http的header或者url模式传递的，<br>
     * 即通过http请求头的 Authorization传递，具体的请参考http basic<br>
     * 或者http://client_id:client_secret@server/oauth/token的模式传递的
     * </p>
     *
     * @param security security 的链式配置
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //启用表单参数：可以从表单获取client_id和client_secret信息
        security.allowFormAuthenticationForClients();
        //默认情况下，checkTokenAccess验证是denyAll（），手动开启
        security.checkTokenAccess("permitAll()");
        //默认情况下，tokenKeyAccess验证是denyAll（），手动开启
        security.tokenKeyAccess("permitAll()");
    }

    /**
     * <p>
     * 功能：配置 {@link ClientDetailsService} 例如：声明单个客户端及其属性；<br>
     * 注意：除非向{@link #configure(AuthorizationServerEndpointsConfigurer)}提供{@link AuthenticationManager},<br>
     * 否则不会启用密码授权（password grant）<br>
     * 必须声明至少一个的客户端或者格式完整的自定义{@link ClientDetailsService}，否则服务不会启动。
     * </p>
     *
     * @param clients 客户端详细信息配置
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //客户端详细信息，可自定义配置(数据库存储)
        clients.withClientDetails(clientDetailsService());
//        clients.inMemory()
//                .withClient("client")
//                .secret(passwordEncoder().encode("secret"))
////                .authorizedGrantTypes("password", "authorization_code", "client_credentials", "implicit", "refresh_token")
//                .authorizedGrantTypes("authorization_code")
//                .scopes("app")
//                .redirectUris("http://localhost:9010/eureka/");
//                是否启用自动授权，如果用自动授权，则不会弹出要求用户授权的页面
//                .autoApprove(false).autoApprove("cas");
    }

    /**
     * <p>
     * 配置授权服务器端点的非安全功能，<br>
     * 例如：token store（token存储）,自定义token，user approvals（用户批准），授权类型<br>
     * 除非想要启用password grant(密码授权)，需要注入{@link AuthenticationManager}<br>
     * 默认情况下，可以不做任何处理
     * </p>
     *
     * @param endpoints 端点配置
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        super.configure(endpoints);
        //启用密码模式
        endpoints.authenticationManager(authenticationManager)
                //数据库管理token
                .tokenStore(jdbcTokenStore())
                .userDetailsService(userDetailsManager)
                .accessTokenConverter(accessTokenConverter());
        endpoints.tokenServices(defaultTokenServices());
        //数据库管理授权码
        endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource));
        //数据库管理授权信息
        endpoints.approvalStore(new JdbcApprovalStore(dataSource));
        endpoints.exceptionTranslator(webResponseExceptionTranslator());
    }

    /**
     * 使用默认的accessTokenConverter,可扩展
     *
     * @return
     */
    @Bean
    public AccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

    /**
     * 基于内存的tokenstore，可扩展为基于数据库
     *
     * @return
     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore();
//    }

    /**
     * 基于redis的token store
     * @return
     */
//    @Bean
//    public RedisTokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }

    /**
     * 基于数据库的 token store
     *
     * @return
     */
    @Bean
    public JdbcTokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * jdbc 管理client detail
     *
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder());
        return jdbcClientDetailsService;
    }

    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setTokenStore(jdbcTokenStore());
        tokenServices.setClientDetailsService(clientDetailsService());
        //access_token 过期时间设置1天=24小时=86400s
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1));
        //refresh_token 过期时间设置30天=2592000s
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        return tokenServices;
    }

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new QbnWebResponseExceptionTranslator();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
