package com.bestbenefits.takoyaki.config;

import com.bestbenefits.takoyaki.filter.TestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<TestFilter> testFilter() {
        FilterRegistrationBean<TestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TestFilter());
        registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴 설정
        registrationBean.setOrder(1); // 필터의 순서 설정
        return registrationBean;
    }
}