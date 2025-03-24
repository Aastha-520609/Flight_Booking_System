package com.bookingservice.booking_service.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8090/users")
public interface UserServiceClient {

    @GetMapping("/getUserId")
    Long getUserIdByUsername(@RequestParam String username);
}
