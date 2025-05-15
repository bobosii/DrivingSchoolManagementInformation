package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.AssignTheoreticalRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.studentCourseSession.StudentCourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.models.StudentCourseSession;
import dev.emir.DrivingSchoolManagementInformation.service.StudentCourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theoretical")
public class TheoreticalLessonController {
    @Autowired
    private final StudentCourseSessionService service;

    public TheoreticalLessonController(StudentCourseSessionService service) {
        this.service = service;
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<StudentCourseSessionResponse>> assignTheoretical(@RequestBody AssignTheoreticalRequest request){
        StudentCourseSession result = service.assignTheoreticalLesson(request.getStudentId(), request.getCourseSessionId(), request.getAssignerId());
        StudentCourseSessionResponse sessionResponse = new StudentCourseSessionResponse(result.getId(),result.getStudent().getId(), request.getCourseSessionId(), result.isApproved(),result.getAssignedAt(),result.getAssignedBy().getUsername(),result.getCourseSession().getCourse().getName());
        ApiResponse<StudentCourseSessionResponse> response = new ApiResponse<>(true,"Theoretical lesson created successfully",sessionResponse);
        return ResponseEntity.ok(response);
    }
}
