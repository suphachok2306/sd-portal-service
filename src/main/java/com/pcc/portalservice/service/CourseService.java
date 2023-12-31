package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Course;
import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.enums.StatusCourse;
import com.pcc.portalservice.repository.CourseRepository;
import com.pcc.portalservice.repository.TrainingRepository;
import com.pcc.portalservice.requests.CreateCourseRequest;
import com.pcc.portalservice.requests.EditCourseRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService {

  // Services
  private final CourseRepository courseRepository;
  private final TrainingService trainingService;
  private final EntityManager entityManager;

  public Course create(CreateCourseRequest createCourseRequest)
    throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date startDateFormat = dateFormat.parse(createCourseRequest.getStartDate());
    Date endDateFormat = dateFormat.parse(createCourseRequest.getEndDate());

    Course course = null;

    if (createCourseRequest.getType().toString().equals("อบรม")) {
      course =
        Course
          .builder()
          .type(createCourseRequest.getType())
          .hours(createCourseRequest.getHours())
          .active("ดำเนินการอยู่")
          .courseName(createCourseRequest.getCourseName())
          .startDate(startDateFormat)
          .endDate(endDateFormat)
          .time(createCourseRequest.getTime())
          .note(createCourseRequest.getNote())
          .objective("เพื่อนำมาใช้ในการปฎิบัติงาน")
          .price(createCourseRequest.getPrice())
          .priceProject(createCourseRequest.getPriceProject())
          .place(createCourseRequest.getPlace())
          .institute(createCourseRequest.getInstitute())
          .build();
    } else if (createCourseRequest.getType().toString().equals("สอบ")) {
      String objectiveString =
        "เพื่อสอบ " + createCourseRequest.getCourseName();
      course =
        Course
          .builder()
          .type(createCourseRequest.getType())
          .hours(createCourseRequest.getHours())
          .active("ดำเนินการอยู่")
          .courseName(createCourseRequest.getCourseName())
          .startDate(startDateFormat)
          .endDate(endDateFormat)
          .time(createCourseRequest.getTime())
          .note(createCourseRequest.getNote())
          .objective(objectiveString)
          .price(createCourseRequest.getPrice())
          .priceProject(createCourseRequest.getPriceProject())
          .place(createCourseRequest.getPlace())
          .institute(createCourseRequest.getInstitute())
          .build();
    }

    return courseRepository.save(course);
  }

  public Course editCourse(EditCourseRequest editCourseRequest, Long courseId)
    throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date startDateFormat = dateFormat.parse(editCourseRequest.getStartDate());
    Date endDateFormat = dateFormat.parse(editCourseRequest.getEndDate());
    Course course = findById(courseId);
    if (editCourseRequest.getType().toString().equals("อบรม")) {
      course.setObjective("เพื่อนำมาใช้ในการปฎิบัติงาน");
    } else if (editCourseRequest.getType().toString().equals("สอบ")) {
      String objectiveString = "เพื่อสอบ " + editCourseRequest.getCourseName();
      course.setObjective(objectiveString);
    }
    course.setType(editCourseRequest.getType());
    course.setCourseName(editCourseRequest.getCourseName());
    course.setStartDate(startDateFormat);
    course.setEndDate(endDateFormat);
    course.setTime(editCourseRequest.getTime());
    course.setNote(editCourseRequest.getNote());
    course.setPrice(editCourseRequest.getPrice());
    course.setPriceProject(editCourseRequest.getPriceProject());
    course.setPlace(editCourseRequest.getPlace());
    course.setInstitute(editCourseRequest.getInstitute());
    course.setHours(editCourseRequest.getHours());

    return courseRepository.save(course);
  }

  public String deleteData(Long id) {
    Optional<Course> optionalCourse = courseRepository.findById(id);
    if (optionalCourse.isPresent()) {
      courseRepository.deleteById(id);
      return "ลบข้อมูลของ ID : " + id + " เรียบร้อย";
    } else {
      return null;
    }
  }

  public Course findById(Long id) {
    return courseRepository
      .findById(id)
      .orElseThrow(() ->
        new EntityNotFoundException("ID " + id + " ไม่มีในระบบ")
      );
  }

  public List<Course> findAllCourse() {
    List<Course> allCourses = courseRepository.findAll();
    List<Course> filteredCourses = new ArrayList<>();

    for (Course course : allCourses) {
      if (!"สอบ".equals(course.getType())) {
        filteredCourses.add(course);
      }
    }

    return filteredCourses;
  }

  public List<Course> findAllTest() {
    List<Course> allCourses = courseRepository.findAll();
    List<Course> filteredCourses = new ArrayList<>();

    for (Course course : allCourses) {
      if ("สอบ".equals(course.getType())) {
        filteredCourses.add(course);
      }
    }

    return filteredCourses;
  }

  public boolean isCourseNull(CreateCourseRequest request) {
    return (
      request == null ||
      request.getCourseName() == null ||
      request.getCourseName().isEmpty() ||
      request.getStartDate() == null ||
      request.getStartDate().isEmpty() ||
      request.getEndDate() == null ||
      request.getEndDate().isEmpty() ||
      request.getTime() == null ||
      request.getTime().isEmpty() ||
      request.getPlace() == null ||
      request.getPlace().isEmpty()
    );
  }

  public Course setStatusToCourse(Long courseId, StatusCourse statuscourse) {
    Course course = courseRepository
      .findById(courseId)
      .orElseThrow(() ->
        new RuntimeException("Course not found with ID: " + courseId)
      );
    if (statuscourse.toString() == "ยกเลิก") {
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(
        Training.class
      );

      Root<Training> trainingRoot = criteriaQuery.from(Training.class);
      Join<Training, Course> courseJoin = trainingRoot.join("courses");

      criteriaQuery.select(trainingRoot);
      criteriaQuery.where(
        criteriaBuilder.equal(courseJoin.get("id"), courseId),
        criteriaBuilder.greaterThanOrEqualTo(
          criteriaBuilder.function(
            "TO_CHAR",
            String.class,
            trainingRoot.get("dateSave"),
            criteriaBuilder.literal("YYYY-MM-DD")
          ),
          criteriaBuilder.function(
            "TO_CHAR",
            String.class,
            criteriaBuilder.currentDate(),
            criteriaBuilder.literal("YYYY-MM-DD")
          )
        )
      );

      TypedQuery<Training> query = entityManager.createQuery(criteriaQuery);
      List<Training> resultList = query.getResultList();
      for (Training training : resultList) {
        trainingService.setCancelToTraining(Long.valueOf(training.getId()));
      }
    }
    course.setActive(statuscourse.toString());
    return courseRepository.save(course);
  }
}
