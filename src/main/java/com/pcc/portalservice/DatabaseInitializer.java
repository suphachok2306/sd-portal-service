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
        "(11, 'HQ', 'HQ',1)," +
        "(12, 'ISO', 'ISO',1)," +
        "(13, 'ISO', 'ISO',2);"
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
        "(19, 'PS', '8011', 4), " +
        "(20, 'TOP', '8011', 5) "
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
        "('SR.PROGRAMMER', 4),\r\n" + //
        "('SR. SYSTEM ANALYST I', 5),\r\n" + //
        "('PROGRAMMER I', 6),\r\n" + //
        "('SR.SQA ANALYST II', 7),\r\n" + //
        "('SR.MANAGER', 8),\r\n" + //
        "('SR.TESTER', 9),\r\n" + //
        "('TESTER III', 10),\r\n" + //
        "('SR.SQA ANALYST I', 11),\r\n" + //
        "('TESTER II', 12),\r\n" + //
        "('TESTER I', 13),\r\n" + //
        "('PROGRAMMER II',14),\r\n" + //
        "('SR.SYSTEM ANALYST I',15),\r\n" + //
        "('SR.SYSTEM ANALYST II',16),\r\n" + //
        "('PROGRAMMER III',17)," +
        "('Manager Senior Manager', 7)," +
        "('Business Development Specialist Senior', 7)," +
        "('Business Development Senior', 7)," +
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
        "('Manager Senior Manager', 7)," +
        "('IT Specialist Senior IT Consultant', 7)," +
        "('IT Administrator Senior', 7)," +
        "('Vice President', 13)," +
        "('Asst. Vice President', 13)," +
        "('FM Manager', 13)," +
        "('FSVP-Services Group', 19)," +
        "('Vice President (8B)', 19)," +
        "('Asst.Vice President (8C)', 19)," +
        "('Manager', 13)," +
        "('Asst. Manager', 13)," +
        "('SQA Specialist', 13)," +
        "('SQA Analyst', 13)," +
        "('Application Support', 13)," +
        "('Legal Manager Sr.Legal Manager', 16)," +
        "('Sr.Legal Specialist I', 16)," +
        "('Sr.Legal Specialist II', 16)," +
        "('Legal Specialist I', 16)," +
        "('Legal Specialist II', 16)," +
        "('Sr.Legal Officer I', 16)," +
        "('Sr.Legal Officer II', 16)," +
        "('Admin Staff I', 16)," +
        "('Admin Staff II', 16)," +
        "('Admin Staff III', 16)," +
        "('Legal Officer I', 16)," +
        "('Legal Officer II', 16)," +
        "('Legal Officer III', 16)," +
        "('Finance Officer I', 17)," +
        "('Finance Officer II', 17)," +
        "('Finance Officer III', 17)," +
        "('Cashier', 17)," +
        "('Bill Collector', 17)," +
        "('Vice President', 20)," +
        "('Assistant Vice President', 20)," +
        "('Sr. Personnel Manager', 16)," +
        "('Personnel Manager', 16)," +
        "('Assistant Manager I PN', 16)," +
        "('Assistant Manager II PN', 16)," +
        "('Assistant Manager I (Payroll)', 16)," +
        "('Assistant Manager II (Payroll)', 16)," +
        "('Sr. HR Specialist I', 16)," +
        "('Sr. HR Specialist II', 16)," +
        "('HR Specialist I', 16)," +
        "('HR Specialist II', 16)," +
        "('Sr. HR Officer I', 16)," +
        "('Sr. HR Officer II', 16)," +
        "('Sr. Payroll Officer I', 16)," +
        "('Sr. Payroll Officer II', 16)," +
        "('HR Officer I', 16)," +
        "('HR Officer II', 16)," +
        "('HR Officer III', 16)," +
        "('Payroll Officer I', 16)," +
        "('Payroll Officer II', 16)," +
        "('Payroll Officer III', 16)," +
        "('Officer Admin Manager 7B', 16)," +
        "('Sr. Admin Officer IV 6A', 16)," +
        "('Sr. Admin Officer III 6B', 16)," +
        "('Sr. Admin Officer II 5A', 16)," +
        "('Sr. Admin Officer I 5B', 16)," +
        "('Admin Officer I-III 3A-4A', 16)," +
        "('Admin Officer II 3A-4A', 16)," +
        "('Admin Officer III 3A-4A', 16)," +
        "('Admin Staff I', 16)," +
        "('Admin Staff II', 16)," +
        "('Admin Staff III', 16)," +
        "('Driver', 16)," +
        "('Accounting Officer I', 17)," +
        "('Accounting Officer II', 17)," +
        "('Accounting Officer III', 17)," +
        "('Account Admin I', 17)," +
        "('Account Admin II', 17)," +
        "('Account Admin III', 17)," +
        "('Sr.Accounting Officer I', 17)," +
        "('Sr.Accounting Officer II', 17)," +
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
        "('Manager', 13)," +
        "('Asst Manager', 13)," +
        "('System Engineer', 13)," +
        "('Network Analyst', 13)," +
        "('Network Engineer', 13)," +
        "('DBA', 13)," +
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
        "('Admin Officer Senior', 7)," +
        "('Manager', 13)," +
        "('Assistant Manager', 13)," +
        "('Application Specialist', 13)," +
        "('System Analyst', 13)," +
        "('Programmer', 13)," +
        "('Operation Support', 13)," +
        "('Sr Admin Officer', 13)," +
        "('Admin Officer', 13)," +
        "('Driver', 13)," +
        "('Manager', 13)," +
        "('Asst Manager', 13)," +
        "('Mainframe Specialist', 13)," +
        "('Sr_System Programmer', 13)," +
        "('System Programmer', 13)," +
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
        "('MK Group Executive Vice President', 6)," +
        "('Manager_Sr.Manager (Sale Support)', 6)," +
        "('Assistant Manager I (Business Consultant)', 6)," +
        "('Assistant Manager II (Business Consultant)', 6)," +
        "('Business Consultant I', 6)," +
        "('Business Consultant II', 6)," +
        "('Business Consultant Specialist I', 6)," +
        "('Business Consultant Specialist II', 6)," +
        "('Jr. Business Consultant', 6)," +
        "('Admin Sr.Admin Officer I', 6)," +
        "('Admin Sr.Admin Officer II', 6)," +
        "('Admin Sr.Admin Officer III', 6)," +
        "('Admin Sr.Admin Officer IV', 6)," +
        "('Sale Support Sr.Sale Support', 6)," +
        "('Manager/Sr.Manager', 6)," +
        "('Sr. Business Consultant Specialist I', 6)," +
        "('Sr. Business Consultant Specialist II', 6)," +
        "('Sr. Business Consultant Specialist III', 6)," +
        "('Asst Project Manager', 13)," +
        "('Project Coordinator', 13)," +
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
        "('Manager', 13)," +
        "('Asst Manager', 13)," +
        "('Sr System Operator', 13)," +
        "('System Operator', 13)," +
        "('Manager Senior Manager', 7)," +
        "('Purchasing Specialist', 7)," +
        "('Purchasing Officer Senior', 7)," +
        "('Asst. Vice President', 5)," +
        "('Admin Sr.Admin Officer I', 5)," +
        "('Admin Sr.Admin Officer II', 5)," +
        "('Admin Sr.Admin Officer III', 5)," +
        "('Admin Sr.Admin Officer IV', 5)," +
        "('Vice President', 5)," +
        "('Business Consultant Specialist I', 5)," +
        "('Business Consultant Specialist II', 5)," +
        "('Manager Sr.Manager (Sale Support)', 5)," +
        "('Sr.Business Consultant I', 5)," +
        "('Sr.Business Consultant II', 5)," +
        "('Business Consultant I', 5)," +
        "('Business Consultant II', 5)," +
        "('Sale Support Sr.Sale Support', 5)," +
        "('Manager/Sr.Manager', 5)," +
        "('President', 16)," +
        "('Executive Secretary', 16)," +
        "('Advisor', 16)," +
        "('Secretary Sr. Secretary', 16)," +
        "('Sr. Vice President Chief Architect', 16)," +
        "('CEO', 16)," +
        "('SR-Manager (Chief Architect)', 16)," +
        "('Programmer (Chief Architect)', 16)," +
        "('Manager Sr.Manager (Sale Support)', 16)," +
        "('Manager (7A)', 14)," +
        "('Sr.Project Manager (7A)', 14)," +
        "('Project Manager (7B)', 14)," +
        "('Sr.Asst. Project Manager (6A)', 14)," +
        "('Asst. Project Manager (6B)', 14)," +
        "('Lead Project Co 6B-6A', 14)," +
        "('Sr.Project Co 5B-5A', 14)," +
        "('Project Co 4A', 14)," +
        "('Project Co 3A-4B', 14)," +
        "('Sr.Admin IV (6A)', 14)," +
        "('Sr.Admin III (6B)', 14)," +
        "('Sr.Admin II (5A)', 14)," +
        "('Sr.Admin I (5B)', 14)," +
        "('Admin III (4A)', 14)," +
        "('Admin II (4B)', 14)," +
        "('Admin I (3A)', 14)," +
        "('Technical Consultant', 7)," +
        "('Solution Architect', 7)," +
        "('Solutions Specialist  Senior', 7)," +
        "('Technical Specialist Senior', 7)," +
        "('Technical Engineer Senior', 7)," +
        "('Manager Senior Manager', 7)," +
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
        "('Instructor', 15)"
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
      "', '+1111111111', 1, 5, 8, 6, 'เป็นพนักงานอยู่', 'นาย'), " +
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
      "    (1500.0,1500,3000, '2023', 1,1);";

    if (isTableEmpty("public.budget")) {
      jdbcTemplate.execute(sql);
    }
  }
}
