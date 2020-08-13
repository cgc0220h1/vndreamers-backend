package com.codegym.vndreamers;

import com.codegym.vndreamers.enums.RoleName;
import com.codegym.vndreamers.models.Role;
import com.codegym.vndreamers.repositories.RoleRepository;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityNotFoundException;
import java.util.EnumSet;
import java.util.Set;

@SpringBootApplication
public class DreamersBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamersBackendApplication.class, args);
    }

    @Bean
    public ApplicationRunner initializer(RoleRepository roleRepository) {
        return new ApplicationRunner() {
            @SneakyThrows
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Set<RoleName> roleSet = EnumSet.of(RoleName.ADMIN, RoleName.USER);
                for (RoleName role : roleSet) {
                    try {
                        roleRepository.findByRoleName(role);
                    } catch (EntityNotFoundException e) {
                        Role roleAdd = new Role(role);
                        roleRepository.save(roleAdd);
                    }
                }
            }
        };
    }
}
