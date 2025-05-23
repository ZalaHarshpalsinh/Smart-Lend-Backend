package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.configs;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final RsaKeyProperties rsaKeys;

    private final UserDetailsService customUserDetailsService;

    // DI
    public SecurityConfig(UserDetailsService customUserDetailsService, RsaKeyProperties rsaKeys)
    {
        this.customUserDetailsService = customUserDetailsService;
        this.rsaKeys = rsaKeys;
    }

    // bean for custom authentication manager
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder)
    {
        // new authentication provider
        var authProvider = new DaoAuthenticationProvider();
        // injecting my custom user details service
        authProvider.setUserDetailsService(userDetailsService);
        // injecting password encoder
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    // security config bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // need to disable session and csrf since stateless
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    // all requests authenticated for now
                    auth
                    .requestMatchers(HttpMethod.POST, "/demo/fileUpload").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/verifyOtp").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/sendOtp").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    // to throw custom status codes and errors
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .anyRequest().authenticated();
                })
                // for jwt
                .oauth2ResourceServer(
                        oauth2ResourceServer->
                                oauth2ResourceServer.jwt(
                                        jwt->jwt.decoder(jwtDecoder())
                                )
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // bean for decoding jwts
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    // for encoding
    @Bean
    public JwtEncoder jwtEncoder()
    {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        NimbusJwtEncoder nje = new NimbusJwtEncoder(jwks);
        return nje;
    }

    // bean for password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
