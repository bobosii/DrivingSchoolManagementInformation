package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.CourseSessionRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.StudentCourseSessionRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.models.CourseSession;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.StudentCourseSession;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
// Design Pattern: Service Layer Pattern
@Service
public class StudentCourseSessionService {
    @Autowired
    private final StudentCourseSessionRepository studentCourseSessionRepository;
    @Autowired
    private final CourseSessionRepository courseSessionRepository;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final UserRepository userRepository;

    public StudentCourseSessionService(StudentCourseSessionRepository studentCourseSessionRepository, CourseSessionRepository courseSessionRepository, StudentRepository studentRepository, UserRepository userRepository) {
        this.studentCourseSessionRepository = studentCourseSessionRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public StudentCourseSession assignTheoreticalLesson(Long studentId, Long sessionId, Long assignerId){
        CourseSession session = courseSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session id not found"));

        if (session.getCourse().getCourseType() != CourseType.THEORETICAL){
            new RuntimeException("This is not a theoretical lesson");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        User assigner = userRepository.findById(assignerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudentCourseSession studentCourseSession = new StudentCourseSession();
        studentCourseSession.setStudent(student);
        studentCourseSession.setAssignedBy(assigner);
        studentCourseSession.setCourseSession(session);
        studentCourseSession.setAssignedAt(LocalDateTime.now());
        studentCourseSession.setApproved(true); // Because of theoretical lesson. We don't need to approve this request.

        return studentCourseSessionRepository.save(studentCourseSession);
    }

    public StudentCourseSession createAppointment(Long studentId, Long courseSessionId){
        CourseSession session = this.courseSessionRepository.findById(courseSessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        CourseType courseType = session.getCourse().getCourseType();

        if (courseType != CourseType.SIMULATION && courseType != CourseType.DRIVING){
            throw new RuntimeException("Only simulation or driving lessons require appointment");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentCourseSession courseSession = new StudentCourseSession();
        courseSession.setStudent(student);
        courseSession.setCourseSession(session);
        courseSession.setAssignedBy(null);
        courseSession.setApproved(false);
        courseSession.setAssignedAt(LocalDateTime.now());

        return studentCourseSessionRepository.save(courseSession);
    }

    public StudentCourseSession approveAppointment(Long appointmentId, Long approverId){
        StudentCourseSession session = studentCourseSessionRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment Not found"));

        if (session.isApproved()){
            throw new RuntimeException("This appointment is already approved");
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        session.setApproved(true);
        session.setAssignedBy(approver);

        return studentCourseSessionRepository.save(session);
    }

}
