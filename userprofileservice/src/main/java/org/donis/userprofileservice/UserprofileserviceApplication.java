package org.donis.userprofileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("org.donis")
@EntityScan(basePackages = "org.donis.models.entities")
@EnableJpaRepositories("org.donis")
public class UserprofileserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserprofileserviceApplication.class, args);
    }

}
