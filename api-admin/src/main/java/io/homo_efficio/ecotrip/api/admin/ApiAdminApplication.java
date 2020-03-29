package io.homo_efficio.ecotrip.api.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("io.homo_efficio.ecotrip.domain")
@EnableJpaRepositories("io.homo_efficio.ecotrip.domain")
public class ApiAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAdminApplication.class, args);
    }

}
