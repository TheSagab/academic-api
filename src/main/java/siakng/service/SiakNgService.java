package siakng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siakng.repository.SiakNgRepository;
import siakng.model.Course;

import java.util.Date;
import java.util.List;

@Service
public class SiakNgService {

    private static final int MAX_SKS = 24;
    private static final Date DATE_LIMIT = new Date(1561856400000L);

    @Autowired
    SiakNgRepository siakNgRepository;

    public Course addCourse(String name, int sks){
        Course course = new Course(name, sks);
        return addCourse(course);
    }

    public Course addCourse(Course course){
        int sks = course.getSks();
        int sksTaken = getCourses().stream().mapToInt(e -> e.getSks()).sum();
        if (sksTaken + sks > MAX_SKS) {
            throw new RuntimeException("SKS melebihi 24");
        }
        if (course.getDateCreated().getTime() > DATE_LIMIT.getTime()) {
            throw new RuntimeException("Melebihi batas waktu");
        }
        return siakNgRepository.save(course);
    }

    public List<Course> getCourses() {
        return siakNgRepository.findAll();
    }

    public Course getCourse(int id){
        return siakNgRepository.getOne(id);
    }

    public Course deleteCourse(int id){
        Course course = getCourse(id);
        siakNgRepository.delete(course);
        return course;
    }
}
