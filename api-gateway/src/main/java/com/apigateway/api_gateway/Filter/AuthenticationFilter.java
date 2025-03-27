package com.apigateway.api_gateway.Filter;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.apigateway.api_gateway.Util.JwtUtil;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    
    @Autowired
    private RouteValidator validator;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final List<String> PUBLIC_ROUTES = Arrays.asList(
        "/auth/register", "/auth/login"
    );

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String requestPath = exchange.getRequest().getURI().getPath();
            
            // Allow public routes without authentication
            if (PUBLIC_ROUTES.contains(requestPath)) {
                return chain.filter(exchange);
            }
            
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            
            try {
                jwtUtil.validateToken(authHeader);
                String userRole = jwtUtil.extractRole(authHeader);
                exchange.getRequest().mutate()
                    .header("X-User-Role", userRole)
                    .build();
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            return chain.filter(exchange);
        };
    }
    
    public static class Config {
    }
}