package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver;

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    public WebConfig(CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver) {
        this.currentUserHandlerMethodArgumentResolver = currentUserHandlerMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserHandlerMethodArgumentResolver);
    }

    // cors headers
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin, "http://192.168.1.7:5173").allowedMethods("GET", "POST", "PUT", "DELETE");
    }

}
