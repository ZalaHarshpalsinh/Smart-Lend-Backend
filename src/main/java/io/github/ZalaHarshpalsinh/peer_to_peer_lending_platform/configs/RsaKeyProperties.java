package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(
    RSAPublicKey publicKey,
    RSAPrivateKey privateKey
) {
}
