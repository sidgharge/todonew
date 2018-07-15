package com.bridgelabz.todo.user.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().and().authorizeRequests().antMatchers("/actuator/**", "/api-docs/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/notes/**").hasAuthority("USER")
				.antMatchers("/**").permitAll().anyRequest().authenticated();
	}
}
