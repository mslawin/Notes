package pl.mslawin.notes.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "pl.mslawin.notes.dao")
@EnableAutoConfiguration
@EntityScan(basePackages = {"pl.mslawin.notes.domain.notes"})
public class RepositoryConfig {
}
