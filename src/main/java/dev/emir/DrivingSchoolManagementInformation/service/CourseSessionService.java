package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.ClassroomRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.CourseRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.CourseSessionRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.InstructorRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import dev.emir.DrivingSchoolManagementInformation.models.Course;
import dev.emir.DrivingSchoolManagementInformation.models.CourseSession;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public CourseSessionService(CourseRepository courseRepository, CourseSessionRepository courseSessionRepository, InstructorRepository instructorRepository, ClassroomRepository classroomRepository) {
        this.courseRepository = courseRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.instructorRepository = instructorRepository;
        this.classroomRepository = classroomRepository;
    }

    public CourseSession createCourseSession(Long courseId, LocalDateTime startTime, Long instructorId, Long classroomId, LocalDateTime endTime){
        System.out.println("courseId: " + courseId);
        System.out.println("instructorId: " + instructorId);
        System.out.println("classroomId: " + classroomId);

        // kontrol amaçlı
        if (courseId == null || instructorId == null || classroomId == null) {
            throw new IllegalArgumentException("One of the IDs is null.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        CourseSession courseSession = new CourseSession();
        courseSession.setCourse(course);
        courseSession.setStartTime(startTime);
        courseSession.setEndTime(endTime);
        courseSession.setClassroom(classroom);
        courseSession.setInstructor(instructor);

        return courseSessionRepository.save(courseSession);
    }
}
