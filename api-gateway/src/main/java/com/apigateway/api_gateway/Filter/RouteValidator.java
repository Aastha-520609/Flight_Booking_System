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

    public Predicate<ServerHttpRequest> isOpenApi =
            request -> openApiEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().startsWith(uri));

    public Predicate<ServerHttpRequest> isAdminApi =
            request -> adminEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().startsWith(uri));

    public Predicate<ServerHttpRequest> isSecured =
            request -> !isOpenApi.test(request);
}
