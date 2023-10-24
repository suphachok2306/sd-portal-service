package com.pcc.portalservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

  private final JdbcTemplate jdbcTemplate;
  private final PasswordEncoder passwordEncoder;

  public DatabaseInitializer(
    JdbcTemplate jdbcTemplate,
    PasswordEncoder passwordEncoder
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    insertDataIntoCourseTable();
    insertDataIntoCompanyTable();
    insertDataIntoSectorTable();
    insertDataIntoDepartmentTable();
    insertDataIntoPositionTable();
    insertDataIntoUsersTable();
    insertBudgetTable();
  }

  private boolean isTableEmpty(String tableName) {
    try {
      String sql = "SELECT COUNT(*) FROM " + tableName;
      int count = jdbcTemplate.queryForObject(sql, Integer.class);
      return count == 0;
    } catch (EmptyResultDataAccessException e) {
      return true;
    }
  }

  private void insertDataIntoCourseTable() {
    if (isTableEmpty("public.course")) {
      jdbcTemplate.execute(
        "INSERT INTO public.course (course_name, end_date, institute, note, place, price, price_project, start_date, time, objective) VALUES " +
        "('BA Fundamental', '2023-11-01 07:00:00.000', 'คุณนิทัศน์ ,คุณเงาะ ,คุณนักสู้ ,คุณชำนาญ', '5-6/10,10-12/10,16-30/10,1-10/11/2566', '102/99 ถนน ณ ระนอง คลองเตย กรุงเทพฯ', 0.0, 0.0, '2023-10-05 07:00:00.000', '09:00-16:30', ''), " +
        "('Introduction to Microservice รุ่น 5', '2023-08-11 07:00:00.000', 'คุณนิทัศน์ หวังวิบูลย์กิจ', NULL, 'Online ผ่าน Program Microsoft Teams', 0.0, 0.0, '2023-08-10 07:00:00.000', '09:00-17:00', ''), " +
        "('Soft Skillathon for IT Professional (Business Mindset and Diversity Mindset)', '2023-08-18 07:00:00.000', 'Software Park Thailand', '', 'Software Park Training Room 3rd floor', 10000.0, 0.0, '2023-08-17 07:00:00.000', '09:00-16:00', ''), " +
        "('DevOps : From Development to Deployment/Production', '2023-08-18 07:00:00.000', 'IMC Institute', NULL, '141/7 Skulthai Surawong Tower 8th FL,Surawong Road, · Suriyawong, Bangrak, Bangkok,Bangkok 10500', 11900.0, 0.0, '2023-08-16 07:00:00.000', '09:00-16:30', ''), " +
        "('Docker for Developers in Practice from Source Code to CI/CD', '2023-07-25 07:00:00.000', 'IMC Institute', NULL, '141/7 Skulthai Surawong Tower 8th FL,Surawong Road, · Suriyawong, Bangrak, Bangkok,Bangkok 10500', 9900.0, 0.0, '2023-07-24 07:00:00.000', '09:00-16:30', ''), " +
        "('ChatGPT for Management', '2023-06-26 07:00:00.000', 'IMC Institute', NULL, '141/7 Skulthai Surawong Tower 8th FL,Surawong Road, · Suriyawong, Bangrak, Bangkok,Bangkok 10500', 5900.0, 0.0, '2023-06-26 07:00:00.000', '09:00-16:30', ''), " +
        "('Python for Programmers', '2023-03-31 07:00:00.000', 'Software Park Thailand', '', 'Software Park Training Room 3rd floor', 13000.0, 0.0, '2023-03-28 07:00:00.000', '09:00-16:00', ''), " +
        "('Workshop Design Thinking', '2023-02-05 07:00:00.000', 'คุณเกรียงไกร  นิตรานนท์', '', 'อาคารพหลโยธินเพลส ชั้น 41', 726.89, 0.0, '2023-02-04 07:00:00.000', '09:00-16:30', ''), " +
        "('Basic Software Testing', '2023-02-28 07:00:00.000', 'IMC Institute', NULL, 'ผ่านโปรแกรม Zoom', 6900.0, 0.0, '2023-02-27 07:00:00.000', '09:00-16:00', ''), " +
        "('Advance SQL', '2023-02-23 07:00:00.000', 'คุณนิทัศน์ หวังวิบูลย์กิจ', NULL, 'ออนไลน์ ผ่าน Program Microsoft Teams', 0.0, 0.0, '2023-02-02 07:00:00.000', '09:00-17:00', '');"
      );
      System.out.println("Data inserted into public.course table.");
    } else {
      System.out.println("public.course table is not empty. No data inserted.");
    }
  }

  private void insertDataIntoCompanyTable() {
    if (isTableEmpty("public.company")) {
      jdbcTemplate.execute(
        "INSERT INTO public.company (id, company_name) VALUES(1, 'PCCTH'),(2, 'WiseSoft');"
      );
      System.out.println("Data inserted into public.company table.");
    } else {
      System.out.println(
        "public.company table is not empty. No data inserted."
      );
    }
  }

  private void insertDataIntoSectorTable() {
    if (isTableEmpty("public.sector")) {
      jdbcTemplate.execute(
        "INSERT INTO public.sector (id, sector_code, sector_name,company_id) VALUES(1, 'SD', 'SD',1),(2, 'SD', 'SD',2);"
      );
      System.out.println("Data inserted into public.sector table.");
    } else {
      System.out.println("public.sector table is not empty. No data inserted.");
    }
  }

  private void insertDataIntoDepartmentTable() {
    if (isTableEmpty("public.department")) {
      jdbcTemplate.execute(
        "INSERT INTO public.department (id, dept_name, dept_code, sector_id) VALUES(1, 'SD1', '4011', 1),(2, 'SD2', '4012', 1),(3, 'SQA', '4013', 1),(4, 'SD1', '5000', 2);"
      );
      System.out.println("Data inserted into public.department table.");
    } else {
      System.out.println(
        "public.department table is not empty. No data inserted."
      );
    }
  }

  private void insertDataIntoPositionTable() {
    if (isTableEmpty("public.position")) {
      jdbcTemplate.execute(
        "INSERT INTO public.\"position\" (position_name, department_id)\r\n" + //
        "VALUES\r\n" + //
        "('VICE PRESIDENT', 1),\r\n" + //
        "('SR.APPLICATION SPECIALIST', 1),\r\n" + //
        "('APPLICATION SPECIALIST', 1),\r\n" + //
        "('SR.ADMIN OFFICER II', 1),\r\n" + //
        "('SR.SYSTEM ANALYST II', 1),\r\n" + //
        "('SR.SYSTEM ANALYST I', 1),\r\n" + //
        "('SR.APPLICATION SUPPORT', 1),\r\n" + //
        "('ADMIN.OFFICER II', 1),\r\n" + //
        "('SR.PROGRAMMER ANALYST I', 1),\r\n" + //
        "('SR.PROGRAMMER', 1),\r\n" + //
        "('PROGRAMMER ANALYST', 1),\r\n" + //
        "('PROGRAMMER II', 1),\r\n" + //
        "('PROGRAMMER I', 1),\r\n" + //
        "('PROGRAMMER III', 1),\r\n" + //
        "('SYSTEM ANALYST', 1),\r\n" + //
        "('BUSINESS ANALYST', 1),\r\n" + //
        "('SENIOR MANAGER', 2),\r\n" + //
        "('SR.APPLICATION SPECICALIST', 2),\r\n" + //
        "('SR.SYSTEM ANALYST II', 2),\r\n" + //
        "('SR.SYSTEM ANALYST I', 2),\r\n" + //
        "('SR.PROGRAMMER ANALYST I', 2),\r\n" + //
        "('PROGRAMMER II', 2),\r\n" + //
        "('PROGRAMMER III', 2),\r\n" + //
        "('SR.PROGRAMMER', 2),\r\n" + //
        "('SR. SYSTEM ANALYST I', 2),\r\n" + //
        "('PROGRAMMER I', 2),\r\n" + //
        "('SR.SQA ANALYST II', 3),\r\n" + //
        "('SR.MANAGER', 3),\r\n" + //
        "('SR.TESTER', 3),\r\n" + //
        "('TESTER III', 3),\r\n" + //
        "('SR.SQA ANALYST I', 3),\r\n" + //
        "('TESTER II', 3),\r\n" + //
        "('TESTER I', 3),\r\n" + //
        "('PROGRAMMER II',4),\r\n" + //
        "('SR.SYSTEM ANALYST I',4),\r\n" + //
        "('SR.SYSTEM ANALYST II',4),\r\n" + //
        "('PROGRAMMER III',4);"
      );
      System.out.println("Data inserted into public.position table.");
    } else {
      System.out.println(
        "public.position table is not empty. No data inserted."
      );
    }
  }

  private void insertDataIntoUsersTable() {
    String hashpassword = passwordEncoder.encode("1234");

    String sql =
      "INSERT INTO public.users (email,emp_code,firstname, lastname, password, telephone, company_id, department_id, position_id, sector_id) VALUES" +
      "('admin@pccth.com',null,'Admin', 'Admin','" + hashpassword + "', '+15555555555',1,1,8,1)," +
      "('approver@pccth.com',null,'Approver 1', 'Approver','" + hashpassword + "', '+15555555555', 1, 1, 4, 1)," +
      "('vicepresident@pccth.com',null,'VicePresident', 'VicePresident','" + hashpassword + "','+15555555555',1, 1, 1, 1)," +
      "('personnel@pccth.com',null,'Personnel', 'Personnel','" + hashpassword + "', '+15555555555', 1, 1, 4, 1)," +
      "('user@pccth.com',null ,'User', 'User','" + hashpassword + "', '+15555555555',1,1,13,1)," +
      "('2@pccth.com',null ,'Approver 2', '2', '" + hashpassword + "', '1',1,1,4,1)," +
      "('3@pccth.com',null ,'Approver 3', '3', '" + hashpassword + "', '1',1,1,4,1)," +
      "('4@pccth.com',null,'Approver 4', '4', '" + hashpassword + "', '1',1,1,4,1)," +
      "('5@pccth.com',null,'Approver 5', '5', '" + hashpassword + "', '1',1,1,4,1)," +
      "('6@pccth.com',null,'Approver 6', '6', '" + hashpassword + "', '1',1,1,4,1)," +
      "('khunanonk@pccth.com','123456','คุณานนท์','ครองขวัญ','" + hashpassword + "','0981234567',1,1,13,1)";

    if (isTableEmpty("public.users")) {
      jdbcTemplate.execute(sql);
      jdbcTemplate.execute(
        "INSERT INTO public.users_roles (user_id, roles_id) VALUES (1, 1),(2, 2),(3, 3),(4, 4),(5, 5),(6, 2),(7, 2),(8, 2),(9, 2),(10, 2),(11, 3)"
      );
      System.out.println("Data inserted into public.users table.");
    } else {
      System.out.println("public.users table is not empty. No data inserted.");
    }
  }

  private void insertBudgetTable() {
    String sql =
      "INSERT INTO public.budget (air_acc, class_name, \"exp\", fee, number_of_person, remark, \"year\", sector_id,company_id, department_id)\n" +
      "VALUES\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2022', 1,1,1),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2022', 1,1,1),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2022', 1,1,1),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,1),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,1),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,1),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,2),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,2),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,2),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,2),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,2),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,2),\n" +
      "    (10.0, 'test', 20.0, 10.0, 100, '', '2023', 1,1,2),\n" +
      "    (1000.0, 'string', 2000.0, 1000.0, 0, 'string', '2023', 1,1,2),\n" +
      "    (15000.0, 'string', 30000.0, 15000.0, 999, 'string', '2023', 1,1,1);";

    if (isTableEmpty("public.budget")) {
      jdbcTemplate.execute(sql);
      System.out.println("Data inserted into public.budget table.");
    } else {
      System.out.println("public.budget table is not empty. No data inserted.");
    }
  }
}
