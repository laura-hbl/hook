package com.design.hook.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Every {@link User} can have one or many {@link Role roles}, and each {@code Role} may have many
 * {@code permissions}. Each user has a unique username.
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class  User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    /**
     * {@code User} username, which must be unique. This is used in many areas to find {@code User} in the database.
     */
    @Column(name = "username")
    private String userName;


    /**
     * The user password which is fully encrypted.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * {@code users} are granted access to different areas of the application depending on their assigned
     * {@link Role roles}. These are loaded each time a user logs in.
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(String)
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})},
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id", nullable = false)})
    private Set<Role> roles;


    /**
     * Indicates whether the user is enabled or not.
     */
    @Column(name = "enabled")
    private Boolean enabled = true;


    public User(final long id, final String userName, final String password, final Set<Role> roles,
                final Boolean enabled) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
    }

    public User(final String userName, final String password, final Set<Role> roles, final Boolean enabled) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
    }

    public User() {
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }


//    public void addRoles(Collection<Role> roles) {
//        this.roles.addAll(roles);
//    }
//
    public void setRoles(Collection<Role> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }

    public void removeRoles(Collection<Role> roles) {

        this.roles.removeAll(roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName);
    }
}
