package org.nov2024.Repo;

import org.nov2024.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "Select * from roles where rolename = :name", nativeQuery = true)
    Optional<Role> findByName(String name);
}