package pesko.orgasms.app.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event extends BaseEntity{



    @Column
    private String info;
    @Column
    @Length(min = 1)
    private String location;
    @Column
    @NotNull
    private LocalDateTime date;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private EventType type;

}
