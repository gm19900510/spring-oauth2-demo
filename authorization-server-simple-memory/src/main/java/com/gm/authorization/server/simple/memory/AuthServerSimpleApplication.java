package com.gm.authorization.server.simple.memory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AuthServerSimpleApplication {

	/**
	 * 配置密码加密，springboot2.x需要配置
	 *
	 * @return
	 */
	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
    public static void main(String[] args) {
        SpringApplication.run(AuthServerSimpleApplication.class, args);
    }

}
