package com.design.hook.dto;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Permits the storage and retrieving of a {@link RoleDTO} list.
 */
public class RoleDTOCollection {

    private Collection<RoleDTO> roles = new ArrayList<>();

    public Collection<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleDTO> roles) {
        this.roles = roles;
    }
}
