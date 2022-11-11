package org.launchcode.codingevents;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Flags this class to Spring as one that contains configuration code.
public class WebApplicationConfig implements WebMvcConfigurer {
    // By implementing WebMvcConfigurer, we ensure that Spring will call our addInterceptors method during startup,
    // giving us the chance to register our filter.

    // Create spring-managed object to allow the app to access our filter
    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    // Register the filter with the Spring container
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( authenticationFilter() );
    }

}
