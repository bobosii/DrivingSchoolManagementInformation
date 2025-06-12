package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentCourseSessionController {

    private final CourseSessionService courseSessionService;

    @Autowired
    public StudentCourseSessionController(CourseSessionService courseSessionService) {
        this.courseSessionService = courseSessionService;
    }

    @GetMapping("/course-sessions")
    public ResponseEntity<ApiResponse<List<CourseSessionResponse>>> getMySessions(Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        List<CourseSessionResponse> sessions = courseSessionService.getSessionsForStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success(sessions));
    }
} 