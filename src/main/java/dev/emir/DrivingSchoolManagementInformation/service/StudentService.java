package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentAppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDetailResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDashboardResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.studentCourseSession.StudentCourseSessionResponse;

import java.util.List;

public interface StudentService {
    
    /**
     * Registers a new student
     * @param request Student registration request
     * @return StudentRegisterResponse with created student details
     */
    StudentRegisterResponse registerStudent(StudentRegisterRequest request);
    
    /**
     * Gets student profile by authentication
     * @param username Username from authentication
     * @return StudentProfileResponse with student profile details
     */
    StudentProfileResponse getStudentProfile(String username);
    
    /**
     * Creates an appointment for a student
     * @param request Student appointment request
     * @return StudentCourseSessionResponse with appointment details
     */
    StudentCourseSessionResponse createAppointment(StudentAppointmentRequest request);
    
    /**
     * Gets detailed information about a student
     * @param id Student ID
     * @return StudentDetailResponse with student details and appointments
     */
    StudentDetailResponse getStudentDetails(Long id);
    
    /**
     * Gets all students
     * @return List of StudentDetailResponse objects
     */
    List<StudentDetailResponse> getAllStudents();
    
    /**
     * Gets student dashboard data
     * @param userId User ID from authentication
     * @return StudentDashboardResponse with dashboard statistics
     */
    StudentDashboardResponse getStudentDashboard(Long userId);
    
    /**
     * Checks if the current user is the specified student
     * @param studentId Student ID to check
     * @param username Current user's username
     * @return true if current user is the specified student
     */
    boolean isCurrentUser(Long studentId, String username);
} 