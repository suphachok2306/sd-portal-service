package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Result;
import com.pcc.portalservice.model.Training;
import com.pcc.portalservice.model.enums.StatusApprove;
import com.pcc.portalservice.requests.CreateTrainingRequest;
import com.pcc.portalservice.requests.EditTrainingSection1Request;
import com.pcc.portalservice.requests.EditTrainingSection2Request;
import com.pcc.portalservice.response.ApiResponse;
import com.pcc.portalservice.response.ResponseData;
import com.pcc.portalservice.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class TrainingController {

  private TrainingService trainingService;

  /**
   * @สร้างTraining
   * @PostMapping
   */
  @PostMapping("/createTraining")
  public ResponseEntity<ApiResponse> createTraining(
    @RequestBody CreateTrainingRequest createTrainingRequest
  ) throws ParseException {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (trainingService.isTrainingNull(createTrainingRequest)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      Training training = trainingService.createTraining(createTrainingRequest);
      data.setResult(training);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/createTraining")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  /**
   * @แก้ไขTrainingส่วนที่1
   * @PostMapping
   */
  @PostMapping("/editTrainingSection1")
  public ResponseEntity<ApiResponse> editTrainingSection1(
    @RequestParam Long trainingId,
    @RequestBody EditTrainingSection1Request editTraining
  ) throws ParseException {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    if (trainingService.isEditTrainingNull1(editTraining)) {
      response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
      return ResponseEntity.badRequest().body(response);
    }
    try {
      Training training = trainingService.editTrainingSection1(
        trainingId,
        editTraining
      );
      data.setResult(training);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/editTraining")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  /**
   * @แก้ไขTrainingส่วนที่3
   * @PostMapping
   */
  //  @PostMapping("/editTrainingSection1Person")
  //  public ResponseEntity<ApiResponse> editTrainingSection1Person(
  //          @RequestParam Long trainingId,
  //          @RequestBody EditTrainingSection1PersonRequest editTraining
  //  ) throws ParseException {
  //    ApiResponse response = new ApiResponse();
  //    ResponseData data = new ResponseData();
  //    //        if (trainingService.isEditTrainingNull3(editTraining)) {
  //    //            response.setResponseMessage("ไม่สามารถบันทึกข้อมูลลงฐานข้อมูลได้");
  //    //            return ResponseEntity.badRequest().body(response);
  //    //        }
  //    try {
  //      Training training = trainingService.editTrainingSection1Person(
  //              trainingId,
  //              editTraining
  //      );
  //      data.setResult(training);
  //      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
  //      response.setResponseData(data);
  //      URI uri = URI.create(
  //              ServletUriComponentsBuilder
  //                      .fromCurrentContextPath()
  //                      .path("/editTraining")
  //                      .toUriString()
  //      );
  //      return ResponseEntity.created(uri).body(response);
  //    } catch (Exception e) {
  //      response.setResponseMessage(e.getMessage());
  //      return ResponseEntity.internalServerError().body(response);
  //    }
  //  }

  /**
   * @แก้ไขTrainingส่วนที่2
   * @PostMapping
   */
  @PostMapping("/editTrainingSection2")
  public ResponseEntity<ApiResponse> editTrainingSection2(
    @RequestParam Long resultId,
    @RequestBody EditTrainingSection2Request editTraining
  ) throws ParseException {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    try {
      Result training = trainingService.editTrainingSection2(
        resultId,
        editTraining
      );
      data.setResult(training);
      response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
      response.setResponseData(data);
      URI uri = URI.create(
        ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/editTraining")
          .toUriString()
      );
      return ResponseEntity.created(uri).body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.internalServerError().body(response);
    }
  }

  /**
   * @แก้ไขStatusของTraining
   * @PostMapping
   */
  @PutMapping("/setStatusToTraining")
  public ResponseEntity<ApiResponse> addStatusToTraining(
    @RequestParam Long trainingId,
    Long approveId,
    StatusApprove statusApprove
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    try {
      Training training = trainingService.setStatusToTraining(
        trainingId,
        approveId,
        statusApprove
      );
      data.setResult(training);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  /**
   * @แก้ไขStatusของTraining
   * @PostMapping
   */
  @PutMapping("/setCancelToTraining")
  public ResponseEntity<ApiResponse> setCancelToTraining(
    @RequestParam Long trainingId
  ) {
    ApiResponse response = new ApiResponse();
    ResponseData data = new ResponseData();
    try {
      Training training = trainingService.setCancelToTraining(trainingId);
      data.setResult(training);
      response.setResponseMessage("ทำรายการเรียบร้อย");
      response.setResponseData(data);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      response.setResponseMessage(e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  /**
   * @หาTrainingด้วยId
   * @GetMapping
   */
  @GetMapping("/findTrainingByTrainingId")
  public ResponseEntity<Map<String, Object>> findTrainingByTrainingId(
    @RequestParam Long trainingId
  ) {
    Map<String, Object> training = trainingService.findById(trainingId);
    return ResponseEntity.ok(training);
  }

  /**
   * @หาTrainingด้วยUserId
   * @GetMapping
   */
  @GetMapping("/findTrainingByUserId")
  public ResponseEntity<List<Map<String, Object>>> findTrainingByUserId(
    @RequestParam Long userId
  ) {
    List<Map<String, Object>> trainings = trainingService.findTrainingsByUserId(
      userId
    );
    return ResponseEntity.ok(trainings);
  }

  /**
   * @หาTrainingด้วยApproveId
   * @GetMapping
   */
  @GetMapping("/findTrainingByApprove1Id")
  public ResponseEntity<List<Map<String, Object>>> findTrainingByApprove1Id(
    @RequestParam Long approve1Id
  ) {
    List<Map<String, Object>> trainings = trainingService.findTrainingsByApprove1Id(
      approve1Id
    );
    return ResponseEntity.ok(trainings);
  }

  // /**
  //  * @หาTrainingด้วยPersonnelId
  //  * @GetMapping
  //  */
  // @GetMapping("/findTrainingByPersonnelId")
  // public ResponseEntity<List<Map<String, Object>>> findTrainingByPersonnelId(
  //         @RequestParam Long PersonnelId
  // ) {
  //   List<Map<String, Object>> trainings = trainingService.findTrainingByPersonnelId(
  //           PersonnelId
  //   );
  //   return ResponseEntity.ok(trainings);
  // }

  /**
   * @หาTrainingทั้งหมด
   * @GetMapping
   */
  @GetMapping("/findAllTraining")
  public List<Map<String, Object>> findAllTraining() {
    return trainingService.findAllTraining();
  }

  /**
   * @หาTrainingด้วยName,Position,Department,startDate,endDate,courseName
   * @GetMapping
   */
  @GetMapping("/searchTraining")
  public Object search(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String position,
    @RequestParam(required = false) String department,
    @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE
    ) Date startDate,
    @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE
    ) Date endDate,
    @RequestParam(required = false) String courseName
  ) {
    return trainingService.searchTraining(
      name,
      position,
      department,
      startDate,
      endDate,
      courseName
    );
  }

  //  @GetMapping("/Report")
  //  public String report(@RequestParam Long trainId) {
  //    return trainingService.printReport(trainId);
  //  }

  @GetMapping("/Report")
  public String report(
    @RequestParam Long trainId,
    @RequestParam(required = false) Long userId1,
    @RequestParam(required = false) Long userId2,
    @RequestParam(required = false) Long userId3,
    @RequestParam(required = false) Long userId4
  ) {
    return trainingService.printReport(
      trainId,
      userId1,
      userId2,
      userId3,
      userId4
    );
  }

//  @Value("${pdf.path}")
//  private String pdfFilesPath;
//
//  // Modify the endpoint to accept parameters and generate the report
//  @GetMapping("/generateReport/{trainId}/{userId1}/{userId2}/{userId3}/{userId4}")
//  public ResponseEntity<?> generateAndDownloadReport(
//          @PathVariable Long trainId,
//          @PathVariable Long userId1,
//          @PathVariable Long userId2,
//          @PathVariable Long userId3,
//          @PathVariable Long userId4) {
//    File pdfFile = generatePdfReport(trainId, userId1, userId2, userId3, userId4);
//
//    if (pdfFile != null) {
//      Resource resource = new FileSystemResource(pdfFile);
//
//      if (resource.exists() && resource.isReadable()) {
//        String encodedFilename = UriUtils.encode(resource.getFilename(), "UTF-8");
//
//        String contentDisposition = String.format("inline; filename=\"%s\"", encodedFilename);
//
//        // Serve the temporary PDF file as a response
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(resource);
//      }
//    }
//
//    return ResponseEntity.notFound().build();
//  }



//  @GetMapping("/ReportHistoryTraining")
//  public String reportHistoryTraining(
//    @RequestParam String startDate,
//    @RequestParam String endDate,
//    @RequestParam Long deptID,
//    @RequestParam Long sectorID
//  ) {
//    System.out.println("0");
//    return trainingService.printReportHistoryTraining(
//      startDate,
//      endDate,
//      deptID,
//      sectorID
//    );
//  }
  @GetMapping("/ReportHistoryTraining")
  public String reportHistoryTraining(
          //@RequestParam Long trainId
          @RequestParam(required = false) String startDate,
          @RequestParam(required = false) String endDate,
          @RequestParam(required = false) Long deptID,
          @RequestParam(required = false) Long sectorID


  ) throws Exception {
    //System.out.println("0");
    return trainingService.printReportHistoryTraining(startDate,endDate,deptID,sectorID);
  }
  @GetMapping("/HistoryTraining")
  public LinkedHashMap<String, Object> HistoryTraining(
    @RequestParam String startDate,
    @RequestParam String endDate,
    @RequestParam Long deptID,
    @RequestParam Long sectorID
  ) {
    return trainingService.HistoryTraining(
      startDate,
      endDate,
      deptID,
      sectorID
    );
  }
  // public ResponseEntity<List<Map<String, Object>>> findAllApprove(@RequestParam Long count) {
  //     List<Map<String, Object>> training = trainingService.findbyAllCountApprove(count);
  //     if (count > 3) {
  //         return ResponseEntity.badRequest().build();
  //     }
  //     return ResponseEntity.ok(training);
  // }

  // @GetMapping("/findNextApprove")
  // public List<Map<String, Object>> findNextApprove() {
  //     return trainingService.findNextApprove();
  // }

}
