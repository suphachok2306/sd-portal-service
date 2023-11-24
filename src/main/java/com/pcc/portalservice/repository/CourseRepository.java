package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Course;
import java.sql.Timestamp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  Optional<Course> findByCourseName(String courseName);
  Optional<Course> findByPrice(float price);
  Optional<Course> findByPriceProject(float priceProject);
  Optional<Course> findByPlace(String place);
  Optional<Course> findByInstitute(String institute);

  Optional<Course> findByStartDate(Timestamp startDate);
  Optional<Course> findByEndDate(Timestamp endDate);
}
