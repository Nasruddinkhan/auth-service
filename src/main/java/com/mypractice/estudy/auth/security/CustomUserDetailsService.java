package com.mypractice.estudy.auth.security;


import com.mypractice.estudy.auth.exception.ResourceNotFoundException;
import com.mypractice.estudy.auth.repository.UserRepository;
import com.mypractice.estudy.auth.security.oauth2.user.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by nasruddin khan
 */

@Service
@Transactional
public record CustomUserDetailsService(
        UserRepository userRepository) implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        var users = userRepository.findByEmail(email);
        if (!users.isPresent())
            throw new ResourceNotFoundException("User not found with email : " + email);
        return users.map(UserPrincipal::create).orElseThrow(() -> new UsernameNotFoundException("Invalid credential "));
    }


    public UserDetails loadUserById(final String id) {
        return UserPrincipal.create(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with email : " + id)));
    }
}