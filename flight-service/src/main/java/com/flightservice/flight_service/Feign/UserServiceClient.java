package com.flightservice.flight_service.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "user-service", url = "http://localhost:8090")
public interface UserServiceClient {

    @GetMapping("/user/validate")
    ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);
}
