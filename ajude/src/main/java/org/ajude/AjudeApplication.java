package org.ajude;

import org.ajude.filters.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AjudeApplication {

	@Bean
	public FilterRegistrationBean<TokenFilter> filterJwt() {
		FilterRegistrationBean<TokenFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new TokenFilter());
		filterRegistrationBean.addUrlPatterns("");

		return filterRegistrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(AjudeApplication.class, args);
	}

}
