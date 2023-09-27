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


    public Training createSectionOne(CreateTrainingSectionOneRequest createTrainingSectionOneRequest) {
        Company companyName = companyRepository.findByCompanyName(createTrainingSectionOneRequest.getCompanyName())
                .orElseThrow(() -> new RuntimeException("companyName not found: " + createTrainingSectionOneRequest.getCompanyName()));

        Sector sectorName = sectorRepository.findBySectorName(createTrainingSectionOneRequest.getSectorName())
                .orElseThrow(() -> new RuntimeException("sectorName not found: " + createTrainingSectionOneRequest.getSectorName()));

        Sector sectorCode = sectorRepository.findBySectorCode(createTrainingSectionOneRequest.getSectorCode())
                .orElseThrow(() -> new RuntimeException("sectorCode not found: " + createTrainingSectionOneRequest.getSectorCode()));

        Department departmentName = departmentRepository.findByDeptName(createTrainingSectionOneRequest.getDeptName())
                .orElseThrow(() -> new RuntimeException("departmentName not found: " + createTrainingSectionOneRequest.getDeptName()));

        Department departmentCode = departmentRepository.findByDeptCode(createTrainingSectionOneRequest.getDeptCode())
                .orElseThrow(() -> new RuntimeException("departmentCode not found: " + createTrainingSectionOneRequest.getDeptCode()));

        Course courseName = courseRepository.findByCourseName(createTrainingSectionOneRequest.getCauseName())
                .orElseThrow(() -> new RuntimeException("courseName not found: " + createTrainingSectionOneRequest.getCauseName()));

//        Course startDate = courseRepository.findByStartDate(createTrainingSectionOneRequest.getStartDate())
//                .orElseThrow(() -> new RuntimeException("startDate not found: " + createTrainingSectionOneRequest.getStartDate()));
//
//        Course endDate = courseRepository.findByEndDate(createTrainingSectionOneRequest.getEndDate())
//                .orElseThrow(() -> new RuntimeException("endDate not found: " + createTrainingSectionOneRequest.getEndDate()));

        Course coursePrice = courseRepository.findByPrice(createTrainingSectionOneRequest.getPrice())
                .orElseThrow(() -> new RuntimeException("coursePrice not found: " + createTrainingSectionOneRequest.getPrice()));

        Course coursePriceProject = courseRepository.findByPriceProject(createTrainingSectionOneRequest.getPriceProject())
                .orElseThrow(() -> new RuntimeException("coursePriceProject not found: " + createTrainingSectionOneRequest.getPriceProject()));

        Course coursePlace = courseRepository.findByPlace(createTrainingSectionOneRequest.getPlace())
                .orElseThrow(() -> new RuntimeException("coursePlace not found: " + createTrainingSectionOneRequest.getPlace()));

        Course courseInstitute = courseRepository.findByInstitute(createTrainingSectionOneRequest.getInstitute())
                .orElseThrow(() -> new RuntimeException("courseInstitute not found: " + createTrainingSectionOneRequest.getInstitute()));


//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Timestamp currentTimestamp = new Timestamp(dateFormat.parse(createTrainingSectionOneRequest.getDateSave()).getTime());
//        Timestamp startDateTimestamp = new Timestamp(dateFormat.parse(createTrainingSectionOneRequest.getStartDate()).getTime());
//        Timestamp endDateTimestamp = new Timestamp(dateFormat.parse(createTrainingSectionOneRequest.getEndDate()).getTime());

        Collection<Course> name = new ArrayList<>();
        name.add(courseName);

        Collection<Course> price = new ArrayList<>();
        price.add(coursePrice);

        Collection<Course> priceProject = new ArrayList<>();
        priceProject.add(coursePriceProject);

        Collection<Course> place = new ArrayList<>();
        place.add(coursePlace);

        Collection<Course> institute = new ArrayList<>();
        institute.add(courseInstitute);

        User user = User.builder()
                .company(companyName)
                .sector(sectorName)
                .sector(sectorCode)
                .department(departmentCode)
                .department(departmentName)
                .build();

        Training training = Training.builder()
                //.dateSave(currentTimestamp)
                .day(createTrainingSectionOneRequest.getDay())
                .action(createTrainingSectionOneRequest.getAction())
                .courses(name)
                .courses(price)
                .courses(priceProject)
                .courses(place)
                .courses(institute)
                .user(user)
                .build();
        user.setTraining(training);

        trainingRepository.save(training);
        userRepository.save(user);

        return training;
    }

//    public Training createSectionTwo(CreateTrainingSectionTwoRequest createTrainingSectionTwoRequest){
//
//    }
}
