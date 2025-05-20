package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.ClassroomRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id).orElse(null);
    }

    public Classroom updateClassroom(Long id, Classroom classroom) {
        Classroom existingClassroom = classroomRepository.findById(id).orElse(null);
        if (existingClassroom != null) {
            existingClassroom.setName(classroom.getName());
            existingClassroom.setCapacity(classroom.getCapacity());
            return classroomRepository.save(existingClassroom);
        }
        return null;
    }

    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }
}
