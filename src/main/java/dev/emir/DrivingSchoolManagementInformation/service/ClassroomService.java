package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.ClassroomRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Design Pattern: Service Layer Pattern
@Service
public class ClassroomService {
    @Autowired
    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public Classroom createClassroom(String name, int capacity, String location) {
        Classroom classroom = new Classroom();
        classroom.setCapacity(capacity);
        classroom.setName(name);
        classroom.setLocation(location);
        return classroomRepository.save(classroom);
    }

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Optional<Classroom> getClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    public Classroom updateClassroom(Long id, String name, int capacity, String location) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        
        classroom.setName(name);
        classroom.setCapacity(capacity);
        classroom.setLocation(location);
        
        return classroomRepository.save(classroom);
    }

    public void deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sınıf bulunamadı"));

        if (!classroom.getCourseSessions().isEmpty()) {
            throw new RuntimeException("Bu sınıfa bağlı ders oturumları bulunmaktadır. Önce ders oturumlarını silmelisiniz.");
        }

        classroomRepository.delete(classroom);
    }
}
