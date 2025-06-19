package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.Employee;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByUserUsername(String username);
    Optional<Employee> findByUser(User user);
}
