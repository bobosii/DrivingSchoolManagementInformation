package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.LicenseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface LicenseClassRepository extends JpaRepository<LicenseClass, Long> {
    boolean existsByCode(String code);
    Optional<LicenseClass> findByCode(String code);
} 