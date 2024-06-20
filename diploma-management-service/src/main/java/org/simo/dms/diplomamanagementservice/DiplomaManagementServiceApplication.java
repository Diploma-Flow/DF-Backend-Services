package org.simo.dms.diplomamanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DiplomaManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomaManagementServiceApplication.class, args);
    }

}
