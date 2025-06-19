package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.CourseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    List<CourseSession> findByCourseId(Long courseId);
    List<CourseSession> findByIsDeletedFalse();
    List<CourseSession> findAllByStudents_Id(Long studentId);
    List<CourseSession> findByInstructorIdAndIsDeletedFalse(Long instructorId);
}
