package com.design.hook.dto;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Permits the storage and retrieving of a {@link RolePermissionDTO} list.
 */
public class RolePermissionDTOCollection {

    private Collection<RolePermissionDTO> permissions = new ArrayList<>();

    public Collection<RolePermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(final Collection<RolePermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
