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
        "INSERT INTO public.course (course_name, end_date, institute, note, place, price, start_date, time, objective,active,type) VALUES " +
        "('BA Fundamental', '2023-11-01 07:00:00.000', 'คุณนิทัศน์ ,คุณเงาะ ,คุณนักสู้ ,คุณชำนาญ', '5-6/10,10-12/10,16-30/10,1-10/11/2566', '102/99 ถนน ณ ระนอง คลองเตย กรุงเทพฯ', 0.0, '2023-10-05 07:00:00.000', '09:00-16:30', 'เพื่อนำมาใช้ในการปฎิบัติงาน', 'ดำเนินการอยู่', 'อบรม'), " +
        "('Introduction to Microservice รุ่น 5', '2023-08-11 07:00:00.000', 'คุณนิทัศน์ หวังวิบูลย์กิจ', NULL, 'Online ผ่าน Program Microsoft Teams', 0.0, '2023-08-10 07:00:00.000', '09:00-17:00', 'เพื่อนำมาใช้ในการปฎิบัติงาน', 'ดำเนินการอยู่', 'อบรม'), " +
        "('Soft Skillathon for IT Professional (Business Mindset and Diversity Mindset)', '2023-08-18 07:00:00.000', 'Software Park Thailand', '', 'Software Park Training Room 3rd floor', 10000.0, '2023-08-17 07:00:00.000', '09:00-16:00', 'เพื่อนำมาใช้ในการปฎิบัติงาน', 'ดำเนินการอยู่', 'อบรม'), " +
        "('DevOps : From Development to Deployment/Production', '2023-08-18 07:00:00.000', 'IMC Institute', NULL, '141/7 Skulthai Surawong Tower 8th FL,Surawong Road, · Suriyawong, Bangrak, Bangkok,Bangkok 10500', 11900.0, '2023-08-16 07:00:00.000', '09:00-16:30', 'เพื่อนำมาใช้ในการปฎิบัติงาน','ดำเนินการอยู่','อบรม'), " +
        "('Docker for Developers in Practice from Source Code to CI/CD', '2023-07-25 07:00:00.000', 'IMC Institute', NULL, '141/7 Skulthai Surawong Tower 8th FL,Surawong Road, · Suriyawong, Bangrak, Bangkok,Bangkok 10500', 9900.0, '2023-07-24 07:00:00.000', '09:00-16:30', 'เพื่อนำมาใช้ในการปฎิบัติงาน','ดำเนินการอยู่','อบรม'), " +
        "('ChatGPT for Management', '2023-06-26 07:00:00.000', 'IMC Institute', NULL, '141/7 Skulthai Surawong Tower 8th FL,Surawong Road, · Suriyawong, Bangrak, Bangkok,Bangkok 10500', 5900.0,  '2023-06-26 07:00:00.000', '09:00-16:30', 'เพื่อนำมาใช้ในการปฎิบัติงาน','ดำเนินการอยู่','อบรม'), " +
        "('Python for Programmers', '2023-03-31 07:00:00.000', 'Software Park Thailand', '', 'Software Park Training Room 3rd floor', 13000.0,  '2023-03-28 07:00:00.000', '09:00-16:00', 'เพื่อนำมาใช้ในการปฎิบัติงาน','ดำเนินการอยู่','อบรม'), " +
        "('Workshop Design Thinking', '2023-02-05 07:00:00.000', 'คุณเกรียงไกร  นิตรานนท์', '', 'อาคารพหลโยธินเพลส ชั้น 41', 700, '2023-02-04 07:00:00.000', '09:00-16:30', 'เพื่อนำมาใช้ในการปฎิบัติงาน','ดำเนินการอยู่','อบรม'), " +
        "('Basic Software Testing', '2023-02-28 07:00:00.000', 'IMC Institute', NULL, 'ผ่านโปรแกรม Zoom', 6900.0, '2023-02-27 07:00:00.000', '09:00-16:00', 'เพื่อนำมาใช้ในการปฎิบัติงาน','ดำเนินการอยู่','อบรม'), " +
        "('Basic Software สอบ', '2023-02-28 07:00:00.000', 'IMC Institute', NULL, 'ผ่านโปรแกรม Zoom', 6900.0,  '2023-02-27 07:00:00.000', '09:00-16:00', 'เพื่อนำมาใช้ในการปฎิบัติงาน','ดำเนินการอยู่','สอบ'), " +
        "('Advan ce SQL', '2023-02-23 07:00:00.000', 'คุณนิทัศน์ หวังวิบูลย์กิจ', NULL, 'ออนไลน์ ผ่าน Program Microsoft Teams', 0.0, '2023-02-02 07:00:00.000', '09:00-17:00', 'เพื่อนำมาใช้ในการปฎิบัติงาน', 'ยกเลิก', 'อบรม');"
      );
    }
  }

  private void insertDataIntoCompanyTable() {
    if (isTableEmpty("public.company")) {
      jdbcTemplate.execute(
        "INSERT INTO public.company (id, company_name) VALUES(1, 'PCCTH'),(2, 'WiseSoft');"
      );
    }
  }

  private void insertDataIntoSectorTable() {
    if (isTableEmpty("public.sector")) {
      jdbcTemplate.execute(
        "INSERT INTO public.sector (id, sector_code, sector_name,company_id)" +
        "VALUES(1, 'SD', 'SD',1)," +
        "(2, 'SD', 'SD',2)," +
        "(3, 'OSS', 'OSS',1)," +
        "(4, 'PS', 'PS',1)," +
        "(5, 'TOP', 'TOP',1)," +
        "(6, 'MS', 'MS',1)," +
        "(7, 'PBS', 'PBS',1)," +
        "(8, 'FSI', 'FSI',1)," +
        "(9, 'QMR', 'QMR',1)," +
        "(10, 'AF', 'AF',1)," +
        "(11, 'HQ', 'HQ',1);"
      );
    }
  }

  private void insertDataIntoDepartmentTable() {
    if (isTableEmpty("public.department")) {
      jdbcTemplate.execute(
        "INSERT INTO public.department (id, dept_name, dept_code, sector_id) VALUES " +
        "(1, 'SD1', '4011', 1), " +
        "(2, 'SD2', '4012', 1), " +
        "(3, 'SQA', '4013', 1), " +
        "(4, 'SD1', '5000', 2), " +
        "(5, 'FSI', '1011', 8), " +
        "(6, 'PBS', '2011', 7), " +
        "(7, 'MS', '3011', 6), " +
        "(8, 'SE', '5011', 5), " +
        "(9, 'CS', '5012', 5), " +
        "(10, 'CE', '5013', 5), " +
        "(11, 'NS', '5014', 5), " +
        "(12, 'SM', '5015', 5), " +
        "(13, 'OSS', '6011', 3), " +
        "(14, 'PM', '7011', 4), " +
        "(15, 'DT', '7012', 4), " +
        "(16, 'HQ', '8011', 11), " +
        "(17, 'AF', '9011', 10)"
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
    }
  }

  private void insertDataIntoUsersTable() {
    String hashpassword = passwordEncoder.encode("1234");

    String sql =
      "INSERT INTO public.users (email, emp_code, firstname, lastname, password, telephone, company_id, department_id, position_id, sector_id, status, title) VALUES " +
      "('admin@pccth.com', 000001, 'Admin', 'Admin', '" +
      hashpassword +
      "', '+1111111111', 1, 1, 8, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('approver@pccth.com', 000002, 'ประวิทย์', 'เถาว์พันธ์', '" +
      hashpassword +
      "', '+2222222222', 1, 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('vicepresident@pccth.com', 000003, 'VicePresident', 'VicePresident', '" +
      hashpassword +
      "','+3333333333', 1, 1, 1, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('personnel@pccth.com', 000004, 'Personnel', 'Personnel', '" +
      hashpassword +
      "', '+4444444444', 1, 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('2@pccth.com', 000005, 'สุริยา', ' จันทร', '" +
      hashpassword +
      "', '+6666666666', 1, 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('3@pccth.com', 000006, 'ณัทกฤช', 'เจริญธรรมทัด', '" +
      hashpassword +
      "', '+7777777777', 1, 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('4@pccth.com', 000007, 'วาณี', 'ธรรมาภิมุขกุล', '" +
      hashpassword +
      "', '+8888888888', 1, 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('5@pccth.com', 000008, 'พันชริทร์', 'โสภะ', '" +
      hashpassword +
      "', '+9999999999', 1, 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('6@pccth.com', 000009, 'เสาวภา', '6', '" +
      hashpassword +
      "', '+0000000000', 1, 2, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('7@pccth.com', 000010, 'ณัฐวรรณ', '7', '" +
      hashpassword +
      "', '1234567890', 1, 3, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('khunanonk@pccth.com', '123456', 'คุณานนท์', 'ครองขวัญ', '" +
      hashpassword +
      "','0981234567', 1, 1, 13, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminFSI@pccth.com', 10006, 'AdminFSI', 'AdminFSI', '" +
      hashpassword +
      "', '+1111111111', 1, 5, 8, 8, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminPBS@pccth.com', 10005, 'AdminPBS', 'AdminPBS', '" +
      hashpassword +
      "', '+1111111111', 1, 6, 8, 7, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminMS@pccth.com', 10004, 'AdminMS', 'AdminMS', '" +
      hashpassword +
      "', '+1111111111', 1, 1, 8, 6, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminOSS@pccth.com', 10003, 'AdminOSS', 'AdminOSS', '" +
      hashpassword +
      "', '+1111111111', 1, 13, 8, 3, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminHQ@pccth.com', 10001, 'AdminHQ', 'AdminHQ', '" +
      hashpassword +
      "', '+1111111111', 1, 16, 8, 11, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminAF@pccth.com', 10000, 'AdminAF', 'AdminAF', '" +
      hashpassword +
      "', '+1111111111', 1, 17, 8, 10, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userFSI@pccth.com', 20006, 'userFSI', 'userFSI', '" +
      hashpassword +
      "', '+1111111111', 1, 5, 4, 8, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userPBS@pccth.com', 20005, 'userPBS', 'userPBS', '" +
      hashpassword +
      "', '+1111111111', 1, 6, 4, 7, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userMS@pccth.com', 20004, 'userMS', 'userMS', '" +
      hashpassword +
      "', '+1111111111', 1, 1, 4, 6, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userOSS@pccth.com', 20003, 'userOSS', 'userOSS', '" +
      hashpassword +
      "', '+1111111111', 1, 13, 4, 3, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userHQ@pccth.com', 20001, 'userHQ', 'userHQ', '" +
      hashpassword +
      "', '+1111111111', 1, 16, 4, 11, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAF@pccth.com', 20000, 'userAF', 'userAF', '" +
      hashpassword +
      "', '+1111111111', 1, 17, 4, 10, 'เป็นพนักงานอยู่', 'นาย')";

    if (isTableEmpty("public.users")) {
      jdbcTemplate.execute(sql);
      jdbcTemplate.execute(
        "INSERT INTO public.users_roles (user_id, roles_id) VALUES (1, 1),(2, 2),(3, 3),(4, 4),(5,2),(6, 2),(7, 2),(8, 2),(9, 2),(10,2),(11,5),(12,1),(13,1),(14,1),(15,1),(16,1)," + 
            "(18,5),(19,5),(20,5),(21,5),(22,5)"
      );
    }
  }

  private void insertBudgetTable() {
    String sql =
      "INSERT INTO public.budget (budget_cer, budget_training, total_exp, \"year\", company_id, department_id)\n" +
      "VALUES\n" +
      "    (10.0,10,20,'2022', 1,1),\n" +
      "    (10.0,10,20,'2022', 1,1),\n" +
      "    (10.0,10,20,'2022', 1,1),\n" +
      "    (10.0,10,20,'2023', 1,1),\n" +
      "    (10.0,10,20,'2023', 1,1),\n" +
      "    (10.0,10,20,'2023', 1,1),\n" +
      "    (10.0,10,20,'2023', 1,2),\n" +
      "    (10.0,10,20,'2023', 1,2),\n" +
      "    (10.0,10,20,'2023', 1,2),\n" +
      "    (10.0,10,20,'2023', 1,2),\n" +
      "    (10.0,10,20,'2023', 1,2),\n" +
      "    (10.0,10,20,'2023', 1,2),\n" +
      "    (10.0,10,20,'2023', 1,2),\n" +
      "    (1000.0,500,1500,'2023',1,2),\n" +
      "    (15000.0,1500,3000, '2023', 1,1);";

    if (isTableEmpty("public.budget")) {
      jdbcTemplate.execute(sql);
    }
  }
}
