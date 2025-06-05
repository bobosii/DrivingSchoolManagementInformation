package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.CourseSessionRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseSessionRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ModelMappings;
import dev.emir.DrivingSchoolManagementInformation.models.CourseSession;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course-sessions")
public class CourseSessionController {
    private final CourseSessionRepository courseSessionRepository;
    private final CourseSessionService courseSessionService;

    @Autowired
    public CourseSessionController(CourseSessionRepository courseSessionRepository, CourseSessionService courseSessionService) {
        this.courseSessionRepository = courseSessionRepository;
        this.courseSessionService = courseSessionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<CourseSessionResponse>>> getAllCourseSessions() {
        List<CourseSession> courseSessions = courseSessionRepository.findAll();
        List<CourseSessionResponse> response = courseSessions.stream()
            .map(session -> new CourseSessionResponse(
                session.getId(),
                session.getCourse().getName(),
                session.getInstructor().getFirstName() + " " + session.getInstructor().getLastName(),
                session.getClassroom().getName(),
                session.getStartTime(),
                session.getEndTime()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Course sessions retrieved successfully", response));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> createCourseSession(@RequestBody CreateCourseSessionRequest request){
        CourseSession created = courseSessionService.createCourseSession(
                request.getCourseId(),
                request.getStartTime(),
                request.getInstructorId(),
                request.getClassroomId(),
                request.getEndTime()
        );

        ApiResponse<CourseSessionResponse> response = ModelMappings.getCourseSessionResponse(created);

        return ResponseEntity.ok(response);
    }
}












