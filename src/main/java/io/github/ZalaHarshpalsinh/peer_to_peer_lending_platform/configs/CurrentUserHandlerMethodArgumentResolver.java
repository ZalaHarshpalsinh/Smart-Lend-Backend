package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.configs;

import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.annotations.CurrentUser;
import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.entities.User;
import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.services.CustomUserDetailsService;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

@Component
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver
{
    private final CustomUserDetailsService customUserDetailsService;

    public CurrentUserHandlerMethodArgumentResolver(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // getting principal
        Principal principal = webRequest.getUserPrincipal();

        if( principal==null )
        {
            throw  new UsernameNotFoundException("User is not authenticated");
        }

        // return the User
        return ((SecurityUser)customUserDetailsService.loadUserByUsername(principal.getName())).getUser();
    }
}
