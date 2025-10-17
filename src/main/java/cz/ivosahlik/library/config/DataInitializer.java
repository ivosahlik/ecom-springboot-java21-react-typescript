package cz.ivosahlik.library.config;

import cz.ivosahlik.library.dao.UserRepository;
import cz.ivosahlik.library.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@example.com")
                    .role("admin")
                    .build();
            userRepository.save(adminUser);
        }

        // Create a regular test user if it doesn't exist
        if (!userRepository.existsByUsername("user")) {
            User regularUser = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .email("user@example.com")
                    .role("user")
                    .build();
            userRepository.save(regularUser);
        }
    }
}
