package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {
    boolean existsByName(String name);
} 