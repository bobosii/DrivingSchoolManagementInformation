package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.CourseRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Course;
import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    @Autowired
    private final CourseRepository courseRepository;


    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(String name, CourseType courseType){
        Course course = new Course();
        course.setName(name);
        course.setCourseType(courseType);

        return courseRepository.save(course);
    }
}
