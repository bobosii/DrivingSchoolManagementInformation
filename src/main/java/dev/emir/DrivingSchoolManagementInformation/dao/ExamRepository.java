package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Design Pattern: Repository (DAO) Pattern
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
} 