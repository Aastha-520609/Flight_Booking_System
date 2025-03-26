package com.apigateway.api_gateway.Filter;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.apigateway.api_gateway.Util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // ✅ Allow auth-related requests without checking JWT
        if (path.startsWith("/user-service/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorizedResponse(exchange);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.isValidToken(token)) {
            return unauthorizedResponse(exchange);
        }

        // Extract Role
        Claims claims = jwtUtil.extractClaims(token);
        String role = claims.get("role", String.class);
        System.out.println("User Role: " + role);

        // Add role in headers for downstream services
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-Role", role)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
