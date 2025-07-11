package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameIgnoreCase(String username);
    long countByRole(Role role);
}
