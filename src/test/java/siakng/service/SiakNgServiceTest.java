package siakng.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import siakng.model.Course;
import siakng.repository.SiakNgRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SiakNgServiceTest {

    @InjectMocks
    SiakNgService siakNgService;

    @Mock
    SiakNgRepository siakNgRepository;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = RuntimeException.class)
    public void addCourse_WhenSksIsMoreThan24_ReturnsException() {
        final int courseNum = 5;
        String[] courseStrings = {"DDP", "SDA", "Basdat", "Statprob", "TBA"};
        List<Course> courseList = new ArrayList<>();
        for (int i = 0; i < courseNum; i++) {
            Course c = new Course(courseStrings[i], 4);
            courseList.add(c);
            when(siakNgRepository.save(c)).thenReturn(c);
            siakNgService.addCourse(c);
        }
        when(siakNgRepository.findAll()).thenReturn(courseList);

        siakNgService.addCourse(new Course("Course Pembuat Error", 6));
    }

    @Test(expected = RuntimeException.class)
    public void addCourse_WhenDateLimitExceeded_ReturnsException() {
        Course c1 = new Course(1L, "Statprob", 3);
        c1.setDateCreated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        Mockito.when(siakNgRepository.save(c1)).thenReturn(c1);
        Course result = siakNgService.addCourse(c1);

    }

    @Test
    public void addCourse_Success() {
        Course c1 = new Course(1L, "Statprob", 3);
        Mockito.when(siakNgRepository.save(c1)).thenReturn(c1);
        Course result = siakNgService.addCourse(c1);
        Assert.assertEquals(c1.getId(), result.getId());
    }

    @Test
    public void getCourses() {
        Course c1 = new Course(1L, "Statprob", 3);
        Course c2 = new Course(2L, "Anum", 3);
        List<Course> courseList = new ArrayList<>();
        courseList.add(c1);
        courseList.add(c2);
        Mockito.when(siakNgRepository.findAll()).thenReturn(courseList);
        List<Course> result = siakNgService.getCourses();
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(courseList.get(i).getId(), result.get(i).getId());
        }
    }

    @Test
    public void getCourse() {
        Course c1 = new Course(1L, "Statprob", 3);

        Mockito.when(siakNgRepository.save(c1)).thenReturn(c1);
        siakNgService.addCourse(c1);

        Mockito.when(siakNgRepository.getOne(1L)).thenReturn(c1);
        Course result = siakNgService.getCourse(1L);
        Assert.assertEquals(c1.getId(), result.getId());
    }

    @Test
    public void deleteCourse() {
        Course c1 = new Course(1L, "Statprob", 3);

        Mockito.when(siakNgRepository.save(c1)).thenReturn(c1);
        siakNgService.addCourse(c1);

        Course result = siakNgService.deleteCourse(1L);
        verify(siakNgRepository, times(1)).delete(result);
    }
}
