package pesko.orgasms.app.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jwts")
@Getter
@Setter
public class Jwt extends BaseEntity{

    @Column(unique = true,nullable = false,columnDefinition = "LONGTEXT")
    private String token;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "expires_on")
    private Date expiresOn;


}
