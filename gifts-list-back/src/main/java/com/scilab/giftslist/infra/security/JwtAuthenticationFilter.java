package com.scilab.giftslist.infra.security;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.infra.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private static final String HEADER_AUTHORIZATION="Authorization";
    private static final String BEARER_PREFIX="Bearer ";
    
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader(HEADER_AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX)){
            //Early return, there is no authentication
            filterChain.doFilter(request,response);
            return;
        }

        final String jwt= StringUtils.substringAfter(authHeader,BEARER_PREFIX);
        final String username =jwtService.extractJwtUsername(jwt);
        if(StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null){
            //The username is present and is not yet authenticated
            this.userService.findByUserNameOrEmail(username).ifPresent(userdetails->{
                if(jwtService.isTokenValid(jwt,userdetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userdetails,
                        null,
                        userdetails.getAuthorities()
                    );
                    authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            });
        }
        filterChain.doFilter(request,response);
    }
}
