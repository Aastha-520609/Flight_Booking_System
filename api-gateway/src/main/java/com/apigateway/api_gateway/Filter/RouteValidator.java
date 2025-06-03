package com.apigateway.api_gateway.Filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login"
    );

    public static final List<String> adminEndpoints = List.of(
    	    "/flights/add",
    	    "/flights/update",
    	    "/flights/delete"
    );
    
    public static final List<String> userEndpoints = List.of(
            "/flights/search",
            "/flights/id",
            "/flights" ,
            "/flights/update",
            "/bookings/book",
            "/bookings"
    );

    public Predicate<ServerHttpRequest> isOpenApi =
            request -> {
            	String path = request.getURI().getPath().trim().replaceAll("/+$", "");
                return openApiEndpoints.stream().anyMatch(path::equals);
            };
            
     public Predicate<ServerHttpRequest> isAdminApi =
            	    request -> {
            	        String path = request.getURI().getPath().trim();
            	        return adminEndpoints.stream()
            	                .anyMatch(path::startsWith);
          };
          
     public Predicate<ServerHttpRequest> isUserApi =
        	        request -> {
        	            String path = request.getURI().getPath().trim();
        	            return userEndpoints.stream().anyMatch(path::startsWith);
          };

    public Predicate<ServerHttpRequest> isSecured =
            request -> !isOpenApi.test(request);
}
