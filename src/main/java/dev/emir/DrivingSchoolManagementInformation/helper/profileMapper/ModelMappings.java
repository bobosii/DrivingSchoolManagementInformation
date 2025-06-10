package dev.emir.DrivingSchoolManagementInformation.helper.profileMapper;

import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.AppointmentResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.employee.EmployeeProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.models.*;

public class ModelMappings {

    public static StudentProfileResponse toStudentProfile(Student student, User user){
        return new StudentProfileResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getBirthDate(),
                student.getTerm() != null ? student.getTerm().getName() : null,
                user.getUsername(),
                user.getRole().name()
        );
    }

    public static InstructorProfileResponse toInstructorProfile(Instructor instructor, User user){
        return new InstructorProfileResponse(
                instructor.getId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                instructor.getCertificationNo(),
                user.getRole().name(),
                user.getUsername()
        );
    }

    public static EmployeeProfileResponse toEmployeeProfile(Employee employee, User user){
        return new EmployeeProfileResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                user.getRole().name(),
                user.getUsername()
        );
    }
    public static ApiResponse<CourseSessionResponse> getCourseSessionResponse(CourseSession created) {
        CourseSessionResponse responseData = new CourseSessionResponse(
                created.getId(),
                created.getCourse().getName(),
                created.getInstructor().getFirstName() + " " + created.getInstructor().getLastName(),
                created.getClassroom().getName(),
                created.getStartTime(),
                created.getEndTime(),
                created.getMaxStudents(),
                created.isActive()
        );
        ApiResponse<CourseSessionResponse> response = new ApiResponse<>(true,"Course session created successfully",responseData);
        return response;
    }

    public static AppointmentResponse toAppointmentResponse(Appointment appointment){
        Student student = appointment.getStudent();
        Instructor instructor = appointment.getInstructor();
        AppointmentType type = appointment.getAppointmentType();
        CourseSession courseSession = appointment.getCourseSession();

        AppointmentResponse responseData = new AppointmentResponse();
        responseData.setId(appointment.getId());
        responseData.setStudentId(student.getId());
        responseData.setStudentName(student.getFullName());
        responseData.setInstructorId(instructor.getId());
        responseData.setInstructorName(instructor.getFullName());
        responseData.setAppointmentTypeId(type.getId());
        responseData.setAppointmentTypeName(type.getName());
        
        if (courseSession != null) {
            responseData.setCourseSessionId(courseSession.getId());
            responseData.setCourseName(courseSession.getCourse().getName());
        }
        
        responseData.setStatus(appointment.getStatus());
        responseData.setRequestedAt(appointment.getRequestedAt());
        responseData.setApprovedAt(appointment.getApprovedAt());
        responseData.setAppointmentTime(appointment.getAppointmentTime());

        return responseData;
    }
}
