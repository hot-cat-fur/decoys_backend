package pesko.orgasms.app.domain.models.service;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class JwtServiceModel {

    private String token;

    private Date createdOn;

    private Date expiresOn;
}
