package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.ClassroomRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.CourseRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.CourseSessionRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.InstructorRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import dev.emir.DrivingSchoolManagementInformation.models.Course;
import dev.emir.DrivingSchoolManagementInformation.models.CourseSession;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseSessionService {

    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private final CourseSessionRepository courseSessionRepository;

    @Autowired
    private final InstructorRepository instructorRepository;

    @Autowired
    private final ClassroomRepository classroomRepository;

    public CourseSessionService(CourseRepository courseRepository, CourseSessionRepository courseSessionRepository,
                              InstructorRepository instructorRepository, ClassroomRepository classroomRepository) {
        this.courseRepository = courseRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.instructorRepository = instructorRepository;
        this.classroomRepository = classroomRepository;
    }

    public List<CourseSessionResponse> getAllCourseSessions() {
        List<CourseSession> sessions = courseSessionRepository.findByIsDeletedFalse();
        updateSessionStatuses(sessions);
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private void updateSessionStatuses(List<CourseSession> sessions) {
        LocalDateTime now = LocalDateTime.now();
        for (CourseSession session : sessions) {
            // Eğer oturum başlangıç tarihi geçmişse pasif, gelecekteyse aktif olarak işaretle
            boolean isActive = !session.getStartTime().isBefore(now);
            if (session.isActive() != isActive) {
                session.setActive(isActive);
            }
        }
        courseSessionRepository.saveAll(sessions);
    }

    public CourseSessionResponse getCourseSessionById(Long id) {
        CourseSession session = courseSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course session not found"));
        updateSessionStatuses(List.of(session));
        return convertToResponse(session);
    }

    @Transactional
    public CourseSessionResponse createCourseSession(Long courseId, LocalDateTime startTime, LocalDateTime endTime,
                                                   Long instructorId, Long classroomId, int maxStudents) {
        CourseSession session = new CourseSession();
        session.setCourse(courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found")));
        session.setInstructor(instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found")));
        session.setClassroom(classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found")));
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setMaxStudents(maxStudents);
        session.setDeleted(false);
        
        // Başlangıç tarihine göre aktiflik durumunu belirle
        session.setActive(!startTime.isBefore(LocalDateTime.now()));

        CourseSession savedSession = courseSessionRepository.save(session);
        return convertToResponse(savedSession);
    }

    @Transactional
    public CourseSessionResponse updateCourseSession(Long id, Long courseId, LocalDateTime startTime,
                                                   LocalDateTime endTime, Long instructorId, Long classroomId,
                                                   int maxStudents) {
        CourseSession session = courseSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course session not found"));

        session.setCourse(courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found")));
        session.setInstructor(instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found")));
        session.setClassroom(classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found")));
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setMaxStudents(maxStudents);
        
        // Başlangıç tarihine göre aktiflik durumunu güncelle
        session.setActive(!startTime.isBefore(LocalDateTime.now()));

        CourseSession updatedSession = courseSessionRepository.save(session);
        return convertToResponse(updatedSession);
    }

    @Transactional
    public void deleteCourseSession(Long id) {
        CourseSession session = courseSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course session not found"));
        session.setDeleted(true);
        courseSessionRepository.save(session);
    }

    @Transactional
    public CourseSessionResponse activateCourseSession(Long id) {
        CourseSession session = courseSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course session not found"));
        
        System.out.println("Before activation - Session ID: " + id + ", Active: " + session.isActive());
        
        // Force a new transaction and flush
        session.setActive(true);
        CourseSession savedSession = courseSessionRepository.saveAndFlush(session);
        
        // Verify the change
        CourseSession verifiedSession = courseSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course session not found after save"));
        
        System.out.println("After activation - Session ID: " + id + ", Active: " + savedSession.isActive());
        System.out.println("Verified session - Active: " + verifiedSession.isActive());
        
        if (!verifiedSession.isActive()) {
            throw new RuntimeException("Failed to activate course session - database update failed");
        }
        
        return convertToResponse(savedSession);
    }

    @Transactional
    public CourseSessionResponse deactivateCourseSession(Long id) {
        CourseSession session = courseSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course session not found"));
        
        System.out.println("Before deactivation - Session ID: " + id + ", Active: " + session.isActive());
        session.setActive(false);
        CourseSession savedSession = courseSessionRepository.save(session);
        System.out.println("After deactivation - Session ID: " + id + ", Active: " + savedSession.isActive());
        
        return convertToResponse(savedSession);
    }

    private CourseSessionResponse convertToResponse(CourseSession session) {
        return new CourseSessionResponse(
                session.getId(),
                session.getCourse().getName(),
                session.getInstructor().getFullName(),
                session.getClassroom().getName(),
                session.getStartTime(),
                session.getEndTime(),
                session.getMaxStudents(),
                session.isActive()
        );
    }
}
