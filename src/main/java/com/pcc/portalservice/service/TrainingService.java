package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateTrainingSectionOneRequest;
import com.pcc.portalservice.requests.CreateTrainingSectionTwoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TrainingService {
    private final CompanyRepository companyRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final SectorRepository sectorRepository;
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;


    public Training createSectionOne(CreateTrainingSectionOneRequest createTrainingSectionOneRequest) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Timestamp dateSaveTimestamp = new Timestamp(dateFormat.parse(createTrainingSectionOneRequest.getDateSave()).getTime());
//        Timestamp startDateTimestamp = new Timestamp(dateFormat.parse(createTrainingSectionOneRequest.getStartDate()).getTime());
//        Timestamp endDateTimestamp = new Timestamp(dateFormat.parse(createTrainingSectionOneRequest.getEndDate()).getTime());
//        Timestamp actionDateTimestamp = new Timestamp(dateFormat.parse(createTrainingSectionOneRequest.getActionDate()).getTime());

        Optional<Sector> sectorOptional = sectorRepository.findBySectorCodeAndSectorNameAndCompanyCompanyName(
                        createTrainingSectionOneRequest.getSectorCode(),
                        createTrainingSectionOneRequest.getSectorName(),
                        createTrainingSectionOneRequest.getCompanyName()
                );

        Sector sector = sectorOptional.orElseThrow(() -> new RuntimeException("Sector not found / SectorCode or SectorName wrong"));

        Optional<Department> departmentOptional = departmentRepository.findByDeptCodeAndDeptName(
                createTrainingSectionOneRequest.getDeptCode(),
                createTrainingSectionOneRequest.getDeptName()
        );
        Department department = departmentOptional.orElseThrow(() -> new RuntimeException("Department not found / DeptCode or DeptName wrong"));;

        Optional<Position> positionOptional = positionRepository.findByPositionNameAndDepartment(createTrainingSectionOneRequest.getPosition(), department);

        Position position = positionOptional.orElseThrow(() -> new RuntimeException("Position not found"));;

        Company companyName = getCompanyName(createTrainingSectionOneRequest.getCompanyName());
        
        Optional<Course> courseOptional = courseRepository.findByCourseName(createTrainingSectionOneRequest.getCourseName());
        Course course = courseOptional.orElseThrow(() -> new RuntimeException("Course not found"));

        User userId = getUserId(createTrainingSectionOneRequest.getUserId());

        User user = User.builder()
                .company(companyName)
                .sector(sector)
                .department(department)
                .position(position)
                .build();

        Training training = Training.builder()
                .day(createTrainingSectionOneRequest.getDay())
                .action(createTrainingSectionOneRequest.getAction())
                .courses(Collections.singletonList(course))
                .user(userId)
                .build();
        user.setTraining(training);

        trainingRepository.save(training);
        userRepository.save(user);
        return training;
    }



    public Training createSectionTwo(CreateTrainingSectionTwoRequest createTrainingSectionTwoRequest, Long trainingId){

        Sector sectorName = getSectorName(createTrainingSectionTwoRequest.getSectorName());
        Department departmentName = getDepartmentName(createTrainingSectionTwoRequest.getDeptName());
        Position positionName = getPositionName(createTrainingSectionTwoRequest.getPositionName());

        User user = User.builder()
                .sector(sectorName)
                .department(departmentName)
                .position(positionName)
                .build();

        Training training = Training.builder()
                .id(trainingId)
                .user(user)
                .build();

        Result result = Result.builder()
                .result1(createTrainingSectionTwoRequest.getResult1())
                .result2(createTrainingSectionTwoRequest.getResult2())
                .result3(createTrainingSectionTwoRequest.getResult3())
                .result4(createTrainingSectionTwoRequest.getResult4())
                .result5(createTrainingSectionTwoRequest.getResult5())
                .result6(createTrainingSectionTwoRequest.getResult6())
                .result7(createTrainingSectionTwoRequest.getResult7())
                .result(createTrainingSectionTwoRequest.getResult())
                .comment(createTrainingSectionTwoRequest.getComment())
                .cause(createTrainingSectionTwoRequest.getCause())
                .plan(createTrainingSectionTwoRequest.getPlan())
                .evaluator(user)
                .build();

        result.setEvaluator(user);
        result.setTraining(training);

        trainingRepository.save(training);
        userRepository.save(user);
        resultRepository.save(result);
        return training;
    }

    private Company getCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyName));
    }

    private Sector getSectorName(String sectorName) {
        return sectorRepository.findBySectorName(sectorName)
                .orElseThrow(() -> new RuntimeException("Sector not found: " + sectorName));
    }

    private Sector getSectorCode(String sectorCode) {
        return sectorRepository.findBySectorCode(sectorCode)
                .orElseThrow(() -> new RuntimeException("Sector not found by code: " + sectorCode));
    }

    private Department getDepartmentName(String deptName) {
        return departmentRepository.findByDeptName(deptName)
                .orElseThrow(() -> new RuntimeException("Department not found: " + deptName));
    }

    private Department getDepartmentCode(String deptCode) {
        return departmentRepository.findByDeptCode(deptCode)
                .orElseThrow(() -> new RuntimeException("Department not found by code: " + deptCode));
    }

    private Course getCourseName(String courseName) {
        return courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new RuntimeException("Course not found by name: " + courseName));
    }

    private Course getCourseStartDate(Timestamp startDate) {
        return courseRepository.findByStartDate(startDate)
                .orElseThrow(() -> new RuntimeException("Course not found by startDate: " + startDate));
    }

    private Course getCourseEndDate(Timestamp endDate) {
        return courseRepository.findByEndDate(endDate)
                .orElseThrow(() -> new RuntimeException("Course not found by endDate: " + endDate));
    }

    private Course getCoursePrice(float price) {
        return courseRepository.findByPrice(price)
                .orElseThrow(() -> new RuntimeException("Course not found by price: " + price));
    }

    private Course getCoursePriceProject(float priceProject) {
        return courseRepository.findByPriceProject(priceProject)
                .orElseThrow(() -> new RuntimeException("Course not found by project price: " + priceProject));
    }

    private Course getCoursePlace(String place) {
        return courseRepository.findByPlace(place)
                .orElseThrow(() -> new RuntimeException("Course not found by place: " + place));
    }

    private Course getCourseInstitute(String institute) {
        return courseRepository.findByInstitute(institute)
                .orElseThrow(() -> new RuntimeException("Course not found by institute: " + institute));
    }

    private Position getPositionName(String position) {
        return positionRepository.findByPositionName(position)
                .orElseThrow(() -> new RuntimeException("Position not found by position: " + position));
    }

    private Training getTrainingId(Long trainingId){
        return trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found by id: " + trainingId));
    }

    private User getUserId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found by id: " + userId));
    }

}


