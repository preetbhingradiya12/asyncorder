package com.asyncorder.jwtutil;

import com.asyncorder.entity.Token;
import com.asyncorder.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuth jwtAuthService;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtAuth jwtAuthService, TokenRepository tokenRepository){
        this.jwtAuthService = jwtAuthService;
        this.tokenRepository = tokenRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader   = request.getHeader("Authorization");
        if(authHeader == null|| !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try{
            Claims claims = jwtAuthService.parseToken(token);
            String userIdString = claims.getSubject();
            Object userRole = claims.get("role");
            String jti = claims.getId();

            // check revoked or not
            UUID userId = UUID.fromString(userIdString);
            Optional<Token> isRevoked = tokenRepository.findByJtiAndUserIdAndRevoked(jti, userId, false);

            if (isRevoked.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String jsonResponse = """
                {
                    "timestamp": "%s",
                    "status": 401,
                    "error": "Unauthorized",
                    "message": "You are not login or Token has expired, Please login and try again"
                }
                """.formatted(java.time.LocalDateTime.now());

                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
                return;
            }


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(new CustomPrincipal(userId, jti, userRole), null, Collections.emptyList());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Internal error\", \"status\":500}"
            );

        }

        filterChain.doFilter(request, response);
    }
}
