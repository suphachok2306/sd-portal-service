package com.pcc.portalservice.config;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User user = userRepository
                .findById(Long.parseLong(userId, 10))
                .orElseThrow(() -> new UsernameNotFoundException("User: " + userId + " not found."));

        return org.springframework.security.core.userdetails.User
                .withUsername(userId)
                .password(user.getPassword())
                .authorities(user.getEmail())
                .authorities(getAuthorities(user.getRoles()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false).disabled(false)
                .build();
    }

    private List<GrantedAuthority> getAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
        }
        return authorities;
    }


}
