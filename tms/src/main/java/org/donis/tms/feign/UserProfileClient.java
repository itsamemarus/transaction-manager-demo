package org.donis.tms.feign;

import org.donis.models.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-profile-service", url = "${user-profile-service.url}")
public interface UserProfileClient {


    // NOTE: Feign client exception/Bad requests/etc should be handled and the user should receive proper response
    // and normally this is logged for further debugging
    @GetMapping("/user")
    UserDTO getUserInfo(@RequestHeader("Authorization") String authorizationHeader);

}
