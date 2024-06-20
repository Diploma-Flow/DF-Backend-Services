package org.example.userservice.mapper;

import org.example.userservice.dto.UserDto;
import org.example.userservice.model.UserAccount;
import org.example.userservice.model.UserDetails;
import org.modelmapper.PropertyMap;

/**
 * Author: Simeon Popov
 * Date of creation: 6/20/2024
 */
public class UserAccountToUserDtoMap extends PropertyMap<UserAccount, UserDto> {
    @Override
    protected void configure() {
        map().setFirstName(source.getUserDetails().getFirstName());
        map().setLastName(source.getUserDetails().getLastName());
        map().setMiddleName(source.getUserDetails().getMiddleName());
    }
}
