package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.ApproveAppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.studentCourseSession.StudentCourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.models.StudentCourseSession;
import dev.emir.DrivingSchoolManagementInformation.service.StudentCourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentApprovalController {
    @Autowired
    private final StudentCourseSessionService service;

    public AppointmentApprovalController(StudentCourseSessionService service) {
        this.service = service;
    }

    @PutMapping("/approve")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<StudentCourseSessionResponse>> approveAppointment(
            @RequestBody ApproveAppointmentRequest request
            ){
        StudentCourseSession result = service.approveAppointment(request.getAppointmentId(), request.getApproverId());

        StudentCourseSessionResponse responseData = new StudentCourseSessionResponse(
                result.getId(),
                result.getStudent().getId(),
                result.getCourseSession().getId(),
                result.isApproved(),
                result.getAssignedAt(),
                result.getAssignedBy().getUsername(),
                result.getCourseSession().getCourse().getName()
        );

        ApiResponse<StudentCourseSessionResponse> response =
                new ApiResponse<>(true,"Appointment approved successfully",responseData);

        return ResponseEntity.ok(response);
    }
}
