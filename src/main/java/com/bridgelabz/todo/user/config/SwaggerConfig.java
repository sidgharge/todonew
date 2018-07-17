package com.bridgelabz.todo.user.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().securitySchemes(Arrays.asList(apiKey()));
	}

	@Bean
	public SecurityConfiguration security() {
		return new SecurityConfiguration("client", "clientSecret", "realm", "appName", "Bearer ", ApiKeyVehicle.HEADER,
				"Authorization", ",");
	}
	
	private ApiKey apiKey() {
		return new ApiKey("authkey", "Authorization", "header");
	}

}
