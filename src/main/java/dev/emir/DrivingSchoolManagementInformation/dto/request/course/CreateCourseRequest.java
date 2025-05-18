package dev.emir.DrivingSchoolManagementInformation.dto.request.course;

import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;

public class CreateCourseRequest {
    private String name;
    private CourseType courseType;

    public CreateCourseRequest(){}

    public CreateCourseRequest(String name, CourseType courseType) {
        this.name = name;
        this.courseType = courseType;
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
