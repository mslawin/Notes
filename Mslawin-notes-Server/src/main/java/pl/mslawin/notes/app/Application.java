package pl.mslawin.notes.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import pl.mslawin.notes.config.MvcConfig;
import pl.mslawin.notes.config.RepositoryConfig;

@Configuration
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{MvcConfig.class, RepositoryConfig.class, Application.class}, args);
    }
}