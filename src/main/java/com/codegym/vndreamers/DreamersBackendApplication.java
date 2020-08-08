package com.codegym.vndreamers;

import com.codegym.vndreamers.enums.EnumRole;
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
                Set<EnumRole> roleSet = EnumSet.of(EnumRole.ADMIN, EnumRole.USER);
                for (EnumRole role : roleSet) {
                    try {
                        roleRepository.findByEnumRole(role);
                    } catch (EntityNotFoundException e) {
                        Role roleAdd = new Role(role);
                        roleRepository.save(roleAdd);
                    }
                }
            }
        };
    }
}
