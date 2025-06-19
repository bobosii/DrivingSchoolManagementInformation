package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType,Long> {
    Optional<AppointmentType> findByName(String name);
}
