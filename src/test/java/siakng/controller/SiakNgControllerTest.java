package siakng.controller;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import siakng.model.Course;
import siakng.service.SiakNgService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        given(siakNgService.getCourses()).willReturn(courseList);
        Mockito.when(siakNgService.getCourses()).thenReturn(courseList);

        this.mockMvc.perform(get("/academic/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Statprob"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("DDP2"));
    }


}
