package pl.mslawin.notes.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"pl.mslawin.notes.ws", "pl.mslawin.notes.service"})
public class MvcConfig {
}
