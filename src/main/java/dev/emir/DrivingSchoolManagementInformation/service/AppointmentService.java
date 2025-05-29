package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.*;
import dev.emir.DrivingSchoolManagementInformation.dto.request.appointment.AppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.models.*;
import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;
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

    @Autowired
    public AppointmentService(StudentRepository studentRepository, AppointmentRepository appointmentRepository, InstructorRepository instructorRepository, CourseSessionRepository courseSessionRepository, AppointmentTypeRepository appointmentTypeRepository) {
        this.studentRepository = studentRepository;
        this.appointmentRepository = appointmentRepository;
        this.instructorRepository = instructorRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.appointmentTypeRepository = appointmentTypeRepository;
    }


    public Appointment createAppointment(AppointmentRequest request){
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));
        CourseSession session = courseSessionRepository.findById(request.getCourseSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Course session not found"));
        AppointmentType type = appointmentTypeRepository.findById(request.getAppointmentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment type not found"));

        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setInstructor(instructor);
        appointment.setCourseSession(session);
        appointment.setAppointmentType(type);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.PENDING); // default
        appointment.setRequestedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }

    public Appointment approveAppointment(Long appointmentId, Long approverId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Appointment is not in a pending state");
        }

        appointment.setStatus(AppointmentStatus.APPROVED);
        appointment.setApprovedAt(LocalDateTime.now());

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

    public Appointment rejectAppointment(Long appointmentId, Long rejectedById){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING appointments can be rejected.");
        }

        appointment.setStatus(AppointmentStatus.REJECTED);
        appointment.setApprovedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }

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

}
