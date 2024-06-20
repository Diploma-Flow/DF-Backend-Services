package org.simo.dms.diplomamanagementservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.simo.dms.diplomamanagementservice.model.DiplomaApplication;
import org.simo.dms.diplomamanagementservice.response.DiplomaApplicationDto;
import org.simo.dms.diplomamanagementservice.user.UserDto;
import org.simo.dms.diplomamanagementservice.user.UserServiceClient;
import org.springframework.stereotype.Component;

/**
 * Author: Simeon Popov
 * Date of creation: 6/20/2024
 */

@RequiredArgsConstructor
public class DiplomaApplicationToDiplomaApplicationDto implements Converter<DiplomaApplication, DiplomaApplicationDto> {

    private final UserServiceClient userServiceClient;

    //FIXME it works here but after here the correct object is returned something crumbles
    @Override
    public DiplomaApplicationDto convert(MappingContext<DiplomaApplication, DiplomaApplicationDto> context) {
        DiplomaApplication source = context.getSource();
        DiplomaApplicationDto destination = context.getDestination() == null ? new DiplomaApplicationDto() : context.getDestination();

        // Map simple fields
        destination.setId(source.getId());
        destination.setTitle(source.getTitle());
        destination.setStatus(source.getStatus());
        destination.setCreationDate(source.getCreationDate());
        destination.setTasks(source.getTasks());
        destination.setTechStack(source.getTechStack());

        // Fetch UserDto for owner and supervisor
        UserDto owner = userServiceClient.getUserByEmailOrDefault(source.getOwnerEmail());
        UserDto supervisor = userServiceClient.getUserByEmailOrDefault(source.getSupervisorEmail());

        // Set UserDto fields in the destination
        destination.setOwner(owner);
        destination.setSupervisor(supervisor);

        return destination;
    }
}
