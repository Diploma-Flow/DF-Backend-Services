package org.simo.dms.diplomamanagementservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.dms.diplomamanagementservice.model.DiplomaApplication;
import org.simo.dms.diplomamanagementservice.request.CreateDiplomaApplication;
import org.simo.dms.diplomamanagementservice.response.DiplomaApplicationDto;
import org.simo.dms.diplomamanagementservice.service.DiplomaApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 6/17/2024
 */

@Log4j2
@RestController
@RequestMapping("/diploma-management")
@RequiredArgsConstructor
public class DiplomaManagementController {

    private final DiplomaApplicationService diplomaApplicationService;

    @PostMapping("/create-application")
    public ResponseEntity<DiplomaApplicationDto> createDiplomaApplication(@RequestBody CreateDiplomaApplication request){
        DiplomaApplicationDto diplomaApplication = diplomaApplicationService.createDiplomaApplication(request);

        return ResponseEntity.ok().body(diplomaApplication);
    }

    @PostMapping("/send-for-approval")
    public ResponseEntity<?> sendDiplomaApplicationForApproval(@RequestParam("id") String applicationId){
        diplomaApplicationService.sendDiplomaApplicationForApproval(applicationId);
        return ResponseEntity.ok().body("Successfully SENT Diploma Application");
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelDiplomaApplication(@RequestParam("id") String applicationId){
        diplomaApplicationService.cancelDiplomaApplication(applicationId);
        return ResponseEntity.ok().body("Successfully CANCELED the Diploma Application");
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectDiplomaApplication(@RequestParam("id") String applicationId){
        diplomaApplicationService.rejectDiplomaApplication(applicationId);
        return ResponseEntity.ok().body("Successfully REJECTED the Diploma Application");
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptDiplomaApplication(@RequestParam("id") String applicationId){
        diplomaApplicationService.acceptDiplomaApplication(applicationId);
        return ResponseEntity.ok().body("Successfully ACCEPTED the Diploma Application");
    }

    @PostMapping("/re-open")
    public ResponseEntity<?> reOpenDiplomaApplication(@RequestParam("id") String applicationId){
        diplomaApplicationService.reOpenDiplomaApplication(applicationId);
        return ResponseEntity.ok().body("Successfully REOPENED the Diploma Application");
    }

    @GetMapping("/get-by-user")
    public ResponseEntity<List<DiplomaApplicationDto>> getDiplomaApplicationsOfUser(){
        List<DiplomaApplicationDto> applicationsOfUser = diplomaApplicationService.getDiplomaApplicationsOfUser();
        return ResponseEntity.ok().body(applicationsOfUser);
    }

    @GetMapping()
    public ResponseEntity<Page<DiplomaApplicationDto>> getAllDiplomaApplications(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<DiplomaApplicationDto> diplomaApplicationPage = diplomaApplicationService.getAllDiplomaApplications(pageRequest);
        return ResponseEntity.ok().body(diplomaApplicationPage);
    }
}
