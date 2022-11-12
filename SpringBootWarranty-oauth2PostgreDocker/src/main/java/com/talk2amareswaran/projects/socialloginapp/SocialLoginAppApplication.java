package com.talk2amareswaran.projects.socialloginapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.talk2amareswaran.projects.socialloginapp.config.SocialConfig;
import com.talk2amareswaran.projects.socialloginapp.config.WebSecurityConfig;

@SpringBootApplication
@Import({SocialConfig.class,WebSecurityConfig.class})
public class SocialLoginAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialLoginAppApplication.class, args);
	}
}
