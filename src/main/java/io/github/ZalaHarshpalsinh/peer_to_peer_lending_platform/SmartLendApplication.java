package io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform;

import io.github.ZalaHarshpalsinh.peer_to_peer_lending_platform.configs.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SmartLendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartLendApplication.class, args);
	}

	@RequestMapping
	public String hello() {
		return "Hello World";
	}
}
