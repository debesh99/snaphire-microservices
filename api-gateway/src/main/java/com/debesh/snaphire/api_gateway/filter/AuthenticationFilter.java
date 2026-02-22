package com.debesh.snaphire.api_gateway.filter;


import com.debesh.snaphire.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // Empty class for configuration properties if needed later
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            // 1. Check if the request is for an open endpoint (like /users/login or /users/register)
            if (exchange.getRequest().getURI().getPath().contains("/users/login") ||
                    exchange.getRequest().getURI().getPath().contains("/users/register")) {
                return chain.filter(exchange); // Let it pass without checking token
            }

            // 2. Check if Authorization header exists
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 3. Extract Token
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            // 4. Validate Token and Extract Claims
            try {
                jwtUtil.validateToken(authHeader);
                Claims claims = jwtUtil.extractAllClaims(authHeader);

                // 5. Mutate the request: Add the user ID and Role as headers for the microservices!
                String role = claims.get("role", String.class);
                Long userId = claims.get("userId", Long.class);

                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(exchange.getRequest().mutate()
                                .header("X-User-Role", role)
                                .header("X-User-Id", String.valueOf(userId))
                                .build())
                        .build();

                return chain.filter(modifiedExchange);

            } catch (Exception e) {
                // Token is invalid or expired
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        });
    }
}