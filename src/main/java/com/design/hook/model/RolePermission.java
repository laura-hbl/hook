package com.design.hook.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Every {@link User} can have a one or many {@link Role roles}, and each {@code Role} may have many
 * {@code permissions}. {@code permissions} allow us to identify whether {@code User} has access to
 * different parts of the application.
 */
@Entity
@Table(name = "rolepermissions")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    /**
     * The type name of this {@link Role}. Must be unique - used to look up the {@code RolePermission} in the database.
     */
    @Column(name = "permission", unique = true, nullable = false)
    private String permission;

    public RolePermission(final long id, final String permission) {
        this.id = id;
        this.permission = permission;
    }

    public RolePermission() {

    }

    public RolePermission(final String permission) {
        this.permission = permission;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setPermission(final String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermission rolePermission = (RolePermission) o;
        return Objects.equals(id, rolePermission.id) && Objects.equals(permission, rolePermission.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permission);
    }
}
