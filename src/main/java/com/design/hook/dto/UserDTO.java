package com.design.hook.dto;


import java.util.Collection;

/**
 * Permits the storage and retrieving data of a{@link UserDTO}.
 */
public class UserDTO {

    private long id;

    private String userName;

    private String password;

    private Collection<RoleDTO> roles;

    private Boolean enabled = true;

    public UserDTO(final long id, final String userName, final Collection<RoleDTO> roles, final Boolean enabled) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
        this.enabled = enabled;
    }

    public UserDTO(final String userName, final Collection<RoleDTO> roles, final Boolean enabled) {
        this.userName = userName;
        this.roles = roles;
        this.enabled = enabled;
    }

    public UserDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Collection<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(final Collection<RoleDTO> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }
}
