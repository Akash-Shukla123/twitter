package org.nov2024.Repo;

import org.nov2024.Models.ManageSubscription;
import org.nov2024.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "Select * from user where username = :username and password = :password", nativeQuery = true)
    Optional<User> findByUsernamePassword(String username, String password);

    @Query(value = "Select * from user where username = :username", nativeQuery = true)
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.username FROM user u " +
            "JOIN manage_user_roles ur ON u.userid = ur.userid " +
            "JOIN roles r ON ur.roleid = r.roleid " +
            "WHERE r.rolename = 'PRODUCER'",
            nativeQuery = true)
    List<String> getAllProducers();


}