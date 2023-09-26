package com.pcc.portalservice.service;
import com.pcc.portalservice.model.Course;
import com.pcc.portalservice.model.Department;
import com.pcc.portalservice.model.Position;
import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.repository.CourseRepository;
import com.pcc.portalservice.requests.CreateCourseRequest;
import com.pcc.portalservice.requests.CreateEmployeeRequest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService {
    // Services
    private final AuthenticationService authenticationService;
    private final CourseRepository  courseRepository;
    
    public Course create(CreateCourseRequest createCourseRequest) {
        
        Course course = Course.builder()
            .course_name(createCourseRequest.getCourse_name())
            .startDate(createCourseRequest.getStartDate())
            .endDate(createCourseRequest.getEndDate())
            .time(createCourseRequest.getTime())
            .note(createCourseRequest.getNote())
            .price(createCourseRequest.getPrice())
            .priceProject(createCourseRequest.getPriceProject())
            .place(createCourseRequest.getPlace())
            .institute(createCourseRequest.getInstitute())
            .build();


        return courseRepository.save(course);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " ไม่มีในระบบ"));
    }
    
     public List<Course> findAllCourse() {
        return courseRepository.findAll();
    }


     public Course editCourse(CreateCourseRequest createCourseRequest) {
        Course course = findById(createCourseRequest.getCourse_id());
        course.setCourse_name(createCourseRequest.getCourse_name());
        course.setStartDate(createCourseRequest.getStartDate());
        course.setEndDate(createCourseRequest.getEndDate());
        course.setTime(createCourseRequest.getTime());
        course.setNote(createCourseRequest.getNote());
        course.setPrice(createCourseRequest.getPrice());
        course.setPriceProject(createCourseRequest.getPriceProject());
        course.setPlace(createCourseRequest.getPlace());
        course.setInstitute(createCourseRequest.getInstitute());

        return courseRepository.save(course);
    }

    public Course deleteData(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " ไม่มีในระบบ"));

        courseRepository.delete(course);

        return course;
    }
}
