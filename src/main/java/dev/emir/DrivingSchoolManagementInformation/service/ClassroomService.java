package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.ClassroomRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomService {
    @Autowired
    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public Classroom createClassroom(String name,int capacity){
        Classroom classroom = new Classroom();
        classroom.setCapacity(capacity);
        classroom.setName(name);

        return classroomRepository.save(classroom);
    }
}
