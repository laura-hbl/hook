package com.design.hook.repository;

import com.design.hook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link User} repository interface which provides methods that permit interaction with database.
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves {@link User} associated with the given username.
     *
     * @param username username of the {@link User}
     * @return {@link Optional <User>} or empty value.
     */
    Optional<User> findByUserName(final String username);
}
