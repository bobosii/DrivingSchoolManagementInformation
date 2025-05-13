package dev.emir.DrivingSchoolManagementInformation.helper.profileMapper;

import dev.emir.DrivingSchoolManagementInformation.dto.response.employee.EmployeeProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Employee;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.User;

public class ProfileResponseMapper {

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
}
