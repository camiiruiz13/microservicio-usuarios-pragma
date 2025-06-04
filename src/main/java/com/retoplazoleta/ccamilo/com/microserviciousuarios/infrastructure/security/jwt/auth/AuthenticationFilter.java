package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.dto.GenericResponseDTO;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ErrorException.ERROR_CREDENCIALES;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.SESSION_SUCCES;

@RequiredArgsConstructor
public class AuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    private final String AUTHORITHIES ="authorities";
    private final String USERNAME = "username";
    private final String TOKEN = "token";



    public Authentication authenticateUser(User user) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getCorreo(), user.getClave());
        return authenticationManager.authenticate(authenticationToken);
    }

    public GenericResponseDTO generateTokenResponse(Authentication authResult) throws IOException {

        AuthenticatedUser user = (AuthenticatedUser) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();


        Claims claims = Jwts.claims()
                .add(AUTHORITHIES, new ObjectMapper().writeValueAsString(roles))
                .add(USERNAME, username)
                .build();

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();

        Map<String, Object> body = new HashMap<>();
        body.put(TOKEN, token);
        body.put(USERNAME, username);

        return ResponseUtils.buildResponse(SESSION_SUCCES.getMessage() +  username, body, HttpStatus.OK);


    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
          String password =  new BCryptPasswordEncoder().encode("12345");


            return authenticateUser(user);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_CREDENCIALES.getMessage(), e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        GenericResponseDTO genericResponseDTO = generateTokenResponse(authResult);

        String token = TokenJwtConfig.PREFIX_TOKEN + ((Map<String, Object>) genericResponseDTO.getObjectResponse()).get("token").toString();


        ResponseUtils.write(response, genericResponseDTO, HttpStatus.OK.value(), token);

    }
}
