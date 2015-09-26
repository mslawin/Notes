package pl.mslawin.notes.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import pl.mslawin.notes.config.AppConfig;

@Configuration
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{AppConfig.class, ServletContainerCustomizer.class}, args);
    }
}