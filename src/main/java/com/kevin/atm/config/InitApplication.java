package com.kevin.atm.config;

import com.kevin.atm.model.Account;
import com.kevin.atm.repository.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitApplication {
    private static final Logger log = LoggerFactory.getLogger(InitApplication.class);

    @Bean
    CommandLineRunner initDatabase(AccountRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Account(123456789,1234,800.00,200.00)));
            log.info("Preloading " + repository.save(new Account(987654321,4321,1230.00,150.00)));
        };
    }
}
