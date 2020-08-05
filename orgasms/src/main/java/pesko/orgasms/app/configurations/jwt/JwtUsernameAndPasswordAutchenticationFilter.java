package pesko.orgasms.app.configurations.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pesko.orgasms.app.service.JwtService;
import pesko.orgasms.app.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


public class JwtUsernameAndPasswordAutchenticationFilter extends UsernamePasswordAuthenticationFilter {

    private  AuthenticationManager authenticationManager;
    private  final JwtConfiguration jwtConfiguration;
    private JwtService jwtService;


   @Autowired
    public JwtUsernameAndPasswordAutchenticationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration,JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        this.jwtService=jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {



        Authentication authentication=null;

        try {
            UsernameAndPasswordAutchenticationRequest authRequest=
                    new ObjectMapper().readValue(request.getInputStream(),UsernameAndPasswordAutchenticationRequest.class);

            Authentication authenticate = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );
            authentication   = authenticationManager.authenticate(authenticate);


              return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

     String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities",authResult.getAuthorities()) //Claims is the body
                .setIssuedAt(new Date()) // Token Life
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfiguration.getTokenExpirationAfterDays())))
                .signWith(jwtConfiguration.getSecretKeyForSigning()) //Key
                .compact(); //Create (Build)


        response.addHeader(jwtConfiguration.getAuthorizationHeader(),jwtConfiguration.getTokenPrefix() + token);


        this.jwtService.saveToken(token);
    }
}
