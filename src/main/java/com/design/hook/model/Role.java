package com.design.hook.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * {@code roles} have a {@code @ManyToMany} mapping with {@code users}. {@code roles} are made up of a
 * {@code @ManyToMany} mapping of {@link RolePermission permissions}, which define {@code User} access rights to
 * different areas of the application via the controllers.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    /**
     * The type name of this {@link Role}. Must be unique - used to look up the {@code Role} in the database.
     */
    @Column(name = "role_type", unique = true, nullable = false)
    private String roleType;

    /**
     * {@link Role roles} are made up of a {@code @ManyToMany} mapping of {@link RolePermission permissions}.
     * Different {@code roles} can share the same predefined {@code permissions}. {@code permissions} are used
     * internally by spring.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_rolepermissions",
               uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "rolepermission_id"})},
               joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "rolepermission_id", referencedColumnName = "id")})
    private Set<RolePermission> permissions;

    public Role() {
    }

    public Role(final long id, final String roleType, final Set<RolePermission> permissions) {
        this.id = id;
        this.roleType = roleType;
        this.permissions = permissions;
    }

    public Role(final String roleType, final Set<RolePermission> permissions) {
        this.roleType = roleType;
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(final String roleType) {
        this.roleType = roleType;
    }

    public Set<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(final Collection<RolePermission> permissions) {
        this.permissions.clear();
        this.permissions.addAll(permissions);
    }

    public void addPermissions(final Collection<RolePermission> permissions) {
        this.permissions.addAll(new HashSet<>(permissions));
    }

    public void removePermissions(final Collection<RolePermission> permissions) {
        this.permissions.removeAll(permissions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(roleType, role.roleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleType);
    }
}
