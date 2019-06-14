package siakng.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import siakng.model.Course;
import siakng.service.SiakNgService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
    public void getCourse() throws Exception {
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
    public void addCourse() throws Exception {
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
        verifyNoMoreInteractions(siakNgService);
                /*.andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Statprob"));*/
    }

    @Test
    public void deleteCourse() throws Exception {
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


}
