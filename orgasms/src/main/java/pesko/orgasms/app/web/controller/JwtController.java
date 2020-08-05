package pesko.orgasms.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pesko.orgasms.app.domain.models.binding.JwtBindingModel;
import pesko.orgasms.app.domain.models.info.InfoModel;
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
    public ResponseEntity<InfoModel> logout(HttpServletRequest request){

       String token= request.getHeader("Authorization");
        jwtService.deleteToken(token.replace("Bearer ",""));

        return ResponseEntity.ok().body(new InfoModel("Logged Out"));
    }

    @PostMapping("/valid")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InfoModel> validateToken(HttpServletRequest request, HttpServletResponse response){
        String token= request.getHeader("Authorization");
        JwtServiceModel jwt= this.jwtService.findByToken(token.replace("Bearer ",""));

        if(jwt!=null && jwt.getExpiresOn().after(new Date())){
            return ResponseEntity.status(401).body(new InfoModel("Valid Token"));
        }

        return ResponseEntity.status(403).body(new InfoModel("Invalid Token"));
    }
}
