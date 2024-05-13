package org.example.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.User;
import org.example.userservice.dto.login.LoginRequest;
import org.example.userservice.dto.login.LoginResponse;
import org.example.userservice.dto.register.RegisterUserResponse;
import org.example.userservice.dto.register.RegisterUserRequest;
import org.example.userservice.exception.exceptions.UserNotFoundException;
import org.example.userservice.model.UserAccount;
import org.example.userservice.model.UserDetails;
import org.example.userservice.repository.UserAccountRepository;
import org.example.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Destination;
import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;

    @Override
    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        UserDetails userDetails = modelMapper.map(registerUserRequest, UserDetails.class);

        UserAccount userAccount = UserAccount.builder()
                .email(registerUserRequest.getEmail().toLowerCase())
                .password(registerUserRequest.getPassword())
                .role(registerUserRequest.getUserRole())
                .username(generateUsername(registerUserRequest.getFirstName(), registerUserRequest.getLastName()))
                .userDetails(userDetails)
                .build();

        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        return RegisterUserResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .response("Saved successfully")
                .user(new User(savedUserAccount.getEmail(), savedUserAccount.getRole(), ""))
                .build();
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findByEmail(request
                .getEmail()
                .toLowerCase());

//        if(optionalUserAccount.isEmpty()){
//            return  LoginResponse
//                    .builder()
//                    .httpStatus(HttpStatus.NOT_FOUND)
//                    .response("User with such email not found")
//                    .build();
//        }

        UserAccount user = optionalUserAccount.orElseThrow(UserNotFoundException::new);

        return LoginResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .response("Found successfully")
                .user(new User(user.getEmail(), user.getRole(), user.getPassword()))
                .build();
    }

    private static String generateUsername(String firstName, String lastName) {
        // Concatenate the first and last names
        String combinedName = (firstName + lastName).toLowerCase();
        StringBuilder username = new StringBuilder("@");

        // Add word characters (alphanumeric or underscore) from the combined name
        for (char character : combinedName.toCharArray()) {
            if (Character.isLetterOrDigit(character)) {
                username.append(character);
            }
        }

        // Add digits if needed to adhere to the regex pattern
        if (username.length() <= 1) {
            username.append("1"); // Append a digit if the username is empty or contains only "@"
        }

        return username.toString();
    }
}
