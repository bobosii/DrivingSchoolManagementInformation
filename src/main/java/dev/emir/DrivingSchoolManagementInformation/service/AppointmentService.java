package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.*;
import dev.emir.DrivingSchoolManagementInformation.dto.request.appointment.AppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.models.*;
import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    private final StudentRepository studentRepository;
    private final AppointmentRepository appointmentRepository;
    private final InstructorRepository instructorRepository;
    private final CourseSessionRepository courseSessionRepository;
    private final AppointmentTypeRepository appointmentTypeRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppointmentService(
            StudentRepository studentRepository,
            AppointmentRepository appointmentRepository,
            InstructorRepository instructorRepository,
            CourseSessionRepository courseSessionRepository,
            AppointmentTypeRepository appointmentTypeRepository,
            UserRepository userRepository
    ) {
        this.studentRepository = studentRepository;
        this.appointmentRepository = appointmentRepository;
        this.instructorRepository = instructorRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.appointmentTypeRepository = appointmentTypeRepository;
        this.userRepository = userRepository;
    }

    public Appointment createAppointment(AppointmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));
        AppointmentType type = appointmentTypeRepository.findById(request.getAppointmentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment type not found"));

        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setInstructor(instructor);
        appointment.setAppointmentType(type);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setRequestedAt(LocalDateTime.now());

        if (request.getCourseSessionId() != null) {
            CourseSession courseSession = courseSessionRepository.findById(request.getCourseSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Course session not found"));
            appointment.setCourseSession(courseSession);
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment approveAppointment(Long appointmentId, Long approverId) {
        // Check if approver is admin
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        if (!approver.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can approve appointments");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Appointment is not in a pending state");
        }

        appointment.setStatus(AppointmentStatus.APPROVED);
        appointment.setApprovedAt(LocalDateTime.now());
        appointment.setApprovedBy(approver);

        return appointmentRepository.save(appointment);
    }

    public Appointment rejectAppointment(Long appointmentId, Long rejectedById) {
        // Check if rejecter is admin
        User rejecter = userRepository.findById(rejectedById)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        if (!rejecter.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can reject appointments");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING appointments can be rejected.");
        }

        appointment.setStatus(AppointmentStatus.REJECTED);
        appointment.setApprovedAt(LocalDateTime.now());
        appointment.setApprovedBy(rejecter);

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Long appointmentId, Long studentId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (!appointment.getStudent().getId().equals(studentId)){
            throw new AccessDeniedException("You are not authorized");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING){
            throw new IllegalArgumentException("Only PENDING appointments can be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long appointmentId, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (request.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));
            appointment.setInstructor(instructor);
        }

        if (request.getAppointmentTypeId() != null) {
            AppointmentType type = appointmentTypeRepository.findById(request.getAppointmentTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Appointment type not found"));
            appointment.setAppointmentType(type);
        }

        if (request.getAppointmentTime() != null) {
            appointment.setAppointmentTime(request.getAppointmentTime());
        }

        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
            if (request.getStatus() == AppointmentStatus.APPROVED || request.getStatus() == AppointmentStatus.REJECTED) {
                appointment.setApprovedAt(LocalDateTime.now());
            }
        }

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            throw new IllegalStateException("Cannot delete a pending appointment");
        }

        appointmentRepository.delete(appointment);
    }

    // --------------------- LIST APPOINTMENTS ------------------

    public List<Appointment> getAll(){
        return appointmentRepository.findAll();
    }

    public List<Appointment> getByStudentId(Long studentId){
        return appointmentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("No appointments found for student with ID: " + studentId));
    }

    public List<Appointment> getByInstructorId(Long instructorId){
        return appointmentRepository.findByInstructorId(instructorId)
                .orElseThrow(() -> new RuntimeException("No appointments found for instructor with ID: " + instructorId));
    }

    public List<Appointment> getAppointmentsBetweenDates(LocalDateTime start, LocalDateTime end){
        return appointmentRepository.findByAppointmentTimeBetween(start,end)
                .orElseThrow(() -> new IllegalArgumentException("No appointments found between these dates"));
    }

    public List<Appointment> getAppointmentByStatus(AppointmentStatus status){
        return appointmentRepository.findByStatus(status)
                .orElseThrow(() -> new IllegalArgumentException("No appointments found have this status"));
    }
}
