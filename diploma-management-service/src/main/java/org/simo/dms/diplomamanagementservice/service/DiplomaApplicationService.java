package org.simo.dms.diplomamanagementservice.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.simo.auth.context.provider.RequestContext;
import org.simo.auth.context.provider.RequestContextHolder;
import org.simo.dms.diplomamanagementservice.enums.DiplomaStatus;
import org.simo.dms.diplomamanagementservice.enums.UserRole;
import org.simo.dms.diplomamanagementservice.model.DiplomaApplication;
import org.simo.dms.diplomamanagementservice.repository.DiplomaApplicationRepository;
import org.simo.dms.diplomamanagementservice.request.CreateDiplomaApplication;
import org.simo.dms.diplomamanagementservice.response.DiplomaApplicationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class DiplomaApplicationService {
    private final ModelMapper modelMapper;
    private final DiplomaApplicationRepository diplomaApplicationRepository;


    public DiplomaApplicationDto createDiplomaApplication(CreateDiplomaApplication request) {
        UserRole callerRole = getCallerRole();
        String callerEmail = getCallerEmail();

        if(!callerRole.equals(UserRole.STUDENT)) {
            String errorMessage = "Caller is not a STUDENT";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        if(!callerEmail.equalsIgnoreCase(request.getOwnerEmail())) {
            String errorMessage = "Caller email does not match owner email";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        DiplomaApplication newApplication = modelMapper.map(request, DiplomaApplication.class);
        DiplomaApplication savedApplication = diplomaApplicationRepository.save(newApplication);

        return modelMapper.map(savedApplication, DiplomaApplicationDto.class);
    }

    private String getCallerEmail() {
        RequestContext requestContext = RequestContextHolder.getContext();
        String userEmail = requestContext.getUserEmail().toLowerCase();
        return userEmail;
    }

    private UserRole getCallerRole() {
        RequestContext requestContext = RequestContextHolder.getContext();
        UserRole userRole = UserRole.valueOf(requestContext.getUserRole());
        return userRole;
    }

    public void sendDiplomaApplicationForApproval(String applicationId){
        UserRole callerRole = getCallerRole();
        String callerEmail = getCallerEmail();

        if(!callerRole.equals(UserRole.PROFESSOR)) {
            String errorMessage = "Caller is not a PROFESSOR";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        DiplomaApplication application = diplomaApplicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        String supervisorEmail = application.getSupervisorEmail();

        if(!callerEmail.equalsIgnoreCase(supervisorEmail)) {
            String errorMessage = "Caller email does not match supervisor email";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        if (!application.getStatus().equals(DiplomaStatus.OPENED)) {
            throw new RuntimeException("Application status must be in OPENED in order to send for approval");
        }

        application.setStatus(DiplomaStatus.SENT_FOR_APPROVAL);
        diplomaApplicationRepository.save(application);
    }

    public void cancelDiplomaApplication(String applicationId){
        UserRole callerRole = getCallerRole();
        String callerEmail = getCallerEmail();

        if(callerRole.equals(UserRole.GUEST)) {
            String errorMessage = "Caller must be: {PROFESSOR, STUDENT}";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        DiplomaApplication application = diplomaApplicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        String supervisorEmail = application.getSupervisorEmail();
        String ownerEmail = application.getOwnerEmail();

        if(!callerEmail.equalsIgnoreCase(supervisorEmail) && !callerEmail.equalsIgnoreCase(ownerEmail)) {
            String errorMessage = "Caller email does is not related to this application";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        application.setStatus(DiplomaStatus.CANCELED);
        diplomaApplicationRepository.save(application);
    }

    public void rejectDiplomaApplication(String applicationId){
        UserRole callerRole = getCallerRole();
        String callerEmail = getCallerEmail();

        if(!callerRole.equals(UserRole.PROFESSOR)) {
            String errorMessage = "Caller is not a PROFESSOR";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        DiplomaApplication application = diplomaApplicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        String supervisorEmail = application.getSupervisorEmail();

        if(callerEmail.equalsIgnoreCase(supervisorEmail)) {
            String errorMessage = "Supervisor can not reject its own application";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        if (!application.getStatus().equals(DiplomaStatus.SENT_FOR_APPROVAL)) {
            throw new RuntimeException("Application status must be in SENT_FOR_APPROVAL in order to send for approval");
        }

        application.setStatus(DiplomaStatus.REJECTED);
        diplomaApplicationRepository.save(application);
    }

    public void acceptDiplomaApplication(String applicationId){
        UserRole callerRole = getCallerRole();
        String callerEmail = getCallerEmail();

        if(!callerRole.equals(UserRole.PROFESSOR)) {
            String errorMessage = "Caller is not a PROFESSOR";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        DiplomaApplication application = diplomaApplicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        String supervisorEmail = application.getSupervisorEmail();

        if(callerEmail.equalsIgnoreCase(supervisorEmail)) {
            String errorMessage = "Supervisor can not accept its own application";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        if (!application.getStatus().equals(DiplomaStatus.SENT_FOR_APPROVAL)) {
            throw new RuntimeException("Application status must be in SENT_FOR_APPROVAL in order to send for approval");
        }

        application.setStatus(DiplomaStatus.APPROVED);
        diplomaApplicationRepository.save(application);
    }

    public void reOpenDiplomaApplication(String applicationId){
        UserRole callerRole = getCallerRole();
        String callerEmail = getCallerEmail();

        if(callerRole.equals(UserRole.GUEST)) {
            String errorMessage = "Caller must be: {PROFESSOR, STUDENT}";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        DiplomaApplication application = diplomaApplicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        String supervisorEmail = application.getSupervisorEmail();
        String ownerEmail = application.getOwnerEmail();

        if(!callerEmail.equalsIgnoreCase(supervisorEmail) && !callerEmail.equalsIgnoreCase(ownerEmail)) {
            String errorMessage = "Caller email does is not related to this application";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        application.setStatus(DiplomaStatus.OPENED);
        diplomaApplicationRepository.save(application);
    }

    public List<DiplomaApplicationDto> getDiplomaApplicationsOfUser(){
        UserRole callerRole = getCallerRole();
        String callerEmail = getCallerEmail();

        if(!callerRole.equals(UserRole.STUDENT)) {
            String errorMessage = "Caller must be STUDENT";
            log.error(errorMessage);
            throw new IllegalCallerException(errorMessage);
        }

        List<DiplomaApplication> applications = diplomaApplicationRepository
                .findAllByOwnerEmail(callerEmail);

        List<DiplomaApplicationDto> diplomaApplicationDtos = applications
                .stream()
                .map(application -> modelMapper.map(application, DiplomaApplicationDto.class))
                .toList();

        return diplomaApplicationDtos;
    }

    public Page<DiplomaApplicationDto> getAllDiplomaApplications(PageRequest pageRequest){
        Page<DiplomaApplicationDto> page = diplomaApplicationRepository
                .findAll(pageRequest)
                .map(application -> modelMapper.map(application, DiplomaApplicationDto.class));

        return page;
    }
}
