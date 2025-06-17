package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.StudentCourseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseSessionRepository extends JpaRepository<StudentCourseSession, Long> {
    List<StudentCourseSession> findByStudentId(Long studentId);
    long countByStudent_Id(Long studentId);

}
