package pl.mslawin.notes.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pl.mslawin.notes.interceptor.AuthenticationInterceptor;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"pl.mslawin.notes.ws", "pl.mslawin.notes.service"})
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        registry.addInterceptor(new AuthenticationInterceptor());
    }
}