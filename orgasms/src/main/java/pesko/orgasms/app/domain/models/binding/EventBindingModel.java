package pesko.orgasms.app.domain.models.binding;

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
public class EventBindingModel {




    private String info;

    @Length(min = 1)
    @NotNull
    private String location;
    @NotNull
    @FutureOrPresent
    private LocalDateTime date;

    private EventType type;
}
