package com.gm.authorization.server.simple.memory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

// 开启认证服务
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 客户端 client
		clients.inMemory().withClient("client")
				// 客户端密钥 secret
				.secret(passwordEncoder.encode("secret"))
				// 授权类型，授权码和客户端凭证模式
				.authorizedGrantTypes("authorization_code", "refresh_token", "client_credentials")
				// 范围
				.scopes("write", "read")
				// 重定向地址
				.redirectUris("http://localhost:8080/login")
				// Token有效期和刷新时间
				.accessTokenValiditySeconds(120).refreshTokenValiditySeconds(60)
				// 客户端 test
				.and().withClient("test")
				// 客户端密钥 secret
				.secret(passwordEncoder.encode("secret"))
				// 授权类型，授权码
				.authorizedGrantTypes("authorization_code")
				// 范围
				.scopes("write", "read")
				// 重定向地址
				.redirectUris("http://localhost:8080/login");

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients();

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// 接收GET和POST
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
	}

}
