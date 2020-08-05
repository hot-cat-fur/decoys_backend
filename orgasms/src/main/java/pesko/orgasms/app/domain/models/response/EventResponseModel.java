package pesko.orgasms.app.domain.models.response;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pesko.orgasms.app.domain.entities.EventType;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventResponseModel {

    private Long id;


    private String info;

    private String location;

    private LocalDateTime date;

    private EventType type;
}
