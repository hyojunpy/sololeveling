package com.example.sololeveling.global.config.auth;

import com.example.sololeveling.domain.user.entity.User;
import com.example.sololeveling.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserRepository userRepository; // JPA Repository

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserDetailsImpl(
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole().name()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
