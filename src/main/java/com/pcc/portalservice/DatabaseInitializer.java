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
    // insertDataIntoCourseTable();
    insertDataIntoCompanyTable();
    insertDataIntoSectorTable();
    insertDataIntoDepartmentTable();
    insertDataIntoPositionTable();
    // insertDataIntoUsersTable();
    // insertBudgetTable();
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
    if (isTableEmpty("sector")) {
      jdbcTemplate.execute(
        "INSERT INTO sector (sector_code, sector_name,company_id)" +
        "VALUES('SD', 'SD',1)," +
        "('SD', 'SD',2)," +
        "('OSS', 'OSS',1)," +
        "('PS', 'PS',1)," +
        "( 'TOP', 'TOP',1)," +
        "( 'MS', 'MS',1)," +
        "( 'PBS', 'PBS',1)," +
        "( 'FSI', 'FSI',1)," +
        "( 'QMR', 'QMR',1)," +
        "( 'AF', 'AF',1)," +
        "( 'HQ', 'HQ',1)," +
        "( 'ISO', 'ISO',1)," +
        "( 'ISO', 'ISO',2)," +
        "( 'HQ', 'HQ', 2),  " +
        "( 'AF', 'AF', 2),  " +
        "( 'MK', 'MK', 2),  " +
        "( 'APS', 'APS', 2),  " +
        "( 'APS (BOI)', 'APS (BOI)', 2),  " +
        "( 'OSS', 'OSS', 2),  " +
        "( 'OSS (BOI)', 'OSS (BOI)', 2),  " +
        "( 'TOP', 'TOP', 2),  " +
        "( 'TOP (BOI)', 'TOP (BOI)', 2),  " +
        "( 'SD (BOI)', 'SD (BOI)', 2) , " +
        "('TOP Management','TOP Management',2)," +
        "('TOP Management','TOP Management',1)"
      );
    }
  }

  private void insertDataIntoDepartmentTable() {
    if (isTableEmpty("public.department")) {
      jdbcTemplate.execute(
        "INSERT INTO public.department (dept_name, dept_code, sector_id) VALUES " +
        "('SD1', '4011', 1), " +
        "('SD2', '4012', 1), " +
        "('SQA', '4013', 1), " +
        "('SD1', '5000', 2), " +
        "('FSI', '1011', 8), " +
        "('PBS', '2011', 7), " +
        "('MS', '3011', 6), " +
        "('SE', '5011', 5), " +
        "('CS', '5012', 5), " +
        "('CE', '5013', 5), " +
        "('NS', '5014', 5), " +
        "('SM', '5015', 5), " +
        "('OSS', '6011', 3), " +
        "('PM', '7011', 4), " +
        "('DT', '7012', 4), " +
        "('HQ', '8011', 11), " +
        "('AF', '9011', 10)," +
        "('ISO', '8011', 11), " +
        "('PS', '0000', 4)," +
        "('TOP', '8011', 5), " +
        "('SI', '3011', 6),  " +
        "('BD', '3011', 6),  " +
        "('IS', '3011', 6),  " +
        "('AP', '3011', 6),  " +
        "('FM', '6011', 3),  " +
        "('LG', '8011', 11),  " +
        "('PN', '8011', 11),  " +
        "('AD', '8011', 11),  " +
        "('AC', '9011', 10),  " +
        "('FN', '9011', 10),  " +
        "('HQ', '1000', 14),  " +
        "('AF', '1000', 15),  " +
        "('MK', '2000', 16),  " +
        "('APS', '2100', 17),  " +
        "('APS(BOI)', '2200', 18),  " +
        "('OSS', '3000', 19),  " +
        "('OSS(BOI)', '3100', 20),  " +
        "('CS', '4000', 21),  " +
        "('CE', '4000', 21),  " +
        "('SM', '4000', 21),  " +
        "('NS', '4100', 22),  " +
        "('SD', '5000', 23) , " +
        "('TOP Management','TOP Management',24)," +
        "('TOP Management','TOP Management',25),('NS','5014',21)"
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
        "('PROGRAMMER III',17)," +
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
        "('Vice President', 17)," +
        "('Assistant Vice President', 17)," +
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
      "INSERT INTO public.users (email, firstname, lastname, password, telephone, position_id, status, title) VALUES " +
      //admin
      "('wasana@pccth.com', 'วาสนา', 'ยีตา', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('kamonpanc@pccth.com', 'กมลพรรณ', 'ฉิมจารย์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('napakwans@pccth.com', 'นภาขวัญ', 'แซ่อึ้ง', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('narisa@pccth.com', 'ชณัญพัชร์', 'มิลินภรพิศุทธ์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('utaiwank@pccth.com', 'อุทัยวรรณ', 'กิมศรี', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('nantidas@pccth.com', 'นันทิดา', 'แสงเดือน', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('siriratc@pccth.com', 'ศิริรัตน์', 'ชาลียุทธ', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('ratirotg@pccth.com', 'รติรส', 'กุญแจทอง', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('sirirat@pccth.com', 'สิริรัตน', 'ทรงสอาด', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('prapatsons@pccth.com', 'ประภัสสร', 'ศรีสังข์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('rungkani@pccth.com', 'รุ่งกานต์', 'อินทรีประจง', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('orrathaip@pccth.com', 'อรทัย', 'เพียศักดิ์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('chompoonuchc@pccth.com', 'ชมพูนุท', 'ชัยสุขโกศล', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      "('sedtawutt@pccth.com', 'เสฏฐวุฒิ', 'ตัญจินดา', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('angtima@pccth.com', 'อังติมา', 'เรืองวิไลรัตน์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นางสาว')," +
      //vice

      "('vicePresident1@pccth.com', 'ศักดิ์ณรงค์', 'แสงสง่าพงศ์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('vicePresident2@pccth.com', 'มนัสนันท์', 'สุปัณณานนท์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('vicePresident3@pccth.com', 'ชาญชัย', 'มีสวัสดิ์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('vicePresident4@pccth.com', 'วิชาญ', 'จุนทะเกาศลย์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('vicePresident5@pccth.com', 'ปิยนาถ', 'ฑีฆวาณิช', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('vicePresident6@pccth.com', 'ชนัฐ', 'จาดทองคำ', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('vicePresident7@pccth.com', 'ชิดพล', 'สมิตะลัมพะ', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('vicePresident8@pccth.com', 'ศิโรตม์', 'แสงเจริญ', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      //Approver

      "('Approver1@pccth.com', 'เสาวภา', 'ภารเพิง', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver2@pccth.com', 'ณัฐวรรณ', 'ทนุบำรุงสาสน์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver3@pccth.com', 'ธราธร', 'พิทยะ', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver4@pccth.com', 'ภูรีลาภ', 'ก้อนทอง', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver5@pccth.com', 'ชัยสิทธิ์', 'พัฒนสุขเกษม', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver6@pccth.com', 'คมกฤช', 'สำเนียง', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver7@pccth.com', 'สามารถ', 'เหลี่ยวเจริญ', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver8@pccth.com', 'ณัฐธีร์', 'เปรมะสุทธิพัฒน์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver9@pccth.com', 'มาลินี', 'จงพัฒนกิจ', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver10@pccth.com', 'เจษฐา', 'วรรณกูล', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver11@pccth.com', 'ชาญชัย', 'มีสวัสดิ์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver12@pccth.com', 'รุ่งปิยะธิดา', 'ศรีคง', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver13@pccth.com', 'ธันย์ชนก', 'พันธุวงษ์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver14@pccth.com', 'วงศ์พรรณ', 'จำปา', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver15@pccth.com', 'มนัสนันท์', 'สุปัณณานนท์', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')," +
      "('Approver16@pccth.com', 'พร', 'สมทองพานิช', '" +
      hashpassword +
      "', '+1111111111',  8,  'เป็นพนักงานอยู่', 'นาย')";

    //users
    String users =
      "INSERT INTO public.users (email, emp_code, firstname, lastname, password, telephone, position_id, status, title) VALUES " +
      "('userSD1@pccth.com', 20000, 'userSD1', 'userSD1', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userSD2@pccth.com', 20001, 'userSD2', 'userSD2', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userSQA@pccth.com', 20002, 'userSQA', 'userSQA', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userFSI@pccth.com', 20003, 'userFSI', 'userFSI', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userPBS@pccth.com', 20004, 'userPBS', 'userPBS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userMS@pccth.com', 20005, 'userMS', 'userMS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userSE@pccth.com', 20006, 'userSE', 'userSE', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userCS@pccth.com', 20007, 'userCS', 'userCS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userCE@pccth.com', 20008, 'userCE', 'userCE', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userNS@pccth.com', 20009, 'userNS', 'userNS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userSM@pccth.com', 20010, 'userSM', 'userSM', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userOSS@pccth.com', 20011, 'userOSS', 'userOSS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userPM@pccth.com', 20012, 'userPM', 'userPM', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userDT@pccth.com', 20013, 'userDT', 'userDT', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userHQ@pccth.com', 20014, 'userHQ', 'userHQ', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAF@pccth.com', 20015, 'userAF', 'userAF', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userISO@pccth.com', 20016, 'userISO', 'userISO', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userPS@pccth.com', 20017, 'userPS', 'userPS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userTOP@pccth.com', 20018, 'userTOP', 'userTOP', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userSI@pccth.com', 20019, 'userSI', 'userSI', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userBD@pccth.com', 20020, 'userBD', 'userBD', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userIS@pccth.com', 20021, 'userIS', 'userIS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAP@pccth.com', 20022, 'userAP', 'userAP', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userFM@pccth.com', 20023, 'userFM', 'userFM', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userLG@pccth.com', 20024, 'userLG', 'userLG', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userPN@pccth.com', 20025, 'userPN', 'userPN', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAD@pccth.com', 20026, 'userAD', 'userAD', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAC@pccth.com', 20027, 'userAC', 'userAC', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userFN@pccth.com', 20028, 'userFN', 'userFN', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userMK@pccth.com', 20029, 'userMK', 'userMK', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAPS@pccth.com', 20030, 'userAPS', 'userAPS', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userAPS_BOI@pccth.com', 20031, 'userAPS(BOI)', 'userAPS(BOI)', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userOSS_BOI@pccth.com', 20032, 'userOSS(BOI)', 'userOSS(BOI)', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย'), " +
      "('userSD_BOI@pccth.com', 20033, 'userSD(BOI)', 'userSD(BOI)', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย') ," +
      "('Personnel@pccth.com', 88888, 'Personnel', 'Personnel', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย')," +
      "('viceSD_BOI@pccth.com', 80033, 'viceSD(BOI)', 'viceSD(BOI)', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย') ," +
      
      "('approveSD_BOI@pccth.com', 70033, 'approveSD(BOI)', 'approveSD(BOI)', '" +
      hashpassword +
      "', '+1111111111', 4, 'เป็นพนักงานอยู่', 'นาย') " ;
      

    if (isTableEmpty("public.users")) {
      jdbcTemplate.execute(sql);
      jdbcTemplate.execute(users);
      jdbcTemplate.execute(
        "INSERT INTO public.users_roles (user_id, roles_id) VALUES" +
        "(1,1)," +
        "(2,1)," +
        "(3,1)," +
        "(4,1)," +
        "(5,1)," +
        "(6,1)," +
        "(7,1)," +
        "(8,1)," +
        "(9,1)," +
        "(10,1)," +
        "(11,1)," +
        "(12,1)," +
        "(13,1)," +
        "(14,1)," +
        "(15,1)," +
        "(16,3)," +
        "(17,3)," +
        "(18,3)," +
        "(19,3)," +
        "(20,3)," +
        "(21,3)," +
        "(22,3)," +
        "(23,3)," +
        "(24,2)," +
        "(25,2)," +
        "(26,2)," +
        "(27,2)," +
        "(28,2)," +
        "(29,2)," +
        "(30,2)," +
        "(31,2)," +
        "(32,2)," +
        "(33,2)," +
        "(34,2)," +
        "(35,2)," +
        "(36,2)," +
        "(37,2)," +
        "(38,2)," +
        "(39,2)," +
        "(40,5)," +
        "(41,5)," +
        "(42,5)," +
        "(43,5)," +
        "(44,5)," +
        "(45,5)," +
        "(46,5)," +
        "(47,5)," +
        "(48,5)," +
        "(49,5)," +
        "(50,5)," +
        "(51,5)," +
        "(52,5)," +
        "(53,5)," +
        "(54,5)," +
        "(55,5)," +
        "(56,5)," +
        "(57,5)," +
        "(58,5)," +
        "(59,5)," +
        "(60,5)," +
        "(61,5)," +
        "(62,5)," +
        "(63,5)," +
        "(64,5)," +
        "(65,5)," +
        "(66,5)," +
        "(67,5)," +
        "(68,5)," +
        "(69,5)," +
        "(70,5)," +
        "(71,5)," +
        "(72,5)," +
        "(73,5),(74,4),"+
        "(75,3),(76,4)"
      );
      jdbcTemplate.execute(
        "INSERT INTO public.user_departments (user_id, department_id) VALUES " +
        "(1,5)," +
        "(1,33)," +
        "(2,6)," +
        "(3,21)," +
        "(3,22)," +
        "(3,23)," +
        "(3,24)," +
        "(4,1)," +
        "(4,2)," +
        "(4,3)," +
        "(4,42)," +
        "(5,8)," +
        "(6,9)," +
        "(6,10)," +
        "(6,12)," +
        "(6,38)," +
        "(6,39)," +
        "(6,40)," +
        "(7,41)," +
        "(7,45)," +
        "(8,25)," +
        "(8,36)," +
        "(8,37)," +
        "(9,14)," +
        "(10,15)," +
        "(11,26)," +
        "(12,27)," +
        "(12,28)," +
        "(13,29)," +
        "(13,30)," +
        "(13,31)," +
        "(14,31)," +
        "(15,34)," +
        "(15,35)," +
        "(16,16)," +
        "(16,24)," +
        "(17,17)," +
        "(17,29)," +
        "(18,7)," +
        "(18,13)," +
        "(18,24)," +
        "(19,13)," +
        "(19,25)," +
        "(20,14)," +
        "(20,15)," +
        "(20,19)," +
        "(21,20)," +
        "(22,20)," +
        "(23,1)," +
        "(24,2)," +
        "(25,3)," +
        "(26,8)," +
        "(27,9)," +
        "(28,10)," +
        "(29,11)," +
        "(30,12)," +
        "(31,21)," +
        "(32,22)," +
        "(33,23)," +
        "(34,24)," +
        "(35,26)," +
        "(36,27)," +
        "(37,28)," +
        "(38,29)," +
        "(39,39)," +
        "(40,1)," +
        "(41,2)," +
        "(42,3)," +
        "(43,5)," +
        "(44,6)," +
        "(45,7)," +
        "(46,8)," +
        "(47,9)," +
        "(48,10)," +
        "(49,11)," +
        "(50,12)," +
        "(51,13)," +
        "(52,14)," +
        "(53,15)," +
        "(54,16)," +
        "(55,17)," +
        "(56,11)," +
        "(57,19)," +
        "(58,20)," +
        "(59,21)," +
        "(60,22)," +
        "(61,23)," +
        "(62,24)," +
        "(63,25)," +
        "(64,26)," +
        "(65,27)," +
        "(66,28)," +
        "(67,29)," +
        "(68,30)," +
        "(69,33)," +
        "(70,34)," +
        "(71,35)," +
        "(72,37)," +
        "(73,42)," +
        "(74,1)," +
        "(74,2)," +
        "(74,3)," +
        "(74,4)," +
        "(74,5)," +
        "(74,6)," +
        "(74,7)," +
        "(74,8)," +
        "(74,9)," +
        "(74,10)," +
        "(74,11)," +
        "(74,12)," +
        "(74,13)," +
        "(74,14)," +
        "(74,15)," +
        "(74,16)," +
        "(74,17)," +
        "(74,18)," +
        "(74,19)," +
        "(74,20)," +
        "(74,21)," +
        "(74,22)," +
        "(74,23)," +
        "(74,24)," +
        "(74,25)," +
        "(74,26)," +
        "(74,27)," +
        "(74,28)," +
        "(74,29)," +
        "(74,30)," +
        "(74,31)," +
        "(74,32)," +
        "(74,33)," +
        "(74,34)," +
        "(74,35)," +
        "(74,36)," +
        "(74,37)," +
        "(74,38)," +
        "(74,39)," +
        "(74,40)," +
        "(74,41)," +
        "(74,42)," +
        "(75,42),(76,42)"
      );
    }
    jdbcTemplate.execute(
      "INSERT INTO public.users_companys (user_id, companys_id) VALUES" +
      "(1,1)," +
      "(1,2)," +
      "(2,1)," +
      "(3,1)," +
      "(4,1)," +
      "(4,2)," +
      "(5,1)," +
      "(6,1)," +
      "(6,2)," +
      "(7,1)," +
      "(7,2)," +
      "(8,1)," +
      "(8,2)," +
      "(9,1)," +
      "(10,1)," +
      "(11,1)," +
      "(12,1)," +
      "(13,1)," +
      "(13,2)," +
      "(14,2)," +
      "(15,2)," +
      "(16,1)," +
      "(17,1)," +
      "(18,1)," +
      "(19,1)," +
      "(20,1)," +
      "(21,1)," +
      "(22,1)," +
      "(23,1)," +
      "(24,1)," +
      "(25,1)," +
      "(26,1)," +
      "(27,1)," +
      "(28,1)," +
      "(29,1)," +
      "(30,1)," +
      "(31,1)," +
      "(32,1)," +
      "(33,1)," +
      "(34,1)," +
      "(35,1)," +
      "(36,1)," +
      "(37,1)," +
      "(38,1)," +
      "(39,1)," +
      "(40,1)," +
      "(41,1)," +
      "(42,1)," +
      "(43,1)," +
      "(44,1)," +
      "(45,1)," +
      "(46,1)," +
      "(47,1)," +
      "(48,1)," +
      "(49,1)," +
      "(50,1)," +
      "(51,1)," +
      "(52,1)," +
      "(53,1)," +
      "(54,1)," +
      "(55,1)," +
      "(56,1)," +
      "(57,1)," +
      "(58,1)," +
      "(59,1)," +
      "(60,1)," +
      "(61,1)," +
      "(62,1)," +
      "(63,1)," +
      "(64,1)," +
      "(65,1)," +
      "(66,1)," +
      "(67,1)," +
      "(68,1)," +
      "(69,2)," +
      "(70,2)," +
      "(71,2)," +
      "(72,2)," +
      "(73,2)," +
      "(74,1),(74,2),(75,2),(76,2)"
    );
    jdbcTemplate.execute(
      "INSERT INTO public.users_sectors (users_id, sectors_id) VALUES " +
      "(1,8)," +
      "(1,16)," +
      "(2,7)," +
      "(3,6)," +
      "(4,1)," +
      "(4,23)," +
      "(5,5)," +
      "(6,5)," +
      "(6,21)," +
      "(7,5)," +
      "(7,21)," +
      "(8,3)," +
      "(8,19)," +
      "(8,20)," +
      "(9,4)," +
      "(10,4)," +
      "(11,11)," +
      "(12,11)," +
      "(13,10)," +
      "(13,15)," +
      "(14,14)," +
      "(15,17)," +
      "(15,18)," +
      "(16,11)," +
      "(17,10)," +
      "(18,3)," +
      "(18,6)," +
      "(18,7)," +
      "(19,3)," +
      "(20,4)," +
      "(21,5)," +
      "(22,5)," +
      "(23,1)," +
      "(24,1)," +
      "(25,1)," +
      "(26,5)," +
      "(27,5)," +
      "(28,5)," +
      "(29,5)," +
      "(30,5)," +
      "(31,6)," +
      "(32,6)," +
      "(33,6)," +
      "(34,6)," +
      "(35,11)," +
      "(36,11)," +
      "(37,11)," +
      "(38,10)," +
      "(39,10)," +
      "(40,1)," +
      "(41,1)," +
      "(42,1)," +
      "(43,8)," +
      "(44,7)," +
      "(45,6)," +
      "(46,5)," +
      "(47,5)," +
      "(48,5)," +
      "(49,5)," +
      "(50,5)," +
      "(51,3)," +
      "(52,4)," +
      "(53,4)," +
      "(54,11)," +
      "(55,10)," +
      "(56,11)," +
      "(57,4)," +
      "(58,5)," +
      "(59,6)," +
      "(60,6)," +
      "(61,6)," +
      "(62,6)," +
      "(63,3)," +
      "(64,11)," +
      "(65,11)," +
      "(66,11)," +
      "(67,10)," +
      "(68,10)," +
      "(69,16)," +
      "(70,17)," +
      "(71,18)," +
      "(72,20)," +
      "(73,23)," +
      "(74,1)," +
      "(74,2)," +
      "(74,3)," +
      "(74,4)," +
      "(74,5)," +
      "(74,6)," +
      "(74,7)," +
      "(74,8)," +
      "(74,9)," +
      "(74,10)," +
      "(74,11)," +
      "(74,12)," +
      "(74,13)," +
      "(74,14)," +
      "(74,15)," +
      "(74,16)," +
      "(74,17)," +
      "(74,18)," +
      "(74,19)," +
      "(74,20)," +
      "(74,21)," +
      "(74,22)," +
      "(74,23)," +
      "(74,24)," +
      "(74,25),(75,23),(76,23)"
    );
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
