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
<<<<<<< HEAD
		filterRegistrationBean.addUrlPatterns("/api/forgotPassword/*");
=======
		filterRegistrationBean.addUrlPatterns("/campaign/register");
>>>>>>> 69649590c2b586a8994e9557143daa0630f8a9d5

		return filterRegistrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(AjudeApplication.class, args);
	}

}
