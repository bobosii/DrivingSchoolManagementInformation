package dev.emir.DrivingSchoolManagementInformation.dao;

import dev.emir.DrivingSchoolManagementInformation.models.Appointment;
import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    Optional<List<Appointment>> findByStudentId(Long studentId);
    Optional<List<Appointment>> findByInstructorId(Long instructorId);
    Optional<List<Appointment>> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
    Optional<List<Appointment>> findByStatus(AppointmentStatus status);
}
