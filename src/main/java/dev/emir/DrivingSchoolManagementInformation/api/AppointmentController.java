package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.ApproveAppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.appointment.AppointmentCancelRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.appointment.AppointmentRejectRequets;
import dev.emir.DrivingSchoolManagementInformation.dto.request.appointment.AppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.AppointmentResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ModelMappings;
import dev.emir.DrivingSchoolManagementInformation.models.Appointment;
import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;
import dev.emir.DrivingSchoolManagementInformation.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@RequestBody AppointmentRequest request){
        Appointment created = appointmentService.createAppointment(request);

        AppointmentResponse responseData = ModelMappings.toAppointmentResponse(created);
        ApiResponse<AppointmentResponse> response = new ApiResponse<>(true,"Appointment created successfully", responseData);
        return ResponseEntity.ok(response);
    }

    // --------------------- UPDATE APPOINTMENT STATUS ------------------
    @PutMapping("/approve")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> approveAppointment(
            @RequestBody ApproveAppointmentRequest request
    ){
        Appointment appointment = appointmentService.approveAppointment(request.getAppointmentId(),request.getApproverId());
        AppointmentResponse responseData = ModelMappings.toAppointmentResponse(appointment);
        ApiResponse<AppointmentResponse> response = new ApiResponse<>(true,"Appointment approved successfully", responseData);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reject")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> rejectAppointment(
            @RequestBody AppointmentRejectRequets requets
            ){
        Appointment appointment = appointmentService.rejectAppointment(requets.getAppointmentId(), requets.getApproverId());
        AppointmentResponse responseData = ModelMappings.toAppointmentResponse(appointment);
        ApiResponse<AppointmentResponse> response = new ApiResponse<>(true,"Appointment rejected successfully", responseData);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(
            @RequestBody AppointmentCancelRequest request
            ){
        Appointment appointment = appointmentService.cancelAppointment(request.getAppointmentId(),request.getStudentId());
        AppointmentResponse responseData = ModelMappings.toAppointmentResponse(appointment);
        ApiResponse<AppointmentResponse>  response = new ApiResponse<>(true,"Appointment cancelled successfully",responseData);
        return ResponseEntity.ok(response);
    }
    // --------------------- UPDATE APPOINTMENT STATUS ------------------
    // --------------------- LIST APPOINTMENTS ------------------

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments(){
        List<Appointment> appointments = appointmentService.getAll();
        List<AppointmentResponse> appointmentResponses = appointments.stream().map(ModelMappings::toAppointmentResponse)
                .collect(Collectors.toList());
        ApiResponse<List<AppointmentResponse>> response = new ApiResponse<>(true,"Appointments listed successfully",appointmentResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByStudentId(
            @PathVariable("studentId") Long studentId
    ){
        List<Appointment> appointments = appointmentService.getByStudentId(studentId);
        List<AppointmentResponse> responseData = appointments.stream()
                .map(ModelMappings::toAppointmentResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true,"Appointment successfully listed by student id", responseData));
    }

    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentByInstructorId(
            @PathVariable("instructorId") Long instructorId
    ){
        List<Appointment> appointments = appointmentService.getByInstructorId(instructorId);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(ModelMappings::toAppointmentResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true,"Appointments successfully listed by instructor id",appointmentResponses));
    }

    @GetMapping("between")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsBetweenDates(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
            ){
        List<Appointment> appointments = appointmentService.getAppointmentsBetweenDates(start,end);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(ModelMappings::toAppointmentResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true,"Appointments between dates fetched",appointmentResponses));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentByStatus(
            @RequestParam("status")AppointmentStatus status
            ){
        List<Appointment> appointments = appointmentService.getAppointmentByStatus(status);
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(ModelMappings::toAppointmentResponse).toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Appointments fetched by status",appointmentResponses));
    }
    // --------------------- LIST APPOINTMENTS ------------------
}
