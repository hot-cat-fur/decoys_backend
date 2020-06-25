package pesko.orgasms.app.service.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pesko.orgasms.app.domain.entities.Jwt;
import pesko.orgasms.app.domain.models.service.JwtServiceModel;
import pesko.orgasms.app.service.JwtService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class JwtScheduleComponent {

    private final JwtService service;
    private final static Long FIXED_TIME=24*60*60L;
    @Autowired
    public JwtScheduleComponent(JwtService service) {
        this.service = service;
    }

    @Scheduled(fixedRate = 5*60*1000)
    public void jwtPatrol(){

        List<JwtServiceModel>jwts= this.service.findAll();

        for(int i=0;i<jwts.size();i++){
            if(jwts.get(i).getExpiresOn().before(Date.valueOf(LocalDate.now()))){
                service.deleteToken(jwts.get(i).getToken());
            }
        }
    }


}
