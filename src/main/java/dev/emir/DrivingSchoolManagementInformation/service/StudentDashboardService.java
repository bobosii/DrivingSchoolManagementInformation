package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.*;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDashboardResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentDashboardService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CourseSessionRepository courseSessionRepository;
    @Autowired
    private DocumentRepository documentRepository;

    public StudentDashboardResponse getDashboardForUserId(Long userId) {
        // 1. Get User and Student
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Long sid = student.getId();

        // 2. Get counts
        long totalAppointments = appointmentRepository.countByStudent_Id(sid);
        long totalCourseSessions = courseSessionRepository.findAllByStudents_Id(sid).size();
        System.out.println("TOTAL SESSIONS" + totalCourseSessions);
        long totalDocuments = documentRepository.countByStudent_Id(sid);

        // 3. Package into DTO
        return new StudentDashboardResponse(
                sid,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getBirthDate(),
                student.getTerm() != null ? student.getTerm().getName() : null,
                student.getTerm() != null ? student.getTerm().getId() : null,
                (int) totalAppointments,
                (int) totalCourseSessions,
                (int) totalDocuments
        );
    }
} 