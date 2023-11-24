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
    // insertDataIntoUsersTable();
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
        "(11, 'HQ', 'HQ',1)," +
        "(12, 'ISO', 'ISO',1)," +
        "(13, 'ISO', 'ISO',2)," +
        "(14, 'HQ', 'HQ', 2),  " +
        "(15, 'AF', 'AF', 2),  " +
        "(16, 'MK', 'MK', 2),  " +
        "(17, 'APS', 'APS', 2),  " +
        "(18, 'APS (BOI)', 'APS (BOI)', 2),  " +
        "(19, 'OSS', 'OSS', 2),  " +
        "(20, 'OSS (BOI)', 'OSS (BOI)', 2),  " +
        "(21, 'TOP', 'TOP', 2),  " +
        "(22, 'TOP (BOI)', 'TOP (BOI)', 2),  " +
        "(23, 'SD (BOI)', 'SD (BOI)', 2)  "
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
        "(17, 'AF', '9011', 10)," +
        "(18, 'ISO', '8011', 11), " +
        "(19 ,'PS', '0000', 4)," +
        "(20, 'TOP', '8011', 5), " +
        "(21, 'SI', '3011', 6),  " +
        "(22, 'BD', '3011', 6),  " +
        "(23, 'IS', '3011', 6),  " +
        "(24, 'AP', '3011', 6),  " +
        "(25, 'FM', '6011', 3),  " +
        "(26, 'LG', '8011', 11),  " +
        "(27, 'PN', '8011', 11),  " +
        "(28, 'AD', '8011', 11),  " +
        "(29, 'AC', '9011', 10),  " +
        "(30, 'FN', '9011', 10),  " +
        "(31, 'HQ', '1000', 14),  " +
        "(32, 'AF', '1000', 15),  " +
        "(33, 'MK', '2000', 16),  " +
        "(34, 'APS', '2100', 17),  " +
        "(35, 'APS(BOI)', '2200', 18),  " +
        "(36, 'OSS', '3000', 19),  " +
        "(37, 'OSS(BOI)', '3100', 20),  " +
        "(38, 'CS', '4000', 21),  " +
        "(39, 'CE', '4000', 21),  " +
        "(40, 'SM', '4000', 21),  " +
        "(41, 'NS', '4100', 22),  " +
        "(42, 'SD', '5000', 23)  "
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
        "('PROGRAMMER III', 3),\r\n" + //
        "('SR.PROGRAMMER', 42),\r\n" + //
        "('SR. SYSTEM ANALYST I', 5),\r\n" + //
        "('PROGRAMMER I', 6),\r\n" + //
        "('SR.SQA ANALYST II', 7),\r\n" + //
        "('SR.MANAGER', 8),\r\n" + //
        "('SR.TESTER', 9),\r\n" + //
        "('TESTER III', 10),\r\n" + //
        "('SR.SQA ANALYST I', 11),\r\n" + //
        "('TESTER II', 12),\r\n" + //
        "('TESTER I', 25),\r\n" + //
        "('PROGRAMMER II',14),\r\n" + //
        "('SR.SYSTEM ANALYST I',15),\r\n" + //
        "('SR.SYSTEM ANALYST II',16),\r\n" + //
        //"('PROGRAMMER III',17)," +
        "('Manager_Senior Manager', 22)," +
        "('Business Development Specialist_Senior', 22)," +
        "('Business Development_Senior', 22)," +
        "('Quality Management Representative', 18)," +
        "('Document and Data Control', 18)," +
        "('Manager / Senior Manager', 3)," +
        "('Assistant Manager I', 3)," +
        "('Assistant Manager II', 3)," +
        "('SQA Specialist / Senior SQA Specialist', 3)," +
        "('Senior SQA Analyst I', 3)," +
        "('Senior SQA Analyst II', 3)," +
        "('SQA Analyst', 3)," +
        "('Senior Tester', 3)," +
        "('Tester I', 3)," +
        "('Tester II', 3)," +
        "('Tester III', 3)," +
        "('Manager_Senior Manager', 23)," +
        "('IT Specialist Senior IT Consultant', 23)," +
        "('IT Administrator Senior', 23)," +
        "('Vice President', 25)," +
        "('Asst. Vice President', 25)," +
        "('FM Manager', 25)," +
        "('FSVP-Services Group', 19)," +
        "('Vice President (8B)', 19)," +
        "('Asst.Vice President (8C)', 19)," +
        "('Manager', 25)," +
        "('Asst. Manager', 25)," +
        "('SQA Specialist', 25)," +
        "('SQA_Analyst', 25)," +
        "('Application_Support', 25)," +
        "('Legal Manager_Sr.Legal Manager', 26)," +
        "('Sr.Legal Specialist I', 26)," +
        "('Sr.Legal Specialist II', 26)," +
        "('Legal Specialist I', 26)," +
        "('Legal Specialist II', 26)," +
        "('Sr.Legal Officer I', 26)," +
        "('Sr.Legal Officer II', 26)," +
        "('Admin Staff I', 26)," +
        "('Admin Staff II', 26)," +
        "('Admin Staff III', 26)," +
        "('Legal Officer I', 26)," +
        "('Legal Officer II', 26)," +
        "('Legal Officer III', 26)," +
        "('Finance Officer I', 30)," +
        "('Finance Officer II', 30)," +
        "('Finance Officer III', 30)," +
        "('Cashier', 30)," +
        "('Bill Collector', 30)," +
        "('Vice President', 20)," +
        "('Assistant Vice President', 20)," +
        "('Sr. Personnel Manager', 27)," +
        "('Personnel Manager', 27)," +
        "('Assistant Manager I PN', 27)," +
        "('Assistant Manager II PN', 27)," +
        "('Assistant Manager I (Payroll)', 27)," +
        "('Assistant Manager II (Payroll)', 27)," +
        "('Sr. HR Specialist I', 27)," +
        "('Sr. HR Specialist II', 27)," +
        "('HR Specialist I', 27)," +
        "('HR Specialist II', 27)," +
        "('Sr. HR Officer I', 27)," +
        "('Sr. HR Officer II', 27)," +
        "('Sr. Payroll Officer I', 27)," +
        "('Sr. Payroll Officer II', 27)," +
        "('HR Officer I', 27)," +
        "('HR Officer II', 27)," +
        "('HR Officer III', 27)," +
        "('Payroll Officer I', 27)," +
        "('Payroll Officer II', 27)," +
        "('Payroll Officer III', 27)," +
        "('Officer Admin Manager_7B', 28)," +
        "('Sr. Admin Officer IV_6A', 28)," +
        "('Sr. Admin Officer III_6B', 28)," +
        "('Sr. Admin Officer II_5A', 28)," +
        "('Sr. Admin Officer I_5B', 28)," +
        "('Admin Officer I-III 3A-4A', 28)," +
        "('Admin Officer II 3A-4A', 28)," +
        "('Admin Officer III 3A-4A', 28)," +
        "('Admin Staff I', 28)," +
        "('Admin Staff II', 28)," +
        "('Admin Staff III', 28)," +
        "('Driver', 28)," +
        "('Accounting Officer I', 29)," +
        "('Accounting Officer II', 29)," +
        "('Accounting Officer III', 29)," +
        "('Account Admin I', 29)," +
        "('Account Admin II', 29)," +
        "('Account Admin III', 29)," +
        "('Sr.Accounting Officer I', 29)," +
        "('Sr.Accounting Officer II', 29)," +
        "('Sr.Manager', 10)," +
        "('Sr.Technical Specialist I', 10)," +
        "('Sr.Helpdesk Lead', 10)," +
        "('IT Specialist I', 10)," +
        "('IT Specialist II', 10)," +
        "('Sr.Computer Engineer I', 10)," +
        "('Sr.Computer Engineer II', 10)," +
        "('Sr.Technician I', 10)," +
        "('Sr.Technician II', 10)," +
        "('Computer Engineer I', 10)," +
        "('Computer Engineer II', 10)," +
        "('Computer Engineer III', 10)," +
        "('HelpDesk I', 10)," +
        "('HelpDesk II', 10)," +
        "('HelpDesk III', 10)," +
        "('Store Officer I', 10)," +
        "('Store Officer II', 10)," +
        "('Store Officer III', 10)," +
        "('Technical Support I', 10)," +
        "('Technical Support II', 10)," +
        "('Technical Support III', 10)," +
        "('Technician I', 10)," +
        "('Technician II', 10)," +
        "('Technician III', 10)," +
        "('Manager', 25)," +
        "('Asst_Manager', 25)," +
        "('System_Engineer', 25)," +
        "('Network_Analyst', 25)," +
        "('Network_Engineer', 25)," +
        "('DBA', 25)," +
        "('Manager and Sr.Manager', 11)," +
        "('Asst.Manager I', 11)," +
        "('Asst.Manager II', 11)," +
        "('IT Specialist I', 11)," +
        "('IT Specialist II', 11)," +
        "('Network Analyst and Sr.Network Analyst', 11)," +
        "('Cyber Security System Analyst and Sr.Cyber Security System Analyst', 11)," +
        "('Sr.Computer Engineer I', 11)," +
        "('Sr.Computer Engineer II', 11)," +
        "('Sr.Network Engineer I', 11)," +
        "('Sr.Network Engineer II', 11)," +
        "('Sr.Cyber Security Engineer I', 11)," +
        "('Sr.Cyber Security Engineer II', 11)," +
        "('Computer Engineer I', 11)," +
        "('Computer Engineer II', 11)," +
        "('Computer Engineer III', 11)," +
        "('Network Engineer I', 11)," +
        "('Network Engineer II', 11)," +
        "('Network Engineer III', 11)," +
        "('Cyber Security Engineer II', 11)," +
        "('Cyber Security Engineer III', 11)," +
        "('Driver', 11)," +
        "('Vice President', 7)," +
        "('Advisor', 7)," +
        "('Admin Officer_Senior', 7)," +
        "('Manager', 25)," +
        "('Assistant Manager', 25)," +
        "('Application Specialist', 25)," +
        "('System Analyst', 25)," +
        "('Programmer', 25)," +
        "('Operation Support', 25)," +
        "('Sr_Admin_Officer', 25)," +
        "('Admin_Officer', 25)," +
        "('Driver', 25)," +
        "('Manager', 25)," +
        "('Asst_Manager', 25)," +
        "('Mainframe_Specialist', 25)," +
        "('Sr_System_Programmer', 25)," +
        "('System_Programmer', 25)," +
        "('Manager', 9)," +
        "('Asst. Manager I', 9)," +
        "('Asst. Manager II', 9)," +
        "('IT Specialist I', 9)," +
        "('IT Specialist II', 9)," +
        "('Sr. Engr I', 9)," +
        "('Sr. Engr II', 9)," +
        "('Engr I', 9)," +
        "('Engr II', 9)," +
        "('Asst. TS Manager I', 9)," +
        "('Asst. TS Manager II', 9)," +
        "('Sr. Technical Support I', 9)," +
        "('Sr. Technical Support |I', 9)," +
        "('Sr. Technical Support III', 9)," +
        "('Technical Support I', 9)," +
        "('Technical Support II', 9)," +
        "('Technical Support III', 9)," +
        "('Admin Officer I', 9)," +
        "('Admin Officer II', 9)," +
        "('Admin Officer III', 9)," +
        "('Sr. Admin Officer I', 9)," +
        "('Sr. Admin Officer II', 9)," +
        "('Sr. Admin Officer III', 9)," +
        "('Sr. Admin Officer IV', 9)," +
        "('Admin Admin I', 9)," +
        "('Sr. Manager', 9)," +
        "('Vice President', 6)," +
        "('MK Group_Executive Vice President', 6)," +
        "('Manager_Sr.Manager (Sale Support)', 6)," +
        "('Assistant Manager I (Business Consultant)', 6)," +
        "('Assistant Manager II (Business Consultant)', 6)," +
        "('Business Consultant I', 6)," +
        "('Business Consultant II', 6)," +
        "('Business Consultant Specialist I', 6)," +
        "('Business Consultant Specialist II', 6)," +
        "('Jr. Business Consultant', 6)," +
        "('Admin_Sr.Admin Officer I', 6)," +
        "('Admin_Sr.Admin Officer II', 6)," +
        "('Admin_Sr.Admin Officer III', 6)," +
        "('Admin_Sr.Admin Officer IV', 6)," +
        "('Sale Support_Sr.Sale Support', 6)," +
        "('Manager/Sr.Manager', 6)," +
        "('Sr. Business Consultant Specialist I', 6)," +
        "('Sr. Business Consultant Specialist II', 6)," +
        "('Sr. Business Consultant Specialist III', 6)," +
        "('Asst_Project_Manager', 25)," +
        "('Project_Coordinator', 25)," +
        "('Manager / Senior Manager', 8)," +
        "('Assistant Manager I', 8)," +
        "('Assistant Manager II', 8)," +
        "('Data Engineer I', 8)," +
        "('Data Engineer II', 8)," +
        "('Data Engineer III', 8)," +
        "('Data Engineer IV', 8)," +
        "('Data Engineer V', 8)," +
        "('Data Engineer Specialist / Senior Data Engineer Specialist', 8)," +
        "('Senior Data Engineer I', 8)," +
        "('Senior Data Engineer II', 8)," +
        "('DevOps Engineer I', 8)," +
        "('DevOps Engineer II', 8)," +
        "('DevOps Engineer III', 8)," +
        "('DevOps Engineer IV', 8)," +
        "('DevOps Engineer V', 8)," +
        "('Data Engineer Expert', 8)," +
        "('DevOps Engineer Specialist / Senior DevOps Engineer Specialist', 8)," +
        "('Senior DevOps Engineer I', 8)," +
        "('Senior DevOps Engineer II', 8)," +
        "('Enterprise Architect / Senior Enterprise Architect I', 8)," +
        "('Enterprise Architect / Senior Enterprise Architect II', 8)," +
        "('DevOps Engineer Expert', 8)," +
        "('Enterprise Architect Expert', 8)," +
        "('Enterprise Architect Specialist / Senior Enterprise Architect Specialist', 8)," +
        "('Senior Site Reliability Engineer I', 8)," +
        "('Senior Site Reliability Engineer II', 8)," +
        "('Site Reliability Engineer I', 8)," +
        "('Site Reliability Engineer II', 8)," +
        "('Site Reliability Engineer III', 8)," +
        "('Site Reliability Engineer IV', 8)," +
        "('Site Reliability Engineer V', 8)," +
        "('Site Reliability Engineer Expert', 8)," +
        "('Site Reliability Engineer Specialist / Senior DataSite Reliability Engineer Specialist', 8)," +
        "('Senior System Engineer I', 8)," +
        "('Senior System Engineer II', 8)," +
        "('System Engineer I', 8)," +
        "('System Engineer II', 8)," +
        "('System Engineer III', 8)," +
        "('System Engineer IV', 8)," +
        "('System Engineer V', 8)," +
        "('System Engineer Expert', 8)," +
        "('System Engineer Specialist / Senior System Engineer Specialist', 8)," +
        "('Technical Support I', 8)," +
        "('Technical Support II', 8)," +
        "('Technical Support III', 8)," +
        "('Technical Support IV', 8)," +
        "('Technical Support V', 8)," +
        "('Technology Strategist / Senior Technology Strategist I', 8)," +
        "('Technology Strategist / Senior Technology Strategist II', 8)," +
        "('Technology Strategist Specialist / Senior Technology Strategist Specialist', 8)," +
        "('Technology Strategist Expert', 8)," +
        "('Admin Officer I / Senior Admin Officer', 8)," +
        "('Admin Officer II / Senior Admin Officer', 8)," +
        "('Admin Officer III / Senior Admin Officer', 8)," +
        //        "('Vice President', 17)," +
        //        "('Assistant Vice President', 17)," +
        "('Manager', 25)," +
        "('Asst_Manager', 25)," +
        "('Sr_System_Operator', 25)," +
        "('System_Operator', 25)," +
        "('Manager_Senior Manager', 24)," +
        "('Purchasing Specialist', 24)," +
        "('Purchasing Officer_Senior', 24)," +
        "('Asst. Vice President', 5)," +
        "('Admin_Sr.Admin Officer I', 5)," +
        "('Admin_Sr.Admin Officer II', 5)," +
        "('Admin_Sr.Admin Officer III', 5)," +
        "('Admin_Sr.Admin Officer IV', 5)," +
        "('Vice President', 5)," +
        "('Business Consultant Specialist I', 5)," +
        "('Business Consultant Specialist II', 5)," +
        "('Manager_Sr.Manager (Sale Support)', 5)," +
        "('Sr.Business Consultant I', 5)," +
        "('Sr.Business Consultant II', 5)," +
        "('Business Consultant I', 5)," +
        "('Business Consultant II', 5)," +
        "('Sale Support_Sr.Sale Support', 5)," +
        "('Manager/Sr.Manager', 5)," +
        "('President', 16)," +
        "('Executive Secretary', 16)," +
        "('Advisor', 16)," +
        "('Secretary_Sr. Secretary', 16)," +
        "('Sr. Vice President Chief Architect', 16)," +
        "('CEO', 16)," +
        "('SR-Manager (Chief Architect)', 16)," +
        "('Programmer (Chief Architect)', 16)," +
        "('Manager_Sr.Manager (Sale Support)', 16)," +
        "('Manager (7A)', 14)," +
        "('Sr.Project Manager (7A)', 14)," +
        "('Project Manager (7B)', 14)," +
        "('Sr.Asst. Project Manager (6A)', 14)," +
        "('Asst. Project Manager (6B)', 14)," +
        "('Lead Project Co_6B-6A', 14)," +
        "('Sr.Project Co_5B-5A', 14)," +
        "('Project Co_4A', 14)," +
        "('Project Co_3A-4B', 14)," +
        "('Sr.Admin IV (6A)', 14)," +
        "('Sr.Admin III (6B)', 14)," +
        "('Sr.Admin II (5A)', 14)," +
        "('Sr.Admin I (5B)', 14)," +
        "('Admin III (4A)', 14)," +
        "('Admin II (4B)', 14)," +
        "('Admin I (3A)', 14)," +
        "('Technical Consultant', 21)," +
        "('Solution Architect', 21)," +
        "('Solutions Specialist  Senior', 21)," +
        "('Technical Specialist Senior', 21)," +
        "('Technical Engineer Senior', 21)," +
        "('Manager_Senior Manager', 21)," +
        "('SR.Project Manager', 12)," +
        "('Project Manager', 12)," +
        "('Assitant Project Manger and Senior', 12)," +
        "('Project Coordinator Lead and Senior', 12)," +
        "('Sr Project Coordinator I', 12)," +
        "('Project Coordinator I', 12)," +
        "('Sr Project Coordinator II', 12)," +
        "('Project Coordinator II', 12)," +
        "('Sr.DT Manager (7A)', 15)," +
        "('DT Manager (7B)', 15)," +
        "('Sr. DT Officer II (5A)', 15)," +
        "('Sr. DT Officer I (5B)', 15)," +
        "('DT Officer III (4A)', 15)," +
        "('DT Officer II (4B)', 15)," +
        "('DT Officer I (3A)', 15)," +
        "('Assistant DT Manager (6B)', 15)," +
        "('Java and DB II Instructor', 15)," +
        "('Sr. Asst. DT Manager (6A)', 15)," +
        "('Instructor', 15)," +
        "('PROGRAMMER I' , 33)," +
        "('PROGRAMMER I', 34)," +
        "('PROGRAMMER I', 35)," +
        "('PROGRAMMER I', 36)," +
        "('PROGRAMMER I', 37)," +
        "('PROGRAMMER I', 38)," +
        "('PROGRAMMER I', 39)," +
        "('PROGRAMMER I', 40)," +
        "('PROGRAMMER I', 41)," +
        "('PROGRAMMER I', 31)," +
        "('PROGRAMMER I', 32)," +
        "('PROGRAMMER I', 42)," +
        "('PROGRAMMER II', 42)," +
        "('PROGRAMMER III', 42)," +
        "('PROGRAMMER IIII', 42)," +
        "('SR.SYSTEM ANALYST III', 42)," +
        "('SR.SYSTEM ANALYST II', 42)," +
        "('SR.SYSTEM ANALYST I', 42)," +
        "('SR.SYSTEM ANALYST I', 41)"
      );
    }
  }

  private void insertDataIntoUsersTable() {
    String hashpassword = passwordEncoder.encode("1234");

    String sql =
      "INSERT INTO public.users (email, emp_code, firstname, lastname, password, telephone, company_id, position_id, sector_id, status, title) VALUES " +
      "('admin@pccth.com', 000001, 'Admin', 'Admin', '" +
      hashpassword +
      "', '+1111111111', 1, 8, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('approver@pccth.com', 000002, 'ประวิทย์', 'เถาว์พันธ์', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('vicepresident@pccth.com', 000003, 'VicePresident', 'VicePresident', '" +
      hashpassword +
      "','+3333333333', 1, 1, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('personnel@pccth.com', 000004, 'Personnel', 'Personnel', '" +
      hashpassword +
      "', '+4444444444', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('2@pccth.com', 000005, 'สุริยา', ' จันทร', '" +
      hashpassword +
      "', '+6666666666', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('3@pccth.com', 000006, 'ณัทกฤช', 'เจริญธรรมทัด', '" +
      hashpassword +
      "', '+7777777777', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('4@pccth.com', 000007, 'วาณี', 'ธรรมาภิมุขกุล', '" +
      hashpassword +
      "', '+8888888888', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('5@pccth.com', 000008, 'พันชริทร์', 'โสภะ', '" +
      hashpassword +
      "', '+9999999999', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('6@pccth.com', 000009, 'เสาวภา', '6', '" +
      hashpassword +
      "', '+0000000000', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('7@pccth.com', 000010, 'ณัฐวรรณ', '7', '" +
      hashpassword +
      "', '1234567890', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('khunanonk@pccth.com', '123456', 'คุณานนท์', 'ครองขวัญ', '" +
      hashpassword +
      "','0981234567', 1, 13, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminFSI@pccth.com', 10006, 'วาสนา', 'ยีตา', '" +
      hashpassword +
      "', '+1111111111', 1, 8, 8, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminPBS@pccth.com', 10005, 'กมลพรรณ', 'ฉิมจารย์', '" +
      hashpassword +
      "', '+1111111111', 1, 8, 7, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminMS@pccth.com', 10004, 'นภาขวัญ', 'แซ่อึ้ง', '" +
      hashpassword +
      "', '+1111111111', 1, 8, 6, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminOSS@pccth.com', 10003, 'รติรส', 'กุญแจทอง', '" +
      hashpassword +
      "', '+1111111111', 1, 8, 3, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminLG@pccth.com', 10001, 'รุ่งกานต์', 'อินทรีประจง', '" +
      hashpassword +
      "', '+1111111111', 1, 8, 14, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminPN&AD@pccth.com', 10002, 'อรทัย', 'เพืยศักดิ์', '" +
      hashpassword +
      "', '+1111111111', 1,  8, 11, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminAF@pccth.com', 10000, 'ชมพูนุท', 'ชัยสุขโกศล', '" +
      hashpassword +
      "', '+1111111111', 1,  8, 10, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminSD@pccth.com', 10009, 'ชณัญพัชร์', 'มิลินภรพิศุทธ์', '" +
      hashpassword +
      "', '+1111111111', 1,  8, 1, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminSE@pccth.com', 90000, 'อุทัยวรรณ', 'กิมศรี', '" +
      hashpassword +
      "', '+1111111111', 1,  8, 5, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminCS&CE&SM@pccth.com', 10399, 'นันทิดา', 'แสงเตือน', '" +
      hashpassword +
      "', '+1111111111', 1,  8, 5, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminNS@pccth.com', 10400, 'ศิริรัตน์', 'ชาลียุทธ', '" +
      hashpassword +
      "', '+1111111111', 1,  8, 5, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminPM@pccth.com', 10430, 'สิริรัตน', 'ทรงสอาด', '" +
      hashpassword +
      "', '+1111111111', 1,  8, 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('adminDT@pccth.com', 10410, 'ประภัสสร', 'ศรีสังข์', '" +
      hashpassword +
      "', '+1111111111', 1, 8, 10, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userFSI@pccth.com', 20006, 'userFSI', 'userFSI', '" +
      hashpassword +
      "', '+1111111111', 1, 4, 8, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userPBS@pccth.com', 20005, 'userPBS', 'userPBS', '" +
      hashpassword +
      "', '+1111111111', 1, 4, 7, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userMS@pccth.com', 20004, 'userMS', 'userMS', '" +
      hashpassword +
      "', '+1111111111', 1, 4, 6, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userOSS@pccth.com', 20003, 'userOSS', 'userOSS', '" +
      hashpassword +
      "', '+1111111111', 1, 4, 3, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userHQ@pccth.com', 20001, 'userHQ', 'userHQ', '" +
      hashpassword +
      "', '+1111111111', 1,  4, 11, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAF@pccth.com', 20000, 'userAF', 'userAF', '" +
      hashpassword +
      "', '+1111111111', 1,  4, 10, 'เป็นพนักงานอยู่', 'นาย')," +
      "('adminAPS@pccth.com', 2100, 'adminAPS', 'adminAPS', '" +
      hashpassword +
      "', '+1111111111', 2,  8, 17, 'เป็นพนักงานอยู่', 'นาย')," +
      "('adminHQ@pccth.com', 20200, 'adminHQ', 'adminHQ', '" +
      hashpassword +
      "', '+1111111111', 2,  8, 14, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverSD1@pccth.com',7000, 'approverSD1', 'approverSD1', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 1, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverSD2@pccth.com',7001, 'approverSD2', 'approverSD2', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 2, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverSQA@pccth.com',7002, 'approverSQA', 'approverSQA', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 3, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverFSI@pccth.com',7003, 'approverFSI', 'approverFSI', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 8, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverPBS@pccth.com',7004, 'approverPBS', 'approverPBS', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 7, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverMS@pccth.com',7005, 'approverMS', 'approverMS', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 6, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverSE@pccth.com',7006, 'approverSE', 'approverSE', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 5, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverCS@pccth.com',7007, 'approverCS', 'approverCS', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 5, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverCE@pccth.com',7008, 'approverCE', 'approverCE', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 5, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverNS@pccth.com',7009, 'approverNS', 'approverNS', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 5, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverSM@pccth.com',7010, 'approverSM', 'approverSM', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 5, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverOSS@pccth.com',7011, 'approverOSS', 'approverOSS', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 3, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverPM@pccth.com',7012, 'approverPM', 'approverPM', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 4, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverDT@pccth.com',7013, 'approverDT', 'approverDT', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 4, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverHQ@pccth.com',7014, 'approverHQ', 'approverHQ', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 11, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverAF@pccth.com',7015, 'approverAF', 'approverAF', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 10, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverISO@pccth.com',7016, 'approverISO', 'approverISO', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 12, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverPS@pccth.com',7017, 'approverPS', 'approverPS', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 4, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverTOP@pccth.com',7018, 'approverTOP', 'approverTOP', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 5, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverSI@pccth.com',7019, 'approverSI', 'approverSI', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 6, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverBD@pccth.com',7020, 'approverBD', 'approverBD', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 6, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverIS@pccth.com',7021, 'approverIS', 'approverIS', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 6, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverAP@pccth.com',7022, 'approverAP', 'approverAP', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 6, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverFM@pccth.com',7023, 'approverFM', 'approverFM', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 3, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverLG@pccth.com',7024, 'approverLG', 'approverLG', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 11, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverPN@pccth.com',7025, 'approverPN', 'approverPN', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 11, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverAD@pccth.com',7026, 'approverAD', 'approverAD', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 11, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverAC@pccth.com',7027, 'approverAC', 'approverAC', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 10, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverFN@pccth.com',7028, 'approverFN', 'approverFN', '" +
      hashpassword +
      "', '+2222222222', 1, 4, 10, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverMK@pccth.com',7029, 'approverMK', 'approverMK', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 16, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverAPS@pccth.com',7030, 'approverAPS', 'approverAPS', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 17, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverAPS_BOI@pccth.com',7031, 'approverAPS(BOI)', 'approverAPS(BOI)', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 18, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverOSS_BOI@pccth.com',7032, 'approverOSS(BOI)', 'approverOSS(BOI)', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 20, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverSD_BOI@pccth.com',7033, 'approverSD(BOI)', 'approverSD(BOI)', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 23, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverTOP2CS@pccth.com',7052, 'approverOSS(BOI)', 'approverOSS(BOI)', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 21, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverTOP2CE@pccth.com',7053, 'approverOSS(BOI)', 'approverOSS(BOI)', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 21, 'เป็นพนักงานอยู่', 'นาย')," +
      "('approverTOP2SM@pccth.com',7054, 'approverOSS(BOI)', 'approverOSS(BOI)', '" +
      hashpassword +
      "', '+2222222222', 2, 4, 21, 'เป็นพนักงานอยู่', 'นาย')," +

              "('vicepresidentSD_PCC@pccth.com', 34566, 'VicePresidentSD_PCC', 'VicePresident', '" +
              hashpassword +
              "','+3333333333', 1, 1, 1, 'เป็นพนักงานอยู่', 'นาย'), " +

              "('vicepresidentSD_wire@pccth.com', 67889, 'VicePresidentSD_wire', 'VicePresident', '" +
              hashpassword +
              "','+3333333333', 2, 1, 1, 'เป็นพนักงานอยู่', 'นาย'), " +

              "('vicepresidentTOP@pccth.com', 043623, 'VicePresidentTOP', 'VicePresident', '" +
              hashpassword +
              "','+3333333333', 1, 1, 5, 'เป็นพนักงานอยู่', 'นาย')";


    if (isTableEmpty("public.users")) {
      jdbcTemplate.execute(sql);
      jdbcTemplate.execute(
        "INSERT INTO public.users_roles (user_id, roles_id) VALUES (1, 1),(2, 2),(3, 3),(4, 4),(5,2),(6, 2),(7, 2),(8, 2),(9, 2),(10,2),(11,5),(12,1),(13,1),(14,1),(15,1),(16,1)," +
        "(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,5),(26,5),(27,5),(28,5),(29,5),(30,5),(31,1),(32,1),(33,2)," +
        "(34,2),(35,2),(36,2),(37,2),(38,2),(39,2)," +
        "(40,2),(41,2),(42,2),(43,2),(44,2),(45,2),(46,2),(47,2),(48,2),(49,2)," +
        "(50,2),(51,2),(52,2),(53,2),(54,2),(55,2),(56,2),(57,2),(58,2),(59,2)," +
        "(60,2),(61,2),(62,2),(63,2),(64,2),(65,2),(66,2),(67,2),(68,2),(69,2)," +
              "(70,3),(71,3),(72,3)"

      );
      jdbcTemplate.execute(
        "INSERT INTO public.user_department (user_id, department_id) VALUES " +
        "(1,1),(2,1),(3,1),(3,2),(3,3),(3,42),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1)," +
        "(12,5),(13,6),(16,26),(17,27),(17,28),(19,1),(20,8),(21,9),(21,10),(21,12)," +
        "(22,11),(23,14),(24,15),(25,5),(26,6),(27,7),(28,13),(29,16),(30,17),(21,38),(21,39),(21,40)," +
        "(14,21),(14,22),(14,23),(14,24),(15,25),(15,36),(15,37),(18,29),(18,30),(18,32),(19,2),(19,3)," +
        "(22,41),(12,33),(31,34),(31,35),(19,42),(32,31),(33,1)," +
        "(34,2),(35,3),(36,5),(37,6),(38,7),(39,8)," +
        "(40,38),(41,39),(42,41),(43,40),(44,36),(45,14),(46,15),(47,31),(48,32),(49,18)," +
        "(50,19),(51,20),(52,21),(53,22),(54,23),(55,24),(56,25),(57,26),(58,27),(59,28)," +
        "(60,29),(61,30),(62,33),(63,34),(64,35),(65,37),(66,42),(67,38),(68,39),(69,40)," +
                "(70,1),(70,2),(70,3),(71,42),(72,8),(72,9),(72,10),(72,11),(72,12),(72,38),(72,39),(72,40),(72,41)"
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
      "    (1500.0,1500,3000, '2023', 1,1);";

    if (isTableEmpty("public.budget")) {
      jdbcTemplate.execute(sql);
    }
  }
}
