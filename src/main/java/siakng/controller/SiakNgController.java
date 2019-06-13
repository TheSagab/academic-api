package siakng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import siakng.model.Course;
import siakng.service.SiakNgService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/academic")
public class SiakNgController {

    @Autowired
    SiakNgService siakNgService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<Course> getCourses() {
        return siakNgService.getCourses();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Course getCourse(@PathVariable String id) {
        return siakNgService.getCourse(Integer.parseInt(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Course addCourse(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        int sks = Integer.parseInt(body.get("sks"));
        return siakNgService.addCourse(name, sks);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Course deleteCourse(@PathVariable String id) {
        int courseId = Integer.parseInt(id);
        return siakNgService.deleteCourse(courseId);
    }

}
