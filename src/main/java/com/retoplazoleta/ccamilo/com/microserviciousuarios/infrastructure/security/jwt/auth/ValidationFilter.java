package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth;

import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.model.User;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.auth.AuthenticatedUser;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.TokenJwtConfig;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.shared.util.ResponseUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.ResponseMessages.TOKEN_INVALIDO;


public class ValidationFilter extends BasicAuthenticationFilter
{

    private final IUserPersistencePort userPersistencePort;
    private final String ERROR = "error";

    public ValidationFilter(AuthenticationManager authenticationManager, IUserPersistencePort userPersistencePort) {
        super(authenticationManager);
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");
        try {
            Claims claims = Jwts.parser().verifyWith(TokenJwtConfig.SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String usename = claims.getSubject();
            User user = userPersistencePort.getUsuarioByCorreo(usename);
            Long idUser = user.getId();
            String rol = user.getRol().getNombre();
            Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
            AuthenticatedUser userAuthenticate = new AuthenticatedUser(
                    idUser.toString(),
                    usename,
                    null,
                    authorities
            );
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userAuthenticate, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            Map<String, Object> body = new HashMap<>();
            body.put(ERROR, e.getMessage());
            ResponseUtils.buildResponse(TOKEN_INVALIDO.getMessage(), body, HttpStatus.UNAUTHORIZED);
            ResponseUtils.write(response, ResponseUtils.buildResponse(TOKEN_INVALIDO.getMessage(), body, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED.value());

        }

    }


}
