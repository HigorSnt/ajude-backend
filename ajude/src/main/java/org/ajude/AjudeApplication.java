package org.ajude;

import org.ajude.filters.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class AjudeApplication {

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<TokenFilter> filterJwt() {
        FilterRegistrationBean<TokenFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new TokenFilter());

        filterRegistrationBean.addUrlPatterns("/forgotPassword/*", "/campaign/*");

        return filterRegistrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(AjudeApplication.class, args);
    }

}
