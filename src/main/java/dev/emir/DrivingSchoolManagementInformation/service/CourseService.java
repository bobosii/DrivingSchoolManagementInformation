package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.CourseRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Course;
import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Design Pattern: Service Layer Pattern
@Service
public class CourseService {

    @Autowired
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return convertToResponse(course);
    }

    @Transactional
    public CourseResponse createCourse(String name, CourseType courseType) {
        Course course = new Course();
        course.setName(name);
        course.setCourseType(courseType);
        Course savedCourse = courseRepository.save(course);
        return convertToResponse(savedCourse);
    }

    @Transactional
    public CourseResponse updateCourse(Long id, String name, CourseType courseType) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setName(name);
        course.setCourseType(courseType);
        Course updatedCourse = courseRepository.save(course);
        return convertToResponse(updatedCourse);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    private CourseResponse convertToResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getCourseType()
        );
    }
}
