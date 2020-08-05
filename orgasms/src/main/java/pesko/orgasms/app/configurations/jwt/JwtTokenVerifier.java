package pesko.orgasms.app.configurations.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pesko.orgasms.app.domain.entities.Jwt;
import pesko.orgasms.app.domain.models.service.JwtServiceModel;
import pesko.orgasms.app.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfiguration jwtConfiguration;
    private JwtService jwtService;


    @Autowired
    public JwtTokenVerifier(JwtConfiguration jwtConfiguration, JwtService jwtService) {
        this.jwtConfiguration = jwtConfiguration;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {


        String authorizationHeader = httpServletRequest.getHeader(jwtConfiguration.getAuthorizationHeader());


        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(jwtConfiguration.getTokenPrefix())) {

            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        try {
            String token = authorizationHeader.replace(jwtConfiguration.getTokenPrefix(), "");


            JwtServiceModel exists = this.jwtService.findByToken(token);

            if (exists == null || exists.getExpiresOn().before(Date.valueOf(LocalDate.now()))) {
                throw new JwtException("Token cannot be trusted");
            }

            String secretKey = jwtConfiguration.getSecretKey();

            Jws<Claims> claimsJws = Jwts.parser()

                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String, String>> auth = (List<Map<String, String>>) body.get("authorities");

            List<SimpleGrantedAuthority> authorities = auth.stream().map(e -> new SimpleGrantedAuthority(e.get("authority")))
                    .collect(Collectors.toList());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities

            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new IllegalStateException("Token cannot be trusted");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
