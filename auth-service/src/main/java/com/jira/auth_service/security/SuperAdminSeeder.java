package com.jira.auth_service.security;

import com.jira.auth_service.entity.User;
import com.jira.auth_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SuperAdminSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminSeeder(UserRepository userRepo,
                            PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String email = "superadmin@jira.com";

        if (userRepo.findByEmail(email).isEmpty()) {
            User admin = User.builder()
                    .email(email)
//                    .passwordHash(passwordEncoder.encode("SuperAdmin@123"))
                    .roles(Set.of("SUPER_ADMIN"))
                    .isVerified(true)
                    .isActive(true)
                    .build();

            userRepo.save(admin);

            System.out.println("✅ SUPER ADMIN CREATED");
        }
    }
}
