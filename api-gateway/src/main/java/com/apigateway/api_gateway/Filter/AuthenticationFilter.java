package com.apigateway.api_gateway.Filter;

import com.apigateway.api_gateway.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var path = request.getURI().getPath();

            System.out.println("Incoming request path: " + path);

            // Skip open APIs
            if (validator.isOpenApi.test(request)) {
                System.out.println("Open API - no auth required for: " + path);
                return chain.filter(exchange);
            }

            // Check Authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                System.out.println("Missing Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            try {
                jwtUtil.validateToken(token);
                String role = jwtUtil.extractRole(token);

                System.out.println("JWT Role: " + role);

                boolean isAdminApi = validator.isAdminApi.test(request);
                System.out.println("Is Admin API: " + isAdminApi);

                // Restrict ADMIN-only routes
                if (isAdminApi && !"ADMIN".equalsIgnoreCase(role)) {
                    System.out.println("Access Denied: User role '" + role + "' not authorized for " + path);
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                // Pass user role to downstream services
                exchange = exchange.mutate()
                        .request(r -> r.headers(headers -> headers.add("X-User-Role", role)))
                        .build();

            } catch (Exception e) {
                System.out.println("Token validation failed: " + e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}
