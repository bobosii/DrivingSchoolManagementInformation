package dev.emir.DrivingSchoolManagementInformation.dto.response;

import dev.emir.DrivingSchoolManagementInformation.dto.response.classroom.ClassroomResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.course.CourseResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.course.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.employee.EmployeeProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.term.TermResponse;

import java.util.List;

public class AdminDashboardResponse {
    private List<StudentProfileResponse> students;
    private List<InstructorProfileResponse> instructors;
    private List<EmployeeProfileResponse> employees;
    private List<TermResponse> terms;
    private List<CourseResponse> courses;
    private List<CourseSessionResponse> courseSessions;
    private List<ClassroomResponse> classrooms;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(
            List<StudentProfileResponse> students,
            List<InstructorProfileResponse> instructors,
            List<EmployeeProfileResponse> employees,
            List<TermResponse> terms,
            List<CourseResponse> courses,
            List<CourseSessionResponse> courseSessions,
            List<ClassroomResponse> classrooms) {
        this.students = students;
        this.instructors = instructors;
        this.employees = employees;
        this.terms = terms;
        this.courses = courses;
        this.courseSessions = courseSessions;
        this.classrooms = classrooms;
    }

    public List<StudentProfileResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentProfileResponse> students) {
        this.students = students;
    }

    public List<InstructorProfileResponse> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<InstructorProfileResponse> instructors) {
        this.instructors = instructors;
    }

    public List<EmployeeProfileResponse> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeProfileResponse> employees) {
        this.employees = employees;
    }

    public List<TermResponse> getTerms() {
        return terms;
    }

    public void setTerms(List<TermResponse> terms) {
        this.terms = terms;
    }

    public List<CourseResponse> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseResponse> courses) {
        this.courses = courses;
    }

    public List<CourseSessionResponse> getCourseSessions() {
        return courseSessions;
    }

    public void setCourseSessions(List<CourseSessionResponse> courseSessions) {
        this.courseSessions = courseSessions;
    }

    public List<ClassroomResponse> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<ClassroomResponse> classrooms) {
        this.classrooms = classrooms;
    }
} 