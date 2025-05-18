package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
}
