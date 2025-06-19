package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
}
