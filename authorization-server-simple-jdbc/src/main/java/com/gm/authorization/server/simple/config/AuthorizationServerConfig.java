package com.gm.authorization.server.simple.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

// 开启认证服务
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	/**
	 * 数据源
	 */
	@Autowired
	private DataSource dataSource;

	/**
	 * 注入userDetailsService，开启refresh_token需要用到
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 注入权限验证控制器 来支持 password grant type
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * 基于Scope实现，将Scope授权情况存储在oauth_approvals表中，一般只有使用ApprovalStoreUserApprovalHandler策略采用使用这种存储方式
	 * 详情请见：https://blog.csdn.net/u013887008/article/details/82416652
	 * 
	 * @return
	 */
	@Bean
	public ApprovalStore approvalStore() {
		return new JdbcApprovalStore(dataSource);
	}

	/**
	 * 基于JDBC实现授权码存储和身份验证授权服务
	 * 
	 * @return
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	/**
	 * 基于JDBC 存储Token
	 * 
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	/**
	 * 基于JDBC 实配置独立的client客户端信息，包括授权范围、授权方式、客户端权限等。
	 * 授权方式包括password、implicit、client_redentials、authorization_code四种方式，
	 * 其中密码授权方式必须结合 AuthenticationManager 进行配置。
	 * 
	 * @return
	 */
	@Bean
	public JdbcClientDetailsService jdbcClientDetailsService() {
		return new JdbcClientDetailsService(dataSource);
	}

	/**
	 * 配置独立的client客户端信息，包括授权范围、授权方式、客户端权限等。
	 * 授权方式包括password、implicit、client_redentials、authorization_code四种方式，
	 * 其中密码授权方式必须结合 AuthenticationManager 进行配置
	 *
	 * @param clients
	 * @throws Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(jdbcClientDetailsService());
	}

	/**
	 * 配置授权服务器的Token存储方式、Token配置、授权模式
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// 开启密码授权类型
		endpoints.authenticationManager(authenticationManager) // 开启密码验证，来源于 WebSecurityConfigurerAdapter
				// token存储
				.tokenStore(tokenStore())
				// 授权请求存储
				.approvalStore(approvalStore())
				// 认证授权码存储
				.authorizationCodeServices(authorizationCodeServices())
				// 支持GET、POST请求
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				// 最后一个参数为替换之后页面的url
				.pathMapping("/oauth/confirm_access", "/custom/confirm_access")
				// 要使用refresh_token的话，需要额外配置userDetailsService，不配置会导致token无法刷新
				.userDetailsService(userDetailsService); // 读取验证用户的信息
	}

	/**
	 * 声明安全约束，允许那些请求可以访问和禁止访问
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients();

	}
}
