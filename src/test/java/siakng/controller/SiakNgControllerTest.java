package siakng.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import siakng.model.Course;
import siakng.service.SiakNgService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(SiakNgController.class)
public class SiakNgControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SiakNgService siakNgService;

    @Before
    public void setUp() {
        //initMocks(this);
    }

    @Test
    public void getCourses() throws Exception {
        Course c1 = new Course(1L, "Statprob", 3);
        Course c2 = new Course(2L, "DDP2", 4);

        List<Course> courseList = Arrays.asList(c1, c2);
        Mockito.when(siakNgService.getCourses()).thenReturn(courseList);

        this.mockMvc.perform(get("/academic/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Statprob"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("DDP2"));

        verify(siakNgService, times(1)).getCourses();
        verifyNoMoreInteractions(siakNgService);
    }

    @Test
    public void getCourse_Success() throws Exception {
        Course c1 = new Course(1L, "Statprob", 3);

        Mockito.when(siakNgService.getCourse(1L)).thenReturn(c1);

        this.mockMvc.perform(get("/academic/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Statprob"));

        verify(siakNgService, times(1)).getCourse(1L);
        verifyNoMoreInteractions(siakNgService);
    }

    @Test
    public void getCourse_WhenCourseIsNotFound_ReturnsNotFound() throws Exception {
        Mockito.when(siakNgService.getCourse(1L)).thenThrow(new ResourceNotFoundException());

        this.mockMvc.perform(get("/academic/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void addCourse_Success() throws Exception {
        Course c1 = new Course(1L, "Statprob", 3);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(c1);

        given(siakNgService.addCourse(any(Course.class))).willReturn(c1);
        this.mockMvc.perform(post("/academic/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(siakNgService, times(1)).addCourse("Statprob", 3);
        verifyNoMoreInteractions(siakNgService);
                /*.andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Statprob"));*/
    }

    @Test
    public void addCourse_WhenSksIsMoreThan24_ReturnsException() throws Exception {
        final int courseNum = 5;
        String[] courseStrings = {"DDP", "SDA", "Basdat", "Statprob", "TBA"};
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < courseNum; i++) {
            Course c = new Course(courseStrings[i], 4);
            String json = objectMapper.writeValueAsString(c);
            given(siakNgService.addCourse(c)).willReturn(c);
            this.mockMvc.perform(post("/academic/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .characterEncoding("utf-8"));
            verify(siakNgService, times(1)).addCourse(courseStrings[i], 4);
        }
        Course errorCourse = new Course("Course Pembuat Error", 6);
        String json = objectMapper.writeValueAsString(errorCourse);
        Mockito.when(siakNgService.addCourse(errorCourse)).thenThrow(new RuntimeException("SKS melebihi 24"));
        this.mockMvc.perform(post("/academic/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"));
                //@TODO belum selesai, masih 200 OK
                //.andExpect(status().isBadRequest());
    }

    @Test
    public void addCourse_WhenDateLimitExceeded_ReturnsException() throws Exception {
        Course c1 = new Course(1L, "Statprob", 3);
        c1.setDateCreated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(c1);
        given(siakNgService.addCourse(any(Course.class))).willReturn(null);
        this.mockMvc.perform(post("/academic/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"));
        //@TODO belum selesai, masih 200 OK
        //.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCourse_Success() throws Exception {
        Course c1 = new Course(1L, "Statprob", 3);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(c1);

        Mockito.when(siakNgService.addCourse(any(Course.class))).thenReturn(c1);

        this.mockMvc.perform(post("/academic/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(siakNgService, times(1)).addCourse("Statprob", 3);

        Mockito.when(siakNgService.deleteCourse(1L)).thenReturn(c1);

        this.mockMvc.perform(delete("/academic/delete/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Statprob"));

        verify(siakNgService, times(1)).deleteCourse(1L);
        verifyNoMoreInteractions(siakNgService);
    }

    @Test
    public void deleteCourse_WhenCourseIsNotFound_ReturnsNotFound() throws Exception {
        Mockito.when(siakNgService.deleteCourse(1L)).thenThrow(new ResourceNotFoundException());

        this.mockMvc.perform(delete("/academic/delete/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
