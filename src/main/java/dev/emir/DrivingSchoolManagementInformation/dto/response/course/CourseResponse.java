package dev.emir.DrivingSchoolManagementInformation.dto.response.course;

import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;

public class CourseResponse {
    private Long id;
    private String name;
    private CourseType courseType;

    public CourseResponse() {
    }

    public CourseResponse(Long id, String name, CourseType courseType) {
        this.id = id;
        this.name = name;
        this.courseType = courseType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }
} 