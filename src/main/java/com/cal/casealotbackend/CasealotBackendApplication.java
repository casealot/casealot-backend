package com.cal.casealotbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CasealotBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasealotBackendApplication.class, args);
    }

}
