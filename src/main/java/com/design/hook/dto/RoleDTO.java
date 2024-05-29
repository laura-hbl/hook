package com.design.hook.dto;

import java.util.Collection;

/**
 * Permits the storage and retrieving data of a {@link RoleDTO}.
 */
public class RoleDTO {

    private long id;

    private String roleType;

    private Collection<RolePermissionDTO> permissions;

    public RoleDTO(final long id, final String roleType, final Collection<RolePermissionDTO> permissions) {
        this.id = id;
        this.roleType = roleType;
        this.permissions = permissions;
    }

    public RoleDTO() {
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

    public Collection<RolePermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(final Collection<RolePermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
