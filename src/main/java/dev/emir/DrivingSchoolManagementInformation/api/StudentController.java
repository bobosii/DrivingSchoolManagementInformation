package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentAppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDetailResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDashboardResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.studentCourseSession.StudentCourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/student")
public class StudentController {
    
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<StudentRegisterResponse>> registerStudent(@RequestBody StudentRegisterRequest request) {
        try {
            StudentRegisterResponse responseData = studentService.registerStudent(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Student registered successfully", responseData));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error registering student: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getStudentProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            StudentProfileResponse profile = studentService.getStudentProfile(username);
            return ResponseEntity.ok(new ApiResponse<>(true, "Profile information retrieved successfully", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving profile: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/appointment")
    public ResponseEntity<ApiResponse<StudentCourseSessionResponse>> createAppointment(
            @RequestBody StudentAppointmentRequest request
    ) {
        try {
            StudentCourseSessionResponse responseData = studentService.createAppointment(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Appointment created successfully", responseData));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error creating appointment: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE') or (hasRole('STUDENT') and @studentController.isCurrentUser(#id))")
    public ResponseEntity<ApiResponse<StudentDetailResponse>> getStudentDetails(@PathVariable Long id) {
        try {
            StudentDetailResponse response = studentService.getStudentDetails(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Student details retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving student details: " + e.getMessage(), null));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<StudentDetailResponse>>> getAllStudents() {
        try {
            List<StudentDetailResponse> studentResponses = studentService.getAllStudents();
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenciler başarıyla getirildi", studentResponses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving students: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<StudentDashboardResponse>> getStudentDashboard(Authentication authentication) {
        try {
            Long userId = ((Long) authentication.getPrincipal());
            StudentDashboardResponse dto = studentService.getStudentDashboard(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Student dashboard data retrieved successfully", dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving dashboard: " + e.getMessage(), null));
        }
    }

    public boolean isCurrentUser(Long studentId) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        return studentService.isCurrentUser(studentId, username);
    }
}
