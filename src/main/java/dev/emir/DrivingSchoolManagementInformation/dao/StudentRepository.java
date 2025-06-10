package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByUserUsername(String username);
    Optional<Student> findByUser(User user);
    List<Student> findByTermIsNull();
    List<Student> findByCourseSessionsIsEmpty();
    List<Student> findByTermIdAndCourseSessionsIsEmpty(Long termId);
}
