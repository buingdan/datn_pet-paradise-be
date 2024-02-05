package com.example.petparadisebe;

import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

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
//            userService.saveRole(new Role(null, "ROLE_USER"));
//            userService.saveRole(new Role(null, "ROLE_ADMIN"));
//            userService.saveUser(new User("DatPham", "Pham Thanh Dat", "datpham@gmail.com", "12345", "Hai Duong", "086001730", new HashSet<>()));
//            userService.saveUser(new User("DanBui", "Bui Nguyen Dan", "danbui@gmail.com", "12345", "Nam Dinh", "0869361730", new HashSet<>()));
//
//            userService.addToUser("danbui@gmail.com", "ROLE_ADMIN");
//            userService.addToUser("datpham@gmail.com", "ROLE_USER");
//        };
//    }
}

