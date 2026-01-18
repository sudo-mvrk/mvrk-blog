package dev.mvrk.blog.init;

import dev.mvrk.blog.entity.User;
import dev.mvrk.blog.entity.enums.Role;
import dev.mvrk.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.admin.username:admin}")
    private String adminUsername;
    @Value("${application.admin.password:admin}")
    private String adminPassword;
    @Value("${application.admin.email:admin@blog.com}")
    private String adminEmail;
    @Value("${application.admin.nickname}")
    private String nickname;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsUserByUsername(adminUsername)) {
            User user = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .nickname(nickname)
                    .role(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN))
                    .isBanned(false)
                    .build();
            userRepository.save(user);
            log.info("ADMIN CREATED. Login: {}, Password: [HIDDEN]", adminUsername);
        }
    }
}
