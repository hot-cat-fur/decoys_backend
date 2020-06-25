package pesko.orgasms.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pesko.orgasms.app.domain.models.binding.JwtBindingModel;
import pesko.orgasms.app.domain.models.service.JwtServiceModel;
import pesko.orgasms.app.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.Date;

@RestController
public class JwtController {

    private final JwtService jwtService;

    @Autowired
    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/logoff")
    @PreAuthorize("hasRole('USER')")
    public void logout(HttpServletRequest request){

       String token= request.getHeader("Authorization");
        jwtService.deleteToken(token.replace("Bearer ",""));

    }

    @PostMapping("/valid")
    @PreAuthorize("hasRole('USER')")
    public void validateToken(HttpServletRequest request, HttpServletResponse response){
        String token= request.getHeader("Authorization");
        JwtServiceModel jwt= this.jwtService.findByToken(token.replace("Bearer ",""));

        if(jwt!=null && jwt.getExpiresOn().after(new Date())){
            response.setStatus(200);
        }else {
            response.setStatus(403);
        }
    }
}
