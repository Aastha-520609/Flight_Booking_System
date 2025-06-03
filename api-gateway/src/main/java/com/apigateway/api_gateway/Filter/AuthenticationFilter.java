package com.apigateway.api_gateway.Filter;

import com.apigateway.api_gateway.Util.JwtUtil;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

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

            if (validator.isOpenApi.test(request)) {
                System.out.println("Open API - no auth required for: " + path);
                return chain.filter(exchange);
            }

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                System.out.println("Missing Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || !token.startsWith("Bearer ")) {
                System.out.println("Invalid Authorization header format");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            token = token.substring(7);

            try {
                jwtUtil.validateToken(token);
                String role = jwtUtil.extractRole(token);
                System.out.println("JWT Role: " + role);

                boolean isAdminApi = validator.isAdminApi.test(request);
                boolean isUserApi = validator.isUserApi.test(request);

                if (isAdminApi && !"ADMIN".equalsIgnoreCase(role)) {
                    return denyAccess(exchange, "Access Denied: Only admins can access this route.");
                }

                if (isUserApi && !(role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("USER"))) {
                    return denyAccess(exchange, "Access Denied: Only user or admin roles allowed.");
                }

                // Forward role to downstream
                var mutatedRequest = request.mutate()
                		.headers(httpHeaders -> httpHeaders.add("X-User-Role", role))
                        .build();

                var mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();

                return chain.filter(mutatedExchange);

            } catch (Exception e) {
                System.out.println("Token validation failed: " + e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    private Mono<Void> denyAccess(org.springframework.web.server.ServerWebExchange exchange, String message) {
        System.out.println(message);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String responseBody = "{\"message\":\"" + message + "\"}";
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        var buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {}
}
