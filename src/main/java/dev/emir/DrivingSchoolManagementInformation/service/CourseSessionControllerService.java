package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentResponse;

import java.util.List;
// Design Pattern: Service Layer Pattern
public interface CourseSessionControllerService {
    
    /**
     * Gets course sessions for a specific instructor
     * @param userId User ID from authentication
     * @return List of CourseSessionResponse objects
     */
    List<CourseSessionResponse> getMySessions(Long userId);
    
    /**
     * Gets unassigned students for course sessions
     * @param termId Optional term ID to filter students
     * @return List of StudentResponse objects
     */
    List<StudentResponse> getUnassignedStudents(Long termId);
} 