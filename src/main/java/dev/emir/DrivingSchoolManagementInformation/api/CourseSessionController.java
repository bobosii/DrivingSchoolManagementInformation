package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseSessionRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ModelMappings;
import dev.emir.DrivingSchoolManagementInformation.models.CourseSession;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course-session")
public class CourseSessionController {
    @Autowired
    private final CourseSessionService courseSessionService;

    public CourseSessionController(CourseSessionService courseSessionService) {
        this.courseSessionService = courseSessionService;
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












