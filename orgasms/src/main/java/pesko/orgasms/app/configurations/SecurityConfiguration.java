package pesko.orgasms.app.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pesko.orgasms.app.configurations.jwt.JwtConfiguration;
import pesko.orgasms.app.configurations.jwt.JwtTokenVerifier;
import pesko.orgasms.app.configurations.jwt.JwtUsernameAndPasswordAutchenticationFilter;
import pesko.orgasms.app.service.JwtService;
import pesko.orgasms.app.service.impl.UserServiceImpl;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


     private final UserServiceImpl userService;
     private final BCryptPasswordEncoder bCryptPasswordEncoder;
     private final JwtConfiguration jwtConfiguration;
     private final JwtService jwtService;
     @Autowired
    public SecurityConfiguration(UserServiceImpl userService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtConfiguration jwtConfiguration, JwtService jwtService) {
        this.userService = userService;
         this.bCryptPasswordEncoder = bCryptPasswordEncoder;
         this.jwtConfiguration = jwtConfiguration;
         this.jwtService = jwtService;
     }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                 .cors().configurationSource(corsConfigurationSource())
               .and()

                .csrf().disable()
                .sessionManagement()
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAutchenticationFilter(authenticationManager(), jwtConfiguration,jwtService))
                .addFilterAfter(new JwtTokenVerifier(jwtConfiguration,jwtService),JwtUsernameAndPasswordAutchenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/register/**","/charge/**","/login","/orgasm/**","/mail/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/profile","/logoff").hasRole("USER")
                .anyRequest()
                .authenticated()
                  ;

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
         DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
         provider.setPasswordEncoder(bCryptPasswordEncoder);
         provider.setUserDetailsService(userService);
         return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","https://decoys.herokuapp.com"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
