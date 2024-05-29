package com.design.hook.util;

import com.design.hook.dto.PatternDTO;
import com.design.hook.dto.RoleDTO;
import com.design.hook.dto.RolePermissionDTO;
import com.design.hook.dto.UserDTO;
import com.design.hook.model.Pattern;
import com.design.hook.model.Role;
import com.design.hook.model.RolePermission;
import com.design.hook.model.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

/**
 * Allows the conversion of model class to dto class.
 */
@Component
public class DTOConverter {


    public static PatternDTO toPatternDTO(final Pattern pattern) {

        PatternDTO patternDTO = new PatternDTO();
        patternDTO.setId(pattern.getId());
        patternDTO.setTitle(pattern.getTitle());
        patternDTO.setDescription(pattern.getDescription());
        patternDTO.setLikes(pattern.getLikes());
        patternDTO.setLevel(pattern.getLevel());
        patternDTO.setImageUrl(pattern.getImageUrl());
        patternDTO.setCreatedDate(pattern.getCreatedDate());

        return patternDTO;
    }

    /**
     * Converts {@link User} to {@link UserDTO}.
     *
     * @param user {@link User} object to convert
     * @return The {@link UserDTO} object
     */
    public static UserDTO toUserDTO(final User user) {

        return new UserDTO(user.getId(),
                user.getUserName(),
                user.getRoles().stream()
                        .map(DTOConverter::toRoleDTO)
                        .collect(Collectors.toList()),
                user.getEnabled());
    }

    /**
     * Converts {@link Role} to {@link RoleDTO}.
     *
     * @param role {@link Role} object to convert
     * @return The {@link RoleDTO} object
     */
    public static RoleDTO toRoleDTO(final Role role) {

        return new RoleDTO(
                role.getId(),
                role.getRoleType(),
                role.getPermissions().stream()
                        .map(DTOConverter::toRolePermissionDTO)
                        .collect(Collectors.toSet()));
    }

    /**
     * Converts {@link RolePermission} to {@link RolePermissionDTO}.
     *
     * @param rolePermission {@link RolePermission} object to convert
     * @return The {@link RolePermissionDTO} object
     */
    public static RolePermissionDTO toRolePermissionDTO(final RolePermission rolePermission) {

        return new RolePermissionDTO(rolePermission.getId(), rolePermission.getPermission());
    }
}

