package org.simo.dms.diplomamanagementservice.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */

@Log4j2
@Service
public class UserServiceClient {

    public static final String USER_SERVICE_BASE_URL = "lb://user-service";
    private final RestClient.Builder restClientBuilder;

    public UserServiceClient(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder.baseUrl(USER_SERVICE_BASE_URL);
    }

    @Cacheable(value = "userCache", key = "#email")
    public UserDto getUserByEmail(String email) {
        // Construct the URI with query parameters
        String uri = UriComponentsBuilder
                .fromUriString("/user/get-user")
                .queryParam("userEmail", email)
                .toUriString();

        // Perform the GET request with the query parameters
        UserDto userDto = restClientBuilder
                .build()
                .get()
                .uri(uri)
                .retrieve()
                .body(UserDto.class);

        return userDto;
    }

    @Cacheable(value = "userCache", key = "#email")
    public UserDto getUserByEmailOrDefault(String email) {
        // Construct the URI with query parameters
        String uri = UriComponentsBuilder
                .fromUriString("/user/get-user")
                .queryParam("userEmail", email)
                .toUriString();

        // Perform the GET request with the query parameters
        try{
            UserDto userDto = restClientBuilder
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(UserDto.class);

            return userDto;

        }catch (Exception e){
            log.error("UserClient exception: ", e);
            return UserDto.builder().email(email).build();
        }
    }
}
