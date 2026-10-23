
package com.mergemesh.backend.auth;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mergemesh.backend.user.User;
import com.mergemesh.backend.user.UserRepository;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String displayName = request.displayName().trim();
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException();
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(displayName, email, passwordHash);
        User savedUser = userRepository.saveAndFlush(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getDisplayName(),
                savedUser.getEmail()
        );
    }
}
