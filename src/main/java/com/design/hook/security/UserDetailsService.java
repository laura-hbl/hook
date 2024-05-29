package com.design.hook.security;
import com.design.hook.model.Role;
import com.design.hook.model.RolePermission;
import com.design.hook.model.User;
import com.design.hook.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Constructs the {@link User} that is to be managed by spring. All {@link Role userRoles} and {@link RolePermission
 * rolePermissions} are loaded and the {@code permissions} are placed in the user granted authorities, which is used
 * internally by spring.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final IUserRepository userRepository;

    @Autowired
    public UserDetailsService(final IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * All permissions are placed in the user granted authorities, which is used internally by spring.
     *
     * @param permissions list of {@code RolePermission} in string format.
     * @return list of the user granted authorities
     */
    private Collection<SimpleGrantedAuthority> getGrantedAuthorities(final Collection<String> permissions) {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Loops through all {@link RolePermission permissions} in all {@link Role userRoles} and flattens it to a
     * list containing the string representation of the enum.
     *
     * @param roles list of {@link Role userRoles}.
     * @return list of {@code RolePermission} in string format.
     */
    private Collection<String> getRolesAndPermissions(final Collection<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getPermission())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the {@link User} associated with the given username, and if it exists, loads user details and wrap
     * it into a UserDetails object. Else, UsernameNotFoundException is thrown.
     *
     * @param username the user's username
     * @return UserDetails instance
     */
    @Override
    public UserDetails loadUserByUsername(final String username) {

        User user = userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("Invalid username or password"));

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
                getGrantedAuthorities(getRolesAndPermissions(user.getRoles())));
    }
}
