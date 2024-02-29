package com.example.petparadisebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class PetParadiseBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetParadiseBeApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.saveRole(new Role(null, "ROLE_ADMIN"));
//            userService.saveRole(new Role(null, "ROLE_USER"));
//            userService.saveRole(new Role(null, "ROLE_MANAGER"));
//            userService.saveUser(new User("DanBui", "Bui Nguyen Dan", "admin@gmail.com", "12345", "Nam Dinh", "0869361730", new HashSet<>()));
//            userService.saveUser(new User("DatPham", "Pham Thanh Dat", "user@gmail.com", "12345", "Hai Duong", "086001730", new HashSet<>()));
//
//            userService.addToUser("admin@gmail.com", "ROLE_ADMIN");
//            userService.addToUser("admin@gmail.com", "ROLE_MANAGER");
//            userService.addToUser("user@gmail.com", "ROLE_USER");
//        };
//    }
}

