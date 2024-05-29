package com.design.hook.dto;

/**
 * Permits the storage and retrieving of a {@link RolePermissionDTO}.
 */
public class RolePermissionDTO {

    private long id;

    private String permission;

    public RolePermissionDTO(final long id, final String permission) {
        this.id = id;
        this.permission = permission;
    }

    public RolePermissionDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(final String permission) {
        this.permission = permission;
    }
}
