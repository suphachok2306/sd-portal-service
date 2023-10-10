package com.pcc.portalservice.service;
import com.pcc.portalservice.model.Course;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.repository.CourseRepository;
import com.pcc.portalservice.requests.CreateCourseRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService {
    // Services
    private final CourseRepository  courseRepository;

    public boolean isCourseNull(CreateCourseRequest request){
        return request == null || request.getCourseName() == null || request.getCourseName().isEmpty()
                || request.getStartDate() == null || request.getStartDate().isEmpty()
                || request.getEndDate() == null || request.getEndDate().isEmpty()
                || request.getTime() == null || request.getTime().isEmpty()
                || request.getPlace() == null || request.getPlace().isEmpty();
    }

    public Course create(CreateCourseRequest createCourseRequest) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFormat = dateFormat.parse(createCourseRequest.getStartDate());
        Date endDateFormat = dateFormat.parse(createCourseRequest.getEndDate());
        
        Course course = Course.builder()
                .courseName(createCourseRequest.getCourseName())
                .startDate(startDateFormat)
                .endDate(endDateFormat)
                .time(createCourseRequest.getTime())
                .note(createCourseRequest.getNote())
                .objective(createCourseRequest.getObjective())
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


    public Course editCourse(CreateCourseRequest createCourseRequest) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFormat = dateFormat.parse(createCourseRequest.getStartDate());
        Date endDateFormat = dateFormat.parse(createCourseRequest.getEndDate());

        Course course = findById(createCourseRequest.getCourseId());
        course.setCourseName(createCourseRequest.getCourseName());
        course.setStartDate(startDateFormat);
        course.setEndDate(endDateFormat);
        course.setTime(createCourseRequest.getTime());
        course.setNote(createCourseRequest.getNote());
        course.setObjective(createCourseRequest.getObjective());
        course.setPrice(createCourseRequest.getPrice());
        course.setPriceProject(createCourseRequest.getPriceProject());
        course.setPlace(createCourseRequest.getPlace());
        course.setInstitute(createCourseRequest.getInstitute());

        return courseRepository.save(course);
    }

//    public Course deleteData(Long id) {
//        Course course = courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " ไม่มีในระบบ"));
//
//        courseRepository.delete(course);
//
//        return course;
//    }

    public String deleteData(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            courseRepository.deleteById(id);
            return "ลบข้อมูลของ ID : " + id + " เรียบร้อย";
        }
        else{
            return null;
        }
    }
}
