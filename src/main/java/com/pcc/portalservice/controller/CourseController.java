package com.pcc.portalservice.controller;

import org.springframework.web.bind.annotation.*;

import com.pcc.portalservice.model.Course;
import com.pcc.portalservice.service.CourseService;
import lombok.AllArgsConstructor;
import com.pcc.portalservice.requests.CreateCourseRequest;
import com.pcc.portalservice.repository.CourseRepository;

import java.util.List;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



@RestController
@AllArgsConstructor
@BasePathAwareController
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/findAllCourse")
    public ResponseEntity<List<Course>> getCourseAll() {
        List<Course> courses = courseService.findAllCourse();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/findCourseById")
    public ResponseEntity<Course> findCourseById(@RequestParam Long CourseId) {
        Course course = courseService.findById(CourseId);
        if (course != null) {
            return new ResponseEntity<>(course, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createCourse")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest createCourseRequest) {
        Course course = courseService.create(createCourseRequest);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/editCourse")
    public ResponseEntity<Course> updateCourse(
        @RequestParam Long courseID,
        @RequestBody CreateCourseRequest createCourseRequest
    ) {
        Course updatedCourse = courseService.editCourse(courseID,createCourseRequest);
        if (updatedCourse != null) {
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/deleteCourseById")
    public ResponseEntity<String> delete(
         @RequestParam Long courseID
        ) {
            Course course= courseService.deleteData(courseID);
            if (course != null) {
                return new ResponseEntity<>("ลบเรียบร้อย", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } 
    }
    
