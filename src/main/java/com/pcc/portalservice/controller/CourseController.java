package com.pcc.portalservice.controller;

import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import org.springframework.web.bind.annotation.*;

import com.pcc.portalservice.model.Course;
import com.pcc.portalservice.service.CourseService;
import lombok.AllArgsConstructor;
import com.pcc.portalservice.requests.CreateCourseRequest;
import com.pcc.portalservice.repository.CourseRepository;

import java.net.URI;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@AllArgsConstructor
@BasePathAwareController
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/findAllCourse")
    public List<Course> getCourseAll() {
        return courseService.findAllCourse();
    }

    @GetMapping("/findCourseById")
    public ResponseEntity<ApiResponse> findCourseById(@RequestParam Long CourseId) {
        Course course = courseService.findById(CourseId);
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if (course != null) {
            data.setResult(course);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } else {
            response.setResponseMessage("ไม่สามารถทำรายการได้");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/createCourse")
    public ResponseEntity<ApiResponse> createCourse(@RequestBody CreateCourseRequest createCourseRequest) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        if(courseService.isCourseNull(createCourseRequest)) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Course course = courseService.create(createCourseRequest);
            data.setResult(course);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/createCourse").toUriString());
            return ResponseEntity.created(uri).body(response);
        } catch (Exception e) {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้ เพราะ มีข้อผิดพลาดภายในเซิร์ฟเวอร์");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/editCourse")
    public ResponseEntity<ApiResponse> updateCourse(
        @RequestBody CreateCourseRequest createCourseRequest
    ) throws ParseException {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        Course updatedCourse = courseService.editCourse(createCourseRequest);
        if (updatedCourse != null) {
            data.setResult(updatedCourse);
            response.setResponseMessage("ทำรายการเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } else {
            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/deleteCourseById")
    public ResponseEntity<ApiResponse> delete(
         @RequestParam Long courseID
        ) {
            ApiResponse response = new ApiResponse();
            ResponseData data = new ResponseData();
            Course course= courseService.deleteData(courseID);
            if (course != null) {
                data.setResult(course);
                response.setResponseMessage("ลบเรียบร้อย");
                response.setResponseData(data);
                return ResponseEntity.ok().body(response);
            } else {
                response.setResponseMessage("ไม่สามารถทำรายการได้");
                return ResponseEntity.badRequest().body(response);
            }
        } 
    }
    
